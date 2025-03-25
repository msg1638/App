package com.example.fcmtest

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera = findViewById<Button>(R.id.btnCamera)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnConnect = findViewById<Button>(R.id.btnConnect)

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btnConnect.setOnClickListener {
            // 연결 기능 구현
        }

        btnCamera.setOnClickListener {
            // 카메라 보기 기능 구현
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task. isSuccessful) {
                Log.d("Fetching FCM registration token failed", "fcm 토큰얻기 실패")
                return@OnCompleteListener
            }

            val token = task. result

            Log.d("FCMTOKEN", "토큰값:" + token)
            Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_SHORT).show()
        })


    }
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("FCM_MESSAGE")
    }

    fun requestNotificationPermission() { //알림 권한 요청 함수
        if (
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
            )
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 다른 런타임 퍼미션이랑 비슷한 과정
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    // 왜 알림을 허용해야하는지 유저에게 알려주기를 권장
                } else {
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                // 안드로이드 12 이하는 알림에 런타임 퍼미션 없으니, 설정가서 켜보라고 권해볼 수 있겠다.
            }
        }
    }
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isAllow ->
            if (isAllow) {
                // 사용자가 알림 권한 허용
            } else {
                // 사용자가 알림 권한 거부
                // 추가 대처 필요
            }
        }

    override fun onResume() {
        super.onResume()
        requestNotificationPermission()
    }
    override fun onStop() {
        super.onStop()
    }

}


