package com.example.badmintonai

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.badmintonai.database.LogDatabase
import com.example.badmintonai.repository.LogRepository
import com.example.badmintonai.viewmodel.LogViewModel
import com.example.badmintonai.viewmodel.LogViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var logViewModel: LogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel(){
        val logRepository = LogRepository(LogDatabase(this))
        val viewModelProviderFactory = LogViewModelFactory(application, logRepository)
        logViewModel = ViewModelProvider(this, viewModelProviderFactory)[LogViewModel::class.java]
    }
}