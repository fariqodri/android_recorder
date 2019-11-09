package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.enums.PlayEnum
import java.io.IOException
import java.lang.IllegalStateException

private const val CHANNEL_ID = "RecorderServiceChannelId"

class PlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var resumePosition: Int = 0
    private lateinit var playerReceiver: PlayerReceiver
    private lateinit var textTranslation: String

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter().apply {
            addAction(PlayEnum.PAUSE_ACTION.name)
            addAction(PlayEnum.RESUME_ACTION.name)
            addAction(PlayEnum.STOP_PLAYER_ACTION.name)
        }
        playerReceiver = PlayerReceiver()
        registerReceiver(playerReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filePath = intent?.getStringExtra("FILE_LOCATION")
        val filenameInDisplay = intent?.getStringExtra("TEXT_TRANSLATION")
        textTranslation = filenameInDisplay!!

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = buildForegroundNotification(filenameInDisplay, pendingIntent)
        startForeground(1, notification)
        startPlaying(filePath)
        return START_NOT_STICKY
    }

    private fun startPlaying(filePath: String?) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer = mediaPlayer!!.apply {
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
                val intent = Intent(PlayEnum.STOP_PLAYER_ACTION.name)
                sendBroadcast(intent)
                stopSelf()
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
            sendBroadcast(Intent(PlayEnum.PLAY_ACTION.name).apply {
                putExtra("TEXT_TRANSLATION", textTranslation)
            })
        }
    }

    private fun pausePlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                resumePosition = mediaPlayer!!.currentPosition
            }
        }

    }

    private fun resumePlaying() {
        if (mediaPlayer != null) {
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.seekTo(resumePosition)
                mediaPlayer!!.start()
            }
        }
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
            setContentTitle("Playing...")
            setContentText(filename)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        unregisterReceiver(playerReceiver)
    }

    inner class PlayerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                PlayEnum.PAUSE_ACTION.name -> {
                    pausePlaying()
                }
                PlayEnum.RESUME_ACTION.name-> {
                    resumePlaying()
                }
                PlayEnum.STOP_PLAYER_ACTION.name -> {
                    stopSelf()
                }
            }
        }
    }
}