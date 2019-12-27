package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments.RecorderFragment

class RecorderViewEventHandler(private val fragment: RecorderFragment) {
    fun recButtonOnClick(isStart: MutableLiveData<Boolean>) {
        if (isStart.value!!) {
            val cm = fragment.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                fragment.onRecord(true)
                isStart.value = false
            } else {
                Toast.makeText(fragment.activity, R.string.no_internet, Toast.LENGTH_SHORT).show()
            }
        } else {
            fragment.onRecord(false)
            isStart.value = true
        }
    }
}