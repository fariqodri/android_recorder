package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.receivers.TimerReceiver
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioFilesRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioMetadataRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.RecorderService
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.TIMER_ACTION
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.RecorderViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RecorderViewModel
    private lateinit var filename: String
    private lateinit var timerReceiver: TimerReceiver
    private lateinit var fileLocation: String
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(RecorderViewModel::class.java)
        ActivityCompat.requestPermissions(this, viewModel.permissions,
            REQUEST_RECORD_AUDIO_PERMISSION
        )
        Log.d("isChangingConfig", isChangingConfigurations.toString())
        timerReceiver = TimerReceiver.getInstance(viewModel)!!
        val intentFilter = IntentFilter()
        intentFilter.addAction(TIMER_ACTION)
        registerReceiver(timerReceiver, intentFilter)
        viewModel.findAll(this).observe(this, Observer {
            Log.d("files", it.toString())
        })
        parentJob = Job()
        coroutineScope = CoroutineScope(parentJob)
        start_record_button.setOnClickListener {
            val startStopButton = it as Button
            if (viewModel.isStart.value!!) {
                onRecord(true)
                viewModel.isStart.value = false
                startStopButton.text = getString(R.string.stop_rec)
                Log.d("isStart", viewModel.isStart.toString())
            } else {
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
        findViewById<View>(R.id.list_menu).setOnClickListener {
            val intent = Intent(this, AudioListActivity::class.java)
            startActivity(intent)
        }
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
            saveAudio()
        }
    }

    private fun startRecord() {
        val startIntent = Intent(this, RecorderService::class.java)
        filename = "${Date().time}.m4a"
        fileLocation = "$filesDir/$filename"
        startIntent.putExtra("filename", fileLocation)
        startForegroundService(startIntent)
    }

    private fun stopRecord() {
        val stopIntent = Intent(this, RecorderService::class.java)
        stopService(stopIntent)
    }

    private fun saveAudio() {
        coroutineScope.async(Dispatchers.Default) {
            val audioFileId = viewModel.insertAudioFile(AudioFile(fileLocation = fileLocation, fileName = filename), this@MainActivity)
            viewModel.insertAudioMetadata(AudioMetadata(timestamp = Date().time, duration = viewModel.duration, fileId = audioFileId), this@MainActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timerReceiver)
        if (!isChangingConfigurations){
            stopRecord()
        }
        parentJob.cancel()
    }
}
