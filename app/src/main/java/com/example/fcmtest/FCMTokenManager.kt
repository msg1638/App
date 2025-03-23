package com.example.fcmtest

import android.content.Context
import android.content.Intent
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object FCMTokenManager {

    private var serverUrl: String = "http://10.0.2.2:5000/register_token" //서버 URL

    // 서버 URL을 설정하는 함수
    fun setServerUrl(url: String) {
        serverUrl = url
    }

    // 서버로 토큰 전송하는 함수
    fun sendTokenToServer(token: String): String? { //토큰 전달 함수

        val client = OkHttpClient()
        val json = JSONObject().put("token", token).toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(serverUrl)
            .addHeader("Connection","close")
            .post(body)
            .build()

        return try {
            val responseStr = client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.d("SendToken", "Token sent successfully: ${response.body?.string()}")
                    response.body?.string()
                } else {
                    Log.e("SendToken", "Failed to send token: ${response.code}")
                    null
                }
            }
            responseStr
        } catch (e: Exception) {
            Log.e("SendToken", "Error during request: ${e.message}")
            null
        }
    }


    // 오류 메시지를 MainActivity로 전달하는 함수
    private fun sendErrorBroadcast(context: Context, errorMessage: String) {
        val intent = Intent("FCM_ERROR")
        intent.putExtra("error_message", errorMessage)
        context.sendBroadcast(intent)
    }
}
