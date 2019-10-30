package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecorderViewModel : ViewModel() {
    var permissionToRecordAccepted = false
    var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    val isStart: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
    val currentSec: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}