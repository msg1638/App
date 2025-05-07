package com.example.fcmtest.Util

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.fcmtest.MainActivity
import com.example.fcmtest.database.FallEventRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

//FCM 자동처리 클래스
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) { //최초 앱 실행에만 실행되는 토큰생성함수
        super.onNewToken(token)
        sendTokenToServer(token)    //실패한 경우??
        Log.d("FCM", "New token: $token")
    }
    fun sendTokenToServer(token: String): String? { //토큰 전달 함수
        val serverUrl = "http://10.0.2.2:5000/register_token" // 서버 주소

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
                    val responseBody = response.body?.byteStream()?.bufferedReader(Charsets.UTF_8)?.readText()

                    Log.d("SendToken", "Token sent successfully: $responseBody")
                    responseBody
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM TEST", "Data 메시지 수신 성공")

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            val title = data["title"] ?: "알림"
            val body = data["body"] ?: "내용 없음"
            val fallId = data["fall_id"] ?: "N/A"
            val timestamp = data["timestamp"] ?: "N/A"

            Log.d("FCM 데이터", "title: $title, body: $body, fall_id: $fallId, timestamp: $timestamp")

            // 알림 표시
            sendNotification(title, body)

            // db에 데이터 저장
            CoroutineScope(Dispatchers.IO).launch {
                val repository = FallEventRepository(application)
                val fallevent = getFallEvent(fallId, timestamp, "읽지않음", "미분석")
                repository.addFallEvents(fallevent)

            }
        }
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "default_channel_id"
        val notificationId = System.currentTimeMillis().toInt()

        // 알림 클릭 시 MainActivity로 이동
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 빌더 생성
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info) // 아이콘 설정
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true) // 알림 클릭 시 자동 삭제
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // 클릭 이벤트

        // 안드로이드 8.0(Oreo) 이상에서는 NotificationChannel 필요
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "기본 채널",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 표시
        notificationManager.notify(notificationId, builder.build())
        Log.d("FCM", "알림 전송 완료: $title - $message")
    }

}