package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

@Dao
interface AudioFileDao {
    @Transaction
    @Query("SELECT * from audio_files")
    fun findAll(): LiveData<List<AudioFileWithMetadata>>

    @Transaction
    @Query("SELECT * from audio_files WHERE id = :id")
    fun findById(id: Int): LiveData<AudioFileWithMetadata>

    @Transaction
    @Query("SELECT * from audio_files WHERE fileLocation LIKE :location")
    fun findAllbyLocation(location: String): LiveData<List<AudioFileWithMetadata>>

    @Query("DELETE FROM audio_files WHERE id = :id")
    fun deleteById(id: Int)

    @Insert
    fun insert(audioFile: AudioFile): Long

    @Delete
    fun delete(audioFile: AudioFile)

    @Update
    fun update(audioFile: AudioFile)

    @Transaction
    fun upsert(audioFile: AudioFile) {
        val id = insert(audioFile)
        if (id == -1L) {
            update(audioFile)
        }
    }
}