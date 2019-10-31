package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import java.io.IOException
import java.lang.IllegalStateException

private const val CHANNEL_ID = "RecorderServiceChannelId"
const val PAUSE_PLAYER_ACTION = "PAUSE_PLAYER_ACTION"
const val RESUME_PLAYER_ACTION = "RESUME_PLAYER_ACTION"

class PlayerService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private var resumePosition: Int = 0
    private lateinit var playerReceiver: PlayerReceiver

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        val intentFilter = IntentFilter()
        playerReceiver = PlayerReceiver()
        intentFilter.addAction(PAUSE_PLAYER_ACTION)
        intentFilter.addAction(RESUME_PLAYER_ACTION)
        registerReceiver(playerReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filePath = intent?.getStringExtra("FILE_PATH")
        val filenameInDisplay = intent?.getStringExtra("DISPLAY_FILENAME")

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = buildForegroundNotification(filenameInDisplay, pendingIntent)
        startForeground(1, notification)
        startPlaying(filePath)
        return START_NOT_STICKY
    }

    private fun startPlaying(filePath: String?) {
        mediaPlayer = mediaPlayer.apply {
            setOnCompletionListener {
                it.release()
            }
            reset()
            setDataSource(filePath)
            try {
                prepare()
            } catch (e: IllegalStateException) {
                Log.e("Playing Error", e.message)
            } catch (e: IOException) {
                Log.e("Playing Error", e.message)
            }
            start()
        }
    }

    private fun pausePlaying() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            resumePosition = mediaPlayer.currentPosition
        }
    }

    private fun resumePlaying() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(resumePosition)
            mediaPlayer.start()
        }
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
            setContentTitle("Playing...")
            setContentText(filename)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        unregisterReceiver(playerReceiver)
    }

    inner class PlayerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                PAUSE_PLAYER_ACTION -> {
                    pausePlaying()
                }
                RESUME_PLAYER_ACTION -> {
                    resumePlaying()
                }
            }
        }
    }
}