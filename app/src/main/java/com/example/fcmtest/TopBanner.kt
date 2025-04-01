package com.example.fcmtest

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object TopBanner {
    @Composable
    fun create(activity: Activity?, title: String, BackButton : Boolean = false) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(Color.Yellow)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (BackButton) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { activity?.finish() } // 현재 액티비티 종료
                )
                Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 간격
            }
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}