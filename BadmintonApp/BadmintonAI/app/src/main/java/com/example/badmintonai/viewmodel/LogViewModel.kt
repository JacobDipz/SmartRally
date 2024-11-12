package com.example.badmintonai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.badmintonai.model.Log
import com.example.badmintonai.repository.LogRepository
import kotlinx.coroutines.launch

class LogViewModel(app: Application, private val logRepository: LogRepository): AndroidViewModel(app) {
    fun addLog(log: Log) =
        viewModelScope.launch {
            logRepository.insertLog(log)
        }

    fun deleteLog(log: Log) =
        viewModelScope.launch {
            logRepository.deleteLog(log)
        }

    fun updateLog(log: Log) =
        viewModelScope.launch {
            logRepository.updateLog(log)
        }

    fun getAllLogs() = logRepository.getAllLogs()

    fun searchLog(query: String?) =
        logRepository.searchLog(query)
}