package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioFileDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioMetadataDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.UserDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName

@Database(entities = [AudioFile::class, AudioMetadata::class, UserName::class], version = 8)
abstract class AppDatabase : RoomDatabase() {

    abstract fun audioFileDao(): AudioFileDao
    abstract fun audioMetadataDao(): AudioMetadataDao
    abstract fun userDao(): UserDao

    companion object {
        private lateinit var instance: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (!::instance.isInitialized) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "appdata.db").fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}