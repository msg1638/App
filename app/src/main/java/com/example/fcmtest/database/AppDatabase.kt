package com.example.fcmtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FallEvent::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fallEventDao(): FallEventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fall_detection_db"
                )
                    .fallbackToDestructiveMigration() //추후 삭제
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}