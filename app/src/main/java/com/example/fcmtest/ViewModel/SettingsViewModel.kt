package com.example.fcmtest.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.fcmtest.FallAPIClient

class SettingsViewModel : ViewModel() {
    var serverUrl: String = ""

    fun UpdateServerUrl(url: String) {
        serverUrl = url
    }

    fun sendTokenToServer(context: Context) {
        FallAPIClient.setServerUrl(serverUrl)
        FallAPIClient.sendTokenToServer()
    }
}