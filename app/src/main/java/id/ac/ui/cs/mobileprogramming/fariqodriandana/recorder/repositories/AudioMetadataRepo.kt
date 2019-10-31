package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories

import android.content.Context
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioMetadataDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databases.AppDatabase
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

class AudioMetadataRepo (private val audioMetadataDao: AudioMetadataDao) {
    companion object {
        private lateinit var audioMetadataRepo: AudioMetadataRepo

        fun getInstance(context: Context): AudioMetadataRepo {
            if (!::audioMetadataRepo.isInitialized) {
                audioMetadataRepo = AudioMetadataRepo(AppDatabase.getInstance(context)!!.audioMetadataDao())
            }
            return audioMetadataRepo
        }
    }

    suspend fun insert(audioMetadata: AudioMetadata): Long {
//        if (audioFile.id != null) {
//            audioMetadata.fileId = audioFile.id
            return audioMetadataDao.insert(audioMetadata)
//        } else {
//            throw Exception("Audio file must have been inserted first")
//        }
    }
}