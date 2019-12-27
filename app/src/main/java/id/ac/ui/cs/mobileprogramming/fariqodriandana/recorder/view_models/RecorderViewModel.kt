package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.RecordingModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioFilesRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.AudioMetadataRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.SpeechToTextRepo
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.UserRepo
import kotlinx.coroutines.*

class RecorderViewModel : ViewModel() {
    val isStart: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
    val currentSec: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val textTranslation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isLoading by lazy { MutableLiveData<Boolean>(false) }

    var recordingModel: RecordingModel? = null

    private val job by lazy {
        Job()
    }

    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.Default + job)
    }
//    var duration: Int = 0

    private lateinit var audioFilesRepo: AudioFilesRepo
    private lateinit var audioMetadataRepo: AudioMetadataRepo
    private lateinit var userRepo: UserRepo

    private suspend fun insertAudioFile(audioFile: AudioFile, ctx: Context): Long {
        audioFilesRepo = AudioFilesRepo.getInstance(ctx)!!
        return withContext(Dispatchers.Default) {
            return@withContext audioFilesRepo.insert(audioFile)
        }
    }

    private suspend fun insertAudioMetadata(audioMetadata: AudioMetadata, ctx: Context) {
        audioMetadataRepo = AudioMetadataRepo.getInstance(ctx)
        withContext(Dispatchers.Default) {
            audioMetadataRepo.insert(audioMetadata)
        }
    }

    fun recognizeSpeech(fileLocation: String, fileName: String, channelId: String, context: Application) {
        val speechToTextRepo = SpeechToTextRepo.getInstance()
        speechToTextRepo.recognizeSpeech(fileLocation, fileName, channelId, {
            textTranslation.value = it
            recordingModel?.textTranslation = it
            saveAudio(recordingModel!!, context)
            isLoading.value = false
        }, {
            textTranslation.value = null
            isLoading.value = false
        })
    }

    fun saveAudio(recordingModel: RecordingModel, context: Context): Job {
        userRepo = UserRepo.getInstance(context)
        return coroutineScope.launch(Dispatchers.Default) {
            val userName = userRepo.findLast().first()
            val audioFileId = insertAudioFile(AudioFile(
                fileLocation = recordingModel.fileLocation,
                fileName = recordingModel.fileName
            ), context)
            val timestampStart = recordingModel.timestampStart
            val timestampEnd = recordingModel.timestampEnd
            insertAudioMetadata(AudioMetadata(
                timestamp = timestampStart,
                duration = (timestampEnd - timestampStart).toInt() / 1000,
                fileId = audioFileId,
                textTranslation = recordingModel.textTranslation!!,
                userId = userName.id!!
            ), context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}