package com.example.badmintonai.repository

import com.example.badmintonai.database.LogDatabase
import com.example.badmintonai.model.Log

class LogRepository(private val db: LogDatabase) {

    suspend fun insertLog(log: Log) = db.getLogDao().insertLog(log)
    suspend fun deleteLog(log: Log) = db.getLogDao().deleteLog(log)
    suspend fun updateLog(log: Log) = db.getLogDao().updateLog(log)

    fun getAllLogs() = db.getLogDao().getAllLogs()
    fun searchLog(query: String?) = db.getLogDao().searchLog(query)
}
