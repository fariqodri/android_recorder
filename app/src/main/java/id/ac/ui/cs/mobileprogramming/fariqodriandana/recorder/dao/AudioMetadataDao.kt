package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao

import androidx.room.Dao
import androidx.room.Query
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

@Dao
interface AudioMetadataDao {
    @Query("SELECT * FROM audio_metadatas")
    fun getAll(): List<AudioMetadata>

//    @Query("SELECT ")
}