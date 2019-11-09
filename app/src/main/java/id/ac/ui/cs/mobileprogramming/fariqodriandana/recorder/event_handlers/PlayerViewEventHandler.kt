package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments.PlayerFragment
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.services.PlayerService

class PlayerViewEventHandler(private val fragment: PlayerFragment) {
    fun resumePauseButtonOnClick(isPaused: MutableLiveData<Boolean>) {
        if (!isPaused.value!!) {
            isPaused.value = true
            fragment.pauseAudio()
        } else {
            isPaused.value = false
            fragment.resumeAudio()
        }
    }

    fun stopButtonOnClick(view: View) {
        fragment.stopAudio()
    }

    fun listItemOnClick(context: Context, audioFile: AudioFileWithMetadata) {
        val intent = Intent(context, PlayerService::class.java)
        intent.putExtra("FILE_LOCATION", audioFile.audioFile.fileLocation)
        intent.putExtra("TEXT_TRANSLATION", audioFile.metadatas!!.textTranslation)
        context.startForegroundService(intent)
    }
}