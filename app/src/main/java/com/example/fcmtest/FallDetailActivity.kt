package com.example.fcmtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.jvm.java

class FallDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventTime = intent.getStringExtra("eventTime") ?: "정보 없음"
        val eventLocation = intent.getStringExtra("eventLocation") ?: "정보 없음"
        val eventStatus = intent.getStringExtra("eventStatus") ?: "정보 없음"

        setContent {
            FallDetailScreen(eventTime, eventLocation, eventStatus) {
                // 영상 보기 버튼 클릭 시 서버에 요청
                val videoUrl = "https://example.com/videos/${eventTime.replace(" ", "_")}.mp4"
                val intent = Intent(this, VideoPlayerActivity::class.java)
                intent.putExtra("videoUrl", videoUrl)
                startActivity(intent)
            }
        }
    }
}

@Composable
fun FallDetailScreen(eventTime: String, location: String, status: String, onWatchVideo: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "낙상 감지 상세 정보", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "발생 시간: $eventTime")
        Text(text = "위치: $location")
        Text(text = "상태: $status")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onWatchVideo) {
            Text("영상 보기")
        }
    }
}
