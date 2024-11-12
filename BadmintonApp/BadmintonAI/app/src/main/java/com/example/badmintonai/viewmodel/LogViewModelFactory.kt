package com.example.badmintonai.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.badmintonai.repository.LogRepository

class LogViewModelFactory(val app: Application, private val logRepository: LogRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LogViewModel(app, logRepository) as T
    }
}