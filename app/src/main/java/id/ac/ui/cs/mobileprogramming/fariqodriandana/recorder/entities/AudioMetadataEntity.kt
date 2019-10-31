package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "audio_metadatas", foreignKeys = [
        ForeignKey(entity = AudioMetadata::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.SET_NULL)
    ]
)
data class AudioMetadata (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val timestamp: Long,
    val duration: Int,
    @ColumnInfo(index = true)
    var fileId: Long
)