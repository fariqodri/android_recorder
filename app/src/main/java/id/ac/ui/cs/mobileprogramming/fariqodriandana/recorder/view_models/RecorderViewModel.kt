package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models

import android.Manifest
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioFilesRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioMetadataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecorderViewModel : ViewModel() {
    var permissionToRecordAccepted = false
    var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    val isStart: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
    val currentSec: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var duration: Int = 0

    private lateinit var audioFilesRepo: AudioFilesRepo
    private lateinit var audioMetadataRepo: AudioMetadataRepo

    suspend fun insertAudioFile(audioFile: AudioFile, ctx: Context): Long {
        audioFilesRepo = AudioFilesRepo.getInstance(ctx)!!
        return withContext(Dispatchers.Default) {
            return@withContext audioFilesRepo.insert(audioFile)
        }
    }

    suspend fun insertAudioMetadata(audioMetadata: AudioMetadata, ctx: Context) {
        audioMetadataRepo = AudioMetadataRepo.getInstance(ctx)
        withContext(Dispatchers.Default) {
            audioMetadataRepo.insert(audioMetadata)
        }
    }

    fun findAll(context: Context) : LiveData<List<AudioFileWithMetadata>> {
        audioFilesRepo = AudioFilesRepo.getInstance(context)!!
        return audioFilesRepo.findAll()
    }
}