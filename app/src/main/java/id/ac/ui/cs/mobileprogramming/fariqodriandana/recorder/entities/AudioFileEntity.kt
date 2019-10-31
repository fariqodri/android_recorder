package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "audio_files")
data class AudioFile (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val fileLocation: String,
    val fileName: String
)