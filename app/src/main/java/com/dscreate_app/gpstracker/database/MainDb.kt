package com.dscreate_app.gpstracker.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TrackItem::class], version = 1, exportSchema = false)
abstract class MainDb: RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: MainDb? = null
        private const val DB_NAME = "GpsTracker.db"
        private val LOCK = Any()

        fun getInstanceDb(application: Application): MainDb {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val instance = Room.databaseBuilder(
                    application,
                    MainDb::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}