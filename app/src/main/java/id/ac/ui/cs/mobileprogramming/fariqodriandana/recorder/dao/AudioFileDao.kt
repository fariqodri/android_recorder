package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

@Dao
abstract class AudioFileDao {
    @Transaction
    @Query("SELECT * from audio_files")
    abstract fun findAll(): LiveData<List<AudioFileWithMetadata>>

    @Transaction
    @Query("SELECT * from audio_files WHERE id = :id")
    abstract fun findById(id: Int): LiveData<AudioFileWithMetadata>

    @Transaction
    @Query("SELECT * from audio_files WHERE fileLocation LIKE :location")
    abstract fun findAllbyLocation(location: String): LiveData<List<AudioFileWithMetadata>>

    @Query("DELETE FROM audio_files WHERE id = :id")
    abstract fun deleteById(id: Int)

    @Insert
    abstract suspend fun insert(audioFile: AudioFile): Long

    @Delete
    abstract fun delete(audioFile: AudioFile)

    @Update
    abstract fun update(audioFile: AudioFile)

//    @Transaction
//    suspend fun upsert(audioFile: AudioFile) {
//        val id = insert(audioFile)
//        if (id == -1L) {
//            update(audioFile)
//        }
//    }
}