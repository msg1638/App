package com.example.fcmtest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
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

    override fun onMessageReceived(remoteMessage: RemoteMessage) { //앱이 포그라운드상태에서도 알림을 받도록 하는 메소드
        Log.d("FCM TEST", "알림 받기 성공")

        // 메시지에 포함된 데이터 처리
        val data: Map<String, String> = remoteMessage.data

        // Notification 타입의 메시지인 경우
        remoteMessage.notification?.let {
            // 알림 내용을 받아서 사용자에게 알림을 표시
            Log.d("알림 내용","타이틀: " + it.title + "\n내용: " + it.body)
            sendNotification(it.title ?: "알림", it.body ?: "내용 없음")
        }
        if (remoteMessage.data.isNotEmpty()) { //낙상 아이디와 타임스탬프가 포함되어있음
            //낙상 기록을 처리하는 로직을 추가해야함.
            sendNotification(
                remoteMessage.data["fall_id"] ?: "데이터 알림",
                remoteMessage.data["timestamp"] ?: "데이터 내용 없음"
            )
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
            .setSmallIcon(android.R.drawable.ic_dialog_info) // 아이콘 설정
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true) // 알림 클릭 시 자동 삭제
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // 클릭 이벤트

        // 안드로이드 8.0(Oreo) 이상에서는 NotificationChannel 필요
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
