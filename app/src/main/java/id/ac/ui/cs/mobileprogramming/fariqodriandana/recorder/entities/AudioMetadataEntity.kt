package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "audio_metadatas", foreignKeys = [
        ForeignKey(entity = AudioFile::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = UserName::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.SET_NULL)
    ]
)
data class AudioMetadata (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val timestamp: Long,
    val duration: Int,
    val textTranslation: String,
    @ColumnInfo(index = true)
    var fileId: Long,
    @ColumnInfo(index = true)
    val userId: Long
)