package com.example.fcmtest

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = intent.getStringExtra("videoUrl")

        setContent {
            if (videoUrl != null) {
                VideoPlayerScreen(videoUrl)
            } else {
                Toast.makeText(this, "영상 URL을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

@Composable
fun VideoPlayerScreen(videoUrl: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(videoUrl)
            }
        }, modifier = Modifier.fillMaxSize())
    }
}
