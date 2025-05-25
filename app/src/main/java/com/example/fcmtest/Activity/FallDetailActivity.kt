package com.example.fcmtest.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import com.example.fcmtest.FallAPIClient

class FallDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val eventId = intent.getStringExtra("eventId") ?: "정보 없음"
        val eventTime = intent.getStringExtra("eventTime") ?: "정보 없음"
        val eventStatus = intent.getStringExtra("eventStatus") ?: "정보 없음"
        val videoUrl =
            "${FallAPIClient.GetServerUrl()}/video/$eventId"
        //    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        //향후 비디오 url을 서버주소/videos/fall_id로 하면 될듯
        setContent {
            Column {
            }
        }
    }
}

