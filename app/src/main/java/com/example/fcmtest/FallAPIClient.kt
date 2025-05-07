package com.example.fcmtest

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FallAPIClient {
    private var serverUrl: String = "http://10.0.2.2:5000" //서버 URL
    private val client = OkHttpClient()

    // 서버 URL을 설정하는 함수
    fun setServerUrl(url: String) {
        val formattedUrl = when {
            url.startsWith("http://") -> {
                "$url:5000"
            }

            else -> {
                "http://$url:5000"
            }
        }
        serverUrl = formattedUrl
        Log.d("ServerUrl: ", serverUrl)
    }

    fun GetServerUrl(): String {
        return serverUrl
    }

    suspend fun checkStatus(): Boolean = suspendCoroutine { cont ->
        val url = "$serverUrl/status"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                cont.resume(false)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    cont.resume(it.isSuccessful)
                }
            }
        })
    }

    // 서버로 토큰 전송하는 함수
    fun sendTokenToServer() { //토큰 전달 함수
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Fetching FCM registration token failed", "fcm 토큰얻기 실패")
                return@OnCompleteListener
            }
            val tokenUrl = "$serverUrl/register_token"
            val token = task.result
            Log.d("토큰 값 : ", token)

            // 서버 URL 확인
            Log.d("token Server URL", tokenUrl)

            if (tokenUrl.isEmpty()) {
                Log.e("SendToken", "Server URL is empty!")
                return@OnCompleteListener
            }


            // 요청할 JSON 데이터 준비
            val json = JSONObject().put("token", token).toString()
            Log.d("Request", "Request Body: $json")

            // RequestBody 생성
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(tokenUrl)
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
                    } else {
                        Log.e("SendToken", "Failed to send token: $responseCode")
                    }
                } catch (e: Exception) {
                    Log.e("SendToken", "Error during request: ${e.message}")
                    Log.e("SendToken", "Stack Trace: ", e)  // 예외의 Stack Trace 출력
                }
            }.start()
        })
    }

    //분석 데이터(관절좌표.json)받아오는 곳
    fun GetAnalysisData(fall_id: String) {
        val url = "$serverUrl/analysis/$fall_id"  // 여기에 실제 API 엔드포인트 URL 넣기

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        // 여기서 JSON 파싱 작업 수행
                    }
                } else {
                    Log.d("서버 응답 실패", "${response.code}")
                }
            }
        })
    }

}