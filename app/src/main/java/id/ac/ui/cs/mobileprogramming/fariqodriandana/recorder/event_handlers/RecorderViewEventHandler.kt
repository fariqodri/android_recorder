package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers

import androidx.lifecycle.MutableLiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments.RecorderFragment

class RecorderViewEventHandler(private val fragment: RecorderFragment) {
    fun recButtonOnClick(isStart: MutableLiveData<Boolean>) {
        if (isStart.value!!) {
            fragment.onRecord(true)
            isStart.value = false
        } else {
            fragment.onRecord(false)
            isStart.value = true
        }
    }

}