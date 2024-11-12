package com.example.badmintonai.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.badmintonai.model.Log

@Database(entities = [Log::class], version = 1)
abstract class LogDatabase: RoomDatabase() {
    abstract fun getLogDao(): logDao

    companion object {
        @Volatile
        private var instance: LogDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also{
                instance = it
            }
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LogDatabase::class.java,
                "log_db"
            ).build()

    }
}