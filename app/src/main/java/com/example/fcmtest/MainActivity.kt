package com.example.fcmtest

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LinkedCamera
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                TopBanner.create(this@MainActivity, "낙상 감지 프로그램")
                MainScreen()
            }
        }

        requestNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Fetching FCM token failed", "FCM 토큰 얻기 실패")
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCMTOKEN", "토큰값: $token")
            Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("FCM_MESSAGE")
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    // 사용자가 허용해야 하는 이유 설명 가능
                } else {
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isAllowed ->
            if (isAllowed) {
                Log.d("Permission", "알림 권한 허용됨")
            } else {
                Log.d("Permission", "알림 권한 거부됨")
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        MenuGrid(
            onSettingsClick = {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
                Toast.makeText(context, "설정 실행", Toast.LENGTH_SHORT).show()
            },
            onConnectClick = {
                Toast.makeText(context, "연결 기능 실행", Toast.LENGTH_SHORT).show()
            },
            onListClick = {
                val intent = Intent(context, FallDetectionListActivity::class.java)
                context.startActivity(intent)
                Toast.makeText(context, "낙상 감지 목록 실행", Toast.LENGTH_SHORT).show()
            }
        )
        BottomBanner()
    }
}



@Composable
fun MenuGrid(onSettingsClick: () -> Unit, onConnectClick: () -> Unit, onListClick: () -> Unit) {
    val menuItems = listOf(
        "설정" to Icons.Default.Settings,
        "연결" to Icons.Default.LinkedCamera,
        "낙상 목록" to Icons.Default.List
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxWidth()
            
    ) {
        items(menuItems.size) { index ->
            val (title, icon) = menuItems[index]

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                    .clickable {
                        when (title) {
                            "설정" -> onSettingsClick()
                            "연결" -> onConnectClick()
                            "낙상 목록" -> onListClick()
                        }
                    },
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = icon, contentDescription = title)
                    Text(title, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun BottomBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Blue)
            Spacer(modifier = Modifier.width(8.dp))
            Text("구현된 기능\n1. 서버 연결\n2. fcm메시지 전송", fontSize = 14.sp)
        }
    }
}
