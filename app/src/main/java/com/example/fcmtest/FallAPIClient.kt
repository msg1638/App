package com.example.fcmtest

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private var serverUrl: String = "http://localhost:5000"//서버 URL
    private val client = OkHttpClient()

    // 서버 URL을 설정하는 함수
    fun setServerUrl(url: String): Boolean {
        Log.d("FallAPI","url설정")
        val hasScheme = url.startsWith("http://") || url.startsWith("https://")
        val hasPort = Regex(":[0-9]+").containsMatchIn(url)

        // 도메인 또는 IP 형식 검사 (IPv4, localhost, domain 등 허용)
        val ipRegex = Regex("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")
        val hostnameRegex = Regex("^[a-zA-Z0-9.-]+$") // 예: localhost, example.com

        // 스킴과 포트를 제외한 순수 호스트명 추출
        val base = url
            .removePrefix("http://")
            .removePrefix("https://")
            .substringBefore(":")
            .substringBefore("/")  // 경로가 포함된 경우 대비

        if (!ipRegex.matches(base) && !hostnameRegex.matches(base)) {
            Log.e("ServerUrl", "Invalid URL: $url")
            return false
        }

        val formattedUrl = when {
            hasScheme && hasPort -> url
            hasScheme && !hasPort -> "$url:5000"
            !hasScheme && hasPort -> "http://$url"
            else -> "http://$url:5000"
        }

        serverUrl = formattedUrl
        Log.d("ServerUrl", serverUrl)
        return true
    }

    fun GetVideoUrl(fall_id : String) : String{
        return "$serverUrl/get_fall_video/${fall_id.replace("data","video")}"
    }
    fun GetAnalysisVideoUrl(fall_id : String) : String{
        return "$serverUrl/get_fall_lifting/${fall_id.replace("data","lifting")}"
    }
    fun GetServerUrl(): String {
        return serverUrl
    }
    //서버 상태 체크
    suspend fun checkStatus(): Boolean = suspendCoroutine { cont ->
        Log.d("FallAPI","상태체크")
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
    suspend fun requestAnalyze(fall_id: String): String = withContext(Dispatchers.IO) {
        val analyzeUrl = "$serverUrl/run_analyze"
        val json = JSONObject().put("fall_id", fall_id).toString()
        Log.d("분석 요청", "요청 body: $json")

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(analyzeUrl)
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseCode = response.code
            Log.d("분석 요청", "Response Code: $responseCode")

            response.use { res ->
                val resBody = res.body?.string()
                Log.d("분석 요청", "Response Body: $resBody")
                val jsonResponse = JSONObject(resBody)
                //여기서 이제 done, processing, created, error 중 1개 리턴
                return@withContext jsonResponse.optString("status", "")
            }
        } catch (e: Exception) {
            Log.e("분석 요청", "오류 발생", e)
            return@withContext ""
        }
    }
    suspend fun checkProgress(fall_id: String): Int = withContext(Dispatchers.IO) {
        Log.d("FallAPI","진행률체크")
        val progressUrl = "$serverUrl/progress/$fall_id"
        val request = Request.Builder().url(progressUrl).get().build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext -1
            val json = JSONObject(responseBody)
            return@withContext json.optInt("progress", -1)
        } catch (e: Exception) {
            Log.e("진행률 확인", "오류 발생", e)
            return@withContext -1
        }
    }
    suspend fun getAnalysisResult(fall_id : String) : String = withContext(Dispatchers.IO) {
        Log.d("FallAPI","결과가져오기")
        val resultUrl = "$serverUrl/get_fall_analysis/$fall_id"
        val request = Request.Builder().url(resultUrl).get().build()
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            if (responseBody.isNullOrBlank()) {
                Log.e("getAnalysisResult", "서버 응답이 비어 있습니다.")
                return@withContext ""
            }
            Log.d("결과 리턴",responseBody)
            return@withContext responseBody
        } catch (e: Exception) {
            Log.e("getAnalysisResult", "오류 발생", e)
            return@withContext ""
        }
    }
    //카메라 선택
    suspend fun selectCamera(index: Int) : Boolean = withContext(Dispatchers.IO) {
        val command = when (index) {
            0 -> "AUTOSELECT"
            1 -> "USETHERMAL"
            2 -> "USEWEBCAM"
            else -> {
                Log.e("selectCamera", "잘못된 index: $index")
                return@withContext false
            }
        }

        val url = "$serverUrl/send_command"
        val json = JSONObject().put("command", command).toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                Log.d("selectCamera", "명령 전송 성공: $command")
                Log.d("selectCamera", "응답 내용: $responseBody")
                return@withContext true
            } else {
                Log.e("selectCamera", "명령 전송 실패: $command (코드 $responseCode)")
                Log.e("selectCamera", "오류 응답: $responseBody")
                return@withContext false
            }
        } catch (e: Exception) {
            Log.e("selectCamera", "명령 전송 중 예외 발생", e)
            return@withContext false
        }
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
    fun GetAnalysisResult(fall_id: String) {
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