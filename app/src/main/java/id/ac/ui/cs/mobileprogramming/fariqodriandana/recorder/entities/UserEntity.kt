package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "username")
data class UserName (
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val userName: String
)