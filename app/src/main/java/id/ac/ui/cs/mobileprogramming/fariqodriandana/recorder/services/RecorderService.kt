package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services

import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import java.io.IOException
import java.lang.IllegalStateException

private const val CHANNEL_ID = "RecorderServiceChannelId"
const val TIMER_ACTION = "TIMER_ACTION"

class RecorderService : Service() {
    private var recorder: MediaRecorder? = null
    private var timerThread: Thread? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filename = intent?.getStringExtra("filename")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = buildForegroundNotification(filename, pendingIntent)
        startForeground(1, notification)
        startRecording(filename)
        Log.d("onStartCommand", "finished")
        return START_NOT_STICKY
    }

    private fun startRecording(filename: String?) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(filename)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)

            try {
                prepare()
            } catch (e: IllegalStateException) {
                Log.e("Recording", e.message)
            } catch (e: IOException) {
                Log.e("Recording", e.message)
            }
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
                    intent.action = TIMER_ACTION
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
        val intent = Intent()
        intent.action = TIMER_ACTION
        intent.putExtra("currentSec", 0)
        sendBroadcast(intent)
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        stopAndResetTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
    }

    private fun buildForegroundNotification(filename: String?, pendingIntent: PendingIntent): Notification {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
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