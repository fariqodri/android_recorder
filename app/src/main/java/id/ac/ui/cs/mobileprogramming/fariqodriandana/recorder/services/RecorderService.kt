package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services

import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.enums.RecordIntentActionEnum
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.RecordingModel
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import kotlin.properties.Delegates

private const val CHANNEL_ID = "RecorderServiceChannelId"

class RecorderService : Service() {
    private var recorder: MediaRecorder? = null
    private var timerThread: Thread? = null
    private lateinit var filename: String
    private lateinit var fileLocation: String
    private var timestampStart: Long by Delegates.notNull()
    private var timestampEnd: Long by Delegates.notNull()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        filename = intent?.getStringExtra("filename")!!
        fileLocation = intent.getStringExtra("fileLocation")!!
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = buildForegroundNotification(filename, pendingIntent)

        startForeground(1, notification)
        startRecording(fileLocation)
        Log.d("onStartCommand", "finished")
        return START_NOT_STICKY
    }

    private fun startRecording(filename: String?) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)

            try {
                setOutputFile(filename)
                prepare()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            timestampStart = Date().time
            start()
            Log.d("mediaRecorder", "start")
        }
        startTimer()
    }

    private fun startTimer() {
        val runnable = Runnable {
            var counter = 1
            while (true) {
                try {
                    val intent = Intent()
                    intent.action = RecordIntentActionEnum.TIMER_ACTION.name
                    intent.putExtra("currentSec", counter)
                    Thread.sleep(1000)
                    counter++
                    sendBroadcast(intent)
                } catch (e: InterruptedException) {
                    return@Runnable
                }
            }
        }
        timerThread = Thread(runnable)
        timerThread!!.start()
    }

    private fun stopAndResetTimer() {
        timerThread?.interrupt()
        timerThread = null
        val timerIntent = Intent(RecordIntentActionEnum.TIMER_ACTION.name)
        val stopRecordIntent = Intent(RecordIntentActionEnum.STOP_RECORDER_ACTION.name)
        stopRecordIntent.putExtra("recordingFile", RecordingModel(fileLocation, filename, timestampStart, timestampEnd, null))
        timerIntent.putExtra("currentSec", 0)
//        Log.d(stopRecordIntent.action,stopRecordIntent.action)
        sendBroadcast(timerIntent)
        sendBroadcast(stopRecordIntent)
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        timestampEnd = Date().time
        stopAndResetTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("RecorderService", "Destroyed")
        stopRecording()
    }

    private fun buildForegroundNotification(filename: String?, pendingIntent: PendingIntent): Notification {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.setSound(null, null)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Recording...")
            setContentText(filename)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }
        return builder.build()
    }

}