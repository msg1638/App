package com.example.fcmtest

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FCMTokenManager(private val context: Context) {

    private var serverUrl: String = "" //서버 URL

    // 서버 URL을 설정하는 함수
    fun setServerUrl(url: String) {
        val formattedUrl = when {
            url.startsWith("http://") -> {
                "$url:5000/register_token"
            }
            else -> {
                "http://$url:5000/register_token"
            }
        }
        serverUrl = formattedUrl
        Log.d("ServerUrl: ", serverUrl)
    }


    // 서버로 토큰 전송하는 함수
    fun sendTokenToServer() { //토큰 전달 함수
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Fetching FCM registration token failed", "fcm 토큰얻기 실패")
                showToast("FCM 토큰 얻기 실패")
                return@OnCompleteListener
            }

            val token = task.result
            Log.d("토큰 값 : ", token)

            // 서버 URL 확인
            Log.d("Server URL", serverUrl)

            if (serverUrl.isEmpty()) {
                Log.e("SendToken", "Server URL is empty!")
                return@OnCompleteListener
            }

            val client = OkHttpClient()

            // 요청할 JSON 데이터 준비
            val json = JSONObject().put("token", token).toString()
            Log.d("Request", "Request Body: $json")

            // RequestBody 생성
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(serverUrl)
                .post(body)
                .build()

            // 비동기 작업으로 네트워크 요청 실행
            Thread {
                try {
                    val response = client.newCall(request).execute()
                    val responseCode = response.code
                    Log.d("Response Code", "Response Code: $responseCode")

                    if (response.isSuccessful) {
                        val responseBody = response.body?.string() ?: "No response body"
                        Log.d("SendToken", "Token sent successfully: $responseBody")
                        showToast("토큰 전송 성공")
                    } else {
                        Log.e("SendToken", "Failed to send token: $responseCode")
                        showToast("토큰 전송 실패")
                    }
                } catch (e: Exception) {
                    Log.e("SendToken", "Error during request: ${e.message}")
                    Log.e("SendToken", "Stack Trace: ", e)  // 예외의 Stack Trace 출력
                    showToast("토큰 전송 중 에러 발생")
                }
            }.start()
        })
    }
    private fun showToast(message: String) {
        // UI 스레드에서 토스트 메시지를 표시
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


}
