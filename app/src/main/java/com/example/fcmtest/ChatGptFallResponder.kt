package com.example.fcmtest

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ChatGptFallResponder(
    private val apiKey: String
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    private val apiUrl = "https://api.openai.com/v1/chat/completions"
    private val injuryGuidelinesJson = """
{
  "흔한 중증 손상": [
    "대퇴골 골절",
    "척추 압박골절",
    "머리 손상"
  ],
  "손상 세부 정보": {
    "골반뼈와 대퇴골의 골절": {
      "설명": "대퇴골 경부 골절 시 인공관절 수술 가능. 합병증(욕창, 패혈증) 주의.",
      "예상 합병증": ["욕창", "패혈증"]
    },
    "척추 압박골절": {
      "설명": "척추뼈가 으스러져 눌려 앉음. 심한 경우 신경 압박 가능. 노인 여성 골다공증 환자 다수.",
      "증상 단서": ["지속적 근육통", "허리 압통"]
    },
    "머리 손상": {
      "설명": "외상성 뇌출혈 위험.",
      "통계": {
        "외상성 뇌손상(TBI)": 53.6,
        "기타 머리 손상": 22.1
      }
    },
    "기타 부위 손상": {
      "설명": "팔(21.7%), 다리(15.8%), 몸통(9.3%), 엉덩이(8.4%), 척추(5.4%).",
      "흔한 기전": "손으로 땅 짚기 → 손목 골절"
    }
  },
  "골절 처치 원칙": [
    "환자를 함부로 옮기지 말 것",
    "출혈 시 직접 압박 후 드레싱",
    "노출된 뼈를 억지로 밀어넣지 말 것",
    "가능한 움직이지 않게 고정"
  ],
  "부위별 처치 방법": {
    "척추 골절": ["머리 및 목 손으로 고정", "모포로 덮고 의료 지원 대기"],
    "팔 골절": ["팔을 가슴에 고정", "팔–가슴 사이에 부드러운 천 삽입"],
    "골반 골절": ["다리 곧게 눕히기", "무릎 밑 담요 말아 받치기", "쇼크 주의"],
    "발 골절": ["신발·양말 벗기기", "헝겊·부목으로 고정"],
    "쇄골 골절": ["팔을 반대쪽 가슴에 고정", "삼각건으로 지지 후 이송"]
  }
}

    """.trimIndent()

    fun getFallResponse(
        fallDirection: String,
        additionalInput: String = "",
        hurt_joint: String,
        onStart: () -> Unit,
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onStart() // 로딩 시작
        val userPrompt = generatePrompt(fallDirection, hurt_joint, additionalInput)
        val requestBodyJson = JSONObject().apply {
            put("model", "gpt-4-turbo")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put(
                        "content", """
당신은 낙상 응급처치 전문가입니다.  
아래 **119 응급처치 가이드라인**(JSON 형식)과 보호자의 낙상신고를 참고하여 2개의 단락으로 제시해주세요.
첫번째 단락(상황분석) : 비전문 보호자가 이해할 수 있도록 낙상 방향 및 부상 부위로 인한 예상 손상 및 주의점을 간단하게 한글 100자 내외로 설명해주세요.  
두번째 단락(단계별 응급대응) : 비전문 보호자가 즉시 따라할 수 있는 단계별 응급처치 방법을 구체적으로 제시하세요.

$injuryGuidelinesJson

- 응답은 두 개의 단락으로 나뉘며, 각 단락의 제목이나 넘버링 등은 생략하고 바로 내용이 나오도록 해주세요. 아래 형식 참고
1단락의 내용
  
2단락의 내용(번호(예시 : 1. 2. 3.)가 매겨진 단계별 응급처치 방법)  
- 모호한 표현 없이 구체적인 조치와 병원 이송 기준을 포함하세요.
                    """.trimIndent()
                    )
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userPrompt)
                })
            })
        }

        val body = requestBodyJson.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(apiUrl)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ChatGptFallResponder", "API 요청 실패", e)
                onError("네트워크 오류: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ChatGptFallResponder", "응답 코드: ${response.code}")
                Log.d("ChatGptFallResponder", "응답 바디: $responseBody")

                if (response.isSuccessful) {
                    if (responseBody != null) {
                        try {
                            val json = JSONObject(responseBody)
                            val content = json
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                            onResult(content.trim())
                        } catch (e: Exception) {
                            Log.e("ChatGptFallResponder", "응답 파싱 오류", e)
                            onError("응답 파싱 실패: ${e.message}")
                        }
                    } else {
                        onError("응답이 비어 있습니다.")
                    }
                } else {
                    val errorMessage =
                        "API 요청 실패: ${response.code} ${response.message}\n본문: $responseBody"
                    Log.e("ChatGptFallResponder", errorMessage)
                    onError(errorMessage)
                }
            }
        })
    }

    private fun generatePrompt(
        fall_direction: String = "",
        fall_joint: String = "",
        additionalInput: String
    ): String {

        val direction = if (fall_direction.isBlank()) "알 수 없음" else fall_direction
        return """
            넘어진 방향: $direction.
            손상 부위(관절)들 : $fall_joint.
            추가 사항: $additionalInput.
        """.trimIndent()
    }


}
