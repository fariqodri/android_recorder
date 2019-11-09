package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databinding.AudioListItemBinding
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databinding.FragmentPlayerBinding
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers.PlayerViewEventHandler
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments.PlayerFragment
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.AudioListItemModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.PlayerService
import java.text.SimpleDateFormat
import java.util.*

class AudioListAdapter (val audioFiles: List<AudioFileWithMetadata>, val context: Context, val eventHandler: PlayerViewEventHandler): RecyclerView.Adapter<AudioListAdapter.AudioItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioItemViewHolder {
        val layoutInflater = LayoutInflater.from(context)
//        LayoutInflater.from(context).inflate(R.layout.audio_list_item, parent, false)
        val binding = AudioListItemBinding.inflate(layoutInflater, parent, false)
        return AudioItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return audioFiles.size
    }

    override fun onBindViewHolder(holder: AudioItemViewHolder, position: Int) {
        val audioFile = audioFiles[position]
        val timestamp = Date(audioFile.metadatas!!.timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("in_ID"))
        val timeComplexFormat = SimpleDateFormat("HH:mm:ss", Locale("in_ID"))
        val duration = audioFile.metadatas!!.duration
        val minute = duration / 60
        val secs = duration % 60
        val filename = context.getString(R.string.list_filename, audioFile.metadatas!!.textTranslation)
        val info = context.getString(R.string.list_item_info, String.format("%02d", minute), String.format("%02d", secs), dateFormat.format(timestamp), timeComplexFormat.format(timestamp))
        holder.bind(eventHandler, AudioListItemModel(filename, info, audioFile))
//        val filename = "Audio Recording of \"${audioFile.metadatas!!.textTranslation}\""
//        holder.info.text = "${String.format("%02d", minute)}:${String.format("%02d", secs)} on ${dateFormat.format(timestamp)} ${timeComplexFormat.format(timestamp)}"
    }

    inner class AudioItemViewHolder (private val binding: AudioListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(eventHandlers: PlayerViewEventHandler, audioListItem: AudioListItemModel) {
            binding.eventHandler = eventHandlers
            binding.audioListItem = audioListItem
            binding.context = context
            binding.lifecycleOwner = context as MainActivity
            binding.executePendingBindings()
        }
    }
}