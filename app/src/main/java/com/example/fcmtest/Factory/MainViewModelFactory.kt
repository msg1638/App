package com.example.fcmtest.Factory

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fcmtest.ViewModel.MainViewModel

class MainViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}