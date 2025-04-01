package com.example.fcmtest

import android.content.Context
import androidx.lifecycle.ViewModel

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
