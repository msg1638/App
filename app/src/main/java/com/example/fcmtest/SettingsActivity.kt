package com.example.fcmtest

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var fcmTokenManager: FCMTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fcmTokenManager = FCMTokenManager(this)
        val ServerUrl = findViewById<EditText>(R.id.etIpAddress)
        val SendToken = findViewById<Button>(R.id.btnSendToken)

        
        SendToken.setOnClickListener {
            fcmTokenManager.setServerUrl(ServerUrl.text.toString())
            fcmTokenManager.sendTokenToServer()
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)  // 툴바를 액션바로 설정

        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // 뒤로가기 버튼 활성화
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼 클릭 시
                finish() // 현재 액티비티 종료 (이전 액티비티로 이동)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

