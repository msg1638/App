package com.example.fcmtest.customUI

import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.fcmtest.R


@Composable
fun FullScreenVideoPlayer(player: ExoPlayer, onExitFullScreen: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    this.useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = onExitFullScreen,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Exit Fullscreen",
                tint = Color.White
            )
        }
    }
}
@Composable
fun VideoPlayer(
    player : ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit
) {
    val context = LocalContext.current
    var playerView: PlayerView? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        onDispose {
            playerView?.player = null
            player.clearVideoSurface() // <-- 여기는 안전한 시점입니다
            player.stop()
            //player.clearMediaItems()
            playerView = null
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
                this.useController = true
                playerView = this

                val fullscreenButton = ImageButton(context).apply {
                    setImageResource(R.drawable.full_screen)
                    setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
                    setBackgroundColor(0x00000000)
                    setOnClickListener { onFullScreenToggle() }
                }

                // 삽입 위치 찾아 버튼 삽입
                val settingsButton = findViewById<View>(androidx.media3.ui.R.id.exo_settings)
                val controlGroup = settingsButton?.parent as? ViewGroup
                controlGroup?.let {
                    val index = it.indexOfChild(settingsButton)
                    it.addView(fullscreenButton, index + 1)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isFullScreen) Modifier.fillMaxHeight() else Modifier.height(250.dp))
    )
}
