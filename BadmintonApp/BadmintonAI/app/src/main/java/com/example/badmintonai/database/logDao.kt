package com.example.badmintonai.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.badmintonai.model.Log

@Dao
interface logDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE) // Will replace conflicting logs in the database with new log
    suspend fun insertLog(log: Log)

    @Update
    suspend fun updateLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)

    @Query("SELECT * FROM LOGS ORDER BY id DESC") // Newest log at the top, oldest at the bottom
    fun getAllLogs(): LiveData<List<Log>>

    @Query("SELECT * FROM LOGS WHERE logTitle LIKE :query OR logDesc LIKE :query") // Will match keyword to title or description in search
    fun searchLog(query: String?): LiveData<List<Log>>
}