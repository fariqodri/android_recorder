package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories

import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioFileDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata

class AudioFilesRepo(private val audioFileDao: AudioFileDao) {
    companion object {
        private var audioFilesRepo: AudioFilesRepo? = null
        fun getInstance(audioFileDao: AudioFileDao): AudioFilesRepo? {
            if (audioFilesRepo == null) {
                audioFilesRepo = AudioFilesRepo(audioFileDao)
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

    fun insert(audioFile: AudioFile) {
        audioFileDao.insert(audioFile)
    }

    fun save(audioFile: AudioFile) {
        audioFileDao.upsert(audioFile)
    }

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