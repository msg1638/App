package com.example.fcmtest.Util

import android.content.Context
import androidx.core.content.edit

object ServerIpManager {
    private const val PREF_NAME = "AppPrefs"
    private const val KEY_SERVER_IP = "server_ip"

    fun saveServerIp(context: Context, ip: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(KEY_SERVER_IP, ip) }
    }

    fun getServerIp(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_SERVER_IP, "") ?: "10.0.2.2"
    }
}