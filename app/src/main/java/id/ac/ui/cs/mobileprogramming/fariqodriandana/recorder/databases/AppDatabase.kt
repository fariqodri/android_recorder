package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioFileDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.dao.AudioMetadataDao
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFile
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioMetadata

@Database(entities = [AudioFile::class, AudioMetadata::class], version = 6)
abstract class AppDatabase : RoomDatabase() {

    abstract fun audioFileDao(): AudioFileDao
    abstract fun audioMetadataDao(): AudioMetadataDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "appdata.db").fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}