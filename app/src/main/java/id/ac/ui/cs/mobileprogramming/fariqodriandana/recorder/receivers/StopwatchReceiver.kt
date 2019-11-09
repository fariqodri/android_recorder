package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.RecorderViewModel

class StopwatchReceiver(private val viewModel: RecorderViewModel): BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {
        val currentSec = intent?.getIntExtra("currentSec", 0)
        Log.d("CurrentSec", currentSec.toString())
        viewModel.currentSec.value = currentSec
//        viewModel.duration = currentSec!!
    }
}