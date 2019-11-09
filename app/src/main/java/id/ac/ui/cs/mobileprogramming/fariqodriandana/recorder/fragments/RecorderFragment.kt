package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments


import android.accounts.AccountManager
import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.startForegroundService
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers.RecorderViewEventHandler
import androidx.databinding.DataBindingUtil
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.enums.RecordIntentActionEnum.*
import androidx.lifecycle.ViewModelProviders
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.receivers.StopwatchReceiver
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.RecorderViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.RecorderService
import androidx.lifecycle.Observer
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databinding.FragmentRecorderBinding
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.RecordingModel
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Use the [RecorderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecorderFragment : Fragment() {
    private lateinit var activity: MainActivity
    private lateinit var recorderViewModel: RecorderViewModel
    private lateinit var stopwatchReceiver: StopwatchReceiver
    private lateinit var stopReceiver: RecordingFinishReceiver
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var parentJob: Job

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recorderViewModel = activity.run {
            ViewModelProviders.of(this)[RecorderViewModel::class.java]
        }
        parentJob = Job()
        coroutineScope = CoroutineScope(parentJob)
        observeViewModel()
        Log.d("RecordFragment", "Created")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<FragmentRecorderBinding>(inflater, R.layout.fragment_recorder, container, false)
        dataBinding.recordingViewModel = recorderViewModel
        dataBinding.eventHandler = RecorderViewEventHandler(this)
        dataBinding.lifecycleOwner = activity

        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        spawnReceivers()
    }


    private fun observeViewModel() {
        recorderViewModel.textTranslation.observe(this, Observer {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })
    }

    fun recognizeSpeech() {
        recorderViewModel.isLoading.value = true
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        recorderViewModel.recognizeSpeech(recorderViewModel.recordingModel!!.fileLocation, recorderViewModel.recordingModel!!.fileName, activity.application)
    }

    private fun spawnReceivers() {
        stopwatchReceiver = StopwatchReceiver(recorderViewModel)
        stopReceiver = RecordingFinishReceiver()
        val timerIntentFilter = IntentFilter(TIMER_ACTION.name)
        val stopIntentFilter = IntentFilter(STOP_RECORDER_ACTION.name)
        activity.registerReceiver(stopwatchReceiver, timerIntentFilter)
        activity.registerReceiver(stopReceiver, stopIntentFilter)
    }

    fun onRecord(start: Boolean) {
        if (start) {
            startRecord()
        } else {
            stopRecord()
//            saveAudio()
        }
    }

    private fun startRecord() {
        val startIntent = Intent(activity, RecorderService::class.java)
        val fileName = "${System.currentTimeMillis()}.m4a"
        val fileLocation = "${activity.getExternalFilesDir("")}/$fileName"
        startIntent.putExtra("filename", fileName)
        startIntent.putExtra("fileLocation", fileLocation)
        startForegroundService(activity, startIntent)
    }

    private fun stopRecord() {
        val stopIntent = Intent(activity, RecorderService::class.java)
        activity.stopService(stopIntent)
    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(stopwatchReceiver)
        activity.unregisterReceiver(stopReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!activity.isChangingConfigurations){
            stopRecord()
            parentJob.cancel()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            RecorderFragment()
    }

    internal inner class RecordingFinishReceiver : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val recordingResult = intent?.getParcelableExtra<RecordingModel>("recordingFile")!!
            recorderViewModel.recordingModel = recordingResult
            recognizeSpeech()
        }
    }
}
