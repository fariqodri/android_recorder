package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

@Dao
abstract class AudioMetadataDao {
    @Query("SELECT * FROM audio_metadatas")
    abstract fun getAll(): List<AudioMetadata>

    @Insert
    abstract suspend fun insert(audioMetadata: AudioMetadata): Long

//    @Query("SELECT ")
}