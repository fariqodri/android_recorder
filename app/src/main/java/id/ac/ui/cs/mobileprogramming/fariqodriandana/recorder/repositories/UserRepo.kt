package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.UserDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databases.AppDatabase
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName

class UserRepo (private val userDao: UserDao) {
    companion object {
        private lateinit var userRepo: UserRepo

        fun getInstance(context: Context): UserRepo {
            if (!::userRepo.isInitialized) {
                userRepo = UserRepo(AppDatabase.getInstance(context).userDao())
            }
            return userRepo
        }
    }

    suspend fun insert(userName: UserName): Long {
        return userDao.insert(userName)
    }

    suspend fun findLast(): List<UserName> {
        return userDao.findLast()
    }
}