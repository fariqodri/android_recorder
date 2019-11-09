package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioFilesRepo

class PlayerViewModel : ViewModel() {
    lateinit var listOfAudioFileWithMetadata: LiveData<List<AudioFileWithMetadata>>
    val isPlaying by lazy { MutableLiveData<Boolean>() }
    val isPaused by lazy { MutableLiveData<Boolean>().apply { value = false } }
    val textTranslation by lazy { MutableLiveData<String>() }

    fun getAllFileWithMetadata(context: Context) {
        val audioFilesRepo = AudioFilesRepo.getInstance(context)
        listOfAudioFileWithMetadata = audioFilesRepo!!.findAll()
    }


}