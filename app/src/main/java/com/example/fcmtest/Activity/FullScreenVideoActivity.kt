package com.example.fcmtest.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class FullScreenVideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = intent.getStringExtra("videoUrl")

        setContent {
            videoUrl?.let {
                FullScreenVideoPlayer(it)
            }
        }
    }
}

@Composable
fun FullScreenVideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true // 자동 재생
        }
    }

    AndroidView(
        factory = { PlayerView(context).apply { this.player = player } },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
}
