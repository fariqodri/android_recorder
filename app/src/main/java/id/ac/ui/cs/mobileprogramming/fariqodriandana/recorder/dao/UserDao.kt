package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName

@Dao
abstract class UserDao {
    @Insert
    abstract suspend fun insert(user: UserName): Long

    @Query("SELECT * FROM username ORDER BY id DESC LIMIT 1")
    abstract suspend fun findLast(): List<UserName>
}