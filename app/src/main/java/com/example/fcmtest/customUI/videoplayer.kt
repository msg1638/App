package com.example.fcmtest.customUI

import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
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
}@Composable
fun VideoPlayer(
    player: ExoPlayer,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        onDispose {
            player.release()  // 화면을 떠날 때 메모리와 surface 모두 해제
        }
    }
    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
                this.useController = true

                val fullscreenButton = ImageButton(context).apply {
                    setImageResource(R.drawable.full_screen) // 적절한 아이콘으로 교체 가능
                    setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
                    setBackgroundColor(0x00000000)
                    setOnClickListener { onFullScreenToggle() }
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        marginStart = 16
                        gravity = Gravity.CENTER_VERTICAL
                    }
                }

                val settingsButton = findViewById<View>(androidx.media3.ui.R.id.exo_settings)
                val controlViewGroup = settingsButton?.parent as? ViewGroup

                if (controlViewGroup != null && settingsButton != null) {
                    val index = controlViewGroup.indexOfChild(settingsButton)
                    controlViewGroup.addView(fullscreenButton, index + 1)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isFullScreen) Modifier.fillMaxHeight() else Modifier.height(250.dp))
    )
}