package dev.ogabek.pinterest.meneger

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ogabek.pinterest.database.ImageDao
import dev.ogabek.pinterest.model.ImageOffline

@Database(entities = [ImageOffline::class], version = 5)
abstract class RoomManager: RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {
        private var INSTANCE: RoomManager? = null
        fun getDatabase(context: Context): RoomManager? {
            if (INSTANCE == null) {
                synchronized(RoomManager::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            RoomManager::class.java, "images_db"
                        )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}