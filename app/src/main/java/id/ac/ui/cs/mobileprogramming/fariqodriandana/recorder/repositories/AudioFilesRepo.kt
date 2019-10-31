package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioFileDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databases.AppDatabase
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

class AudioFilesRepo(private val audioFileDao: AudioFileDao) {
    companion object {
        private lateinit var audioFilesRepo: AudioFilesRepo
        fun getInstance(context: Context): AudioFilesRepo? {
            if (!::audioFilesRepo.isInitialized) {
                audioFilesRepo = AudioFilesRepo(AppDatabase.getInstance(context)!!.audioFileDao())
            }
            return audioFilesRepo
        }
    }

    fun findById(id: Int): LiveData<AudioFileWithMetadata> {
        return audioFileDao.findById(id)
    }

    fun findAll(): LiveData<List<AudioFileWithMetadata>> {
        return audioFileDao.findAll()
    }

    suspend fun insert(audioFile: AudioFile): Long {
        return audioFileDao.insert(audioFile)
    }

//    suspend fun save(audioFile: AudioFile) {
//        audioFileDao.upsert(audioFile)
//    }

    fun delete(audioFile: AudioFile) {
        audioFileDao.delete(audioFile)
    }

    fun delete(id: Int) {
        audioFileDao.deleteById(id)
    }

    fun update(audioFile: AudioFile) {
        audioFileDao.update(audioFile)
    }
}