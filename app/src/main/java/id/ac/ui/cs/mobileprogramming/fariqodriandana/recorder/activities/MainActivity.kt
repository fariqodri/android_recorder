package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.RecorderService
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.TIMER_ACTION
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.RecorderViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RecorderViewModel
    private lateinit var filename: String
    private lateinit var timerReceiver: TimerReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        ActivityCompat.requestPermissions(this, viewModel.permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )

        timerReceiver = TimerReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(TIMER_ACTION)
        registerReceiver(timerReceiver, intentFilter)

        start_record_button.setOnClickListener {
            val startStopButton = it as Button
            if (viewModel.isStart.value!!) {
                onRecord(true)
                viewModel.isStart.value = false
                startStopButton.text = getString(R.string.stop_rec)
                Log.d("isStart", viewModel.isStart.toString())
            } else {
                Log.d("file path", filename)
                onRecord(false)
                viewModel.isStart.value = true
                startStopButton.text = getString(R.string.start_rec)
                Log.d("isStart", viewModel.isStart.toString())
            }
        }
        viewModel.currentSec.observe(this, Observer {
            val minute = it / 60
            val second = it % 60
            timer.text = "${String.format("%02d", minute)}:${String.format("%02d", second)}"
        })

        viewModel.isStart.observe(this, Observer {
            if (it) {
                start_record_button.icon = getDrawable(R.drawable.round_button)
            } else {
                start_record_button.icon = getDrawable(R.drawable.stop_button)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if(!viewModel.permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecord()
        } else {
            stopRecord()
        }
    }

    private fun startRecord() {
        val startIntent = Intent(this, RecorderService::class.java)
        filename = Date().time.toString()
        startIntent.putExtra("filename", filename)
        startForegroundService(startIntent)
    }

    private fun stopRecord() {
        val stopIntent = Intent(this, RecorderService::class.java)
        stopService(stopIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations){
            unregisterReceiver(timerReceiver)
            val stopIntent = Intent(this, RecorderService::class.java)
            stopService(stopIntent)
        }
    }

    inner class TimerReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val currentSec = intent?.getIntExtra("currentSec", 0)
            Log.d("CurrentSec", currentSec.toString())
            viewModel.currentSec.value = currentSec
        }
    }
}
