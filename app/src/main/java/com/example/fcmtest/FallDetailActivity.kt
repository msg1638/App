package com.example.fcmtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

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
                TopBanner.create(this@FallDetailActivity, "낙상 감지 상세 정보", BackButton = true)
                FallDetailScreen(eventId, eventTime, eventStatus, videoUrl)
            }
        }
    }
}

@Composable
fun FallDetailScreen(eventId : String, eventTime: String, status: String, videoUrl: String) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 낙상 정보 텍스트 표시
        //Text(text = "낙상 감지 상세 정보", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "낙상 번호: $eventId")
        Text(text = "발생 시간: $eventTime")
        Text(text = "상태: $status")
        Spacer(modifier = Modifier.height(16.dp))

        // 비디오 플레이어 추가
        Log.d("videoUrl",videoUrl)
        VideoPlayer(videoUrl)

    }
}
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false // 앱 실행 시 자동 재생
        }
    }

    AndroidView(
        factory = { PlayerView(context).apply { this.player = player } },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // 비디오 크기 조정 가능
    )

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
}