package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.PAUSE_PLAYER_ACTION
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.PlayerService
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.RESUME_PLAYER_ACTION
import kotlinx.android.synthetic.main.audio_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

enum class PlayState {
    START, PAUSE, RESUME
}
// TODO: PAKE FRAGMENT SEBAGAI ITEM
class AudioListAdapter (val audioFiles: List<AudioFileWithMetadata>, val context: Context): RecyclerView.Adapter<AudioItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioItemViewHolder {
        return AudioItemViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return audioFiles.size
    }

    override fun onBindViewHolder(holder: AudioItemViewHolder, position: Int) {
        val audioFile = audioFiles[position]
        val timestamp = Date(audioFile.metadatas!!.timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("in_ID"))
        val timeSimpleFormat = SimpleDateFormat("h:mm a", Locale("in_ID"))
        val timeComplexFormat = SimpleDateFormat("HH:mm:ss", Locale("in_ID"))
        val duration = audioFile.metadatas!!.duration
        val minute = duration / 60
        val secs = duration % 60
        holder.fileName.text = "Audio Recording ${dateFormat.format(timestamp)} ${timeComplexFormat.format(timestamp)}"
        holder.info.text = "${String.format("%02d", minute)}:${String.format("%02d", secs)} on ${dateFormat.format(timestamp)} ${timeSimpleFormat.format(timestamp)}"

    }

    internal inner class PlayPauseButton : ImageButton(context) {
        var mStartPlaying = PlayState.START
        val clicker = OnClickListener {
            when (mStartPlaying) {
                PlayState.START -> {
                    val startIntent = Intent(context, PlayerService::class.java)
                    context.startService(startIntent)
                }
                PlayState.PAUSE -> {
                    val pauseIntent = Intent(context, PlayerService::class.java)
                    pauseIntent.action = PAUSE_PLAYER_ACTION
                    context.sendBroadcast(pauseIntent)
                }
                PlayState.RESUME -> {
                    val resumeIntent = Intent(context, PlayerService::class.java)
                    resumeIntent.action = RESUME_PLAYER_ACTION
                    context.sendBroadcast(resumeIntent)
                }
            }
        }
    }
}

class AudioItemViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val fileName: TextView = view.audio_file_name
    val info: TextView = view.audio_info
//    val playButton: ImageButton = view.play_button
}