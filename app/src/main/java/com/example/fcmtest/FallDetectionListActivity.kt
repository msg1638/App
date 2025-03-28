package com.example.fcmtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class FallDetectionListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                Util.TopBanner(this@FallDetectionListActivity, "낙상 감지 목록", BackButton = true)
                FallDetectionListScreen()
            }
        }
    }
}

@Composable
fun FallDetectionListScreen() {
    val context = LocalContext.current
    val fallEvents = listOf(
        FallEvent("2025-03-27 14:30", "거실", "정상 종료"),
        FallEvent("2025-03-27 15:00", "주방", "응급 신고됨"),
        FallEvent("2025-03-28 09:15", "침실", "수동 종료"),
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
            .background(Color.White)
    ) {

        LazyColumn {
            items(fallEvents) { event ->
                FallEventItem(event) { selectedEvent ->
                    val intent = Intent(context, FallDetailActivity::class.java)
                    intent.putExtra("eventTime", selectedEvent.time)
                    intent.putExtra("eventLocation", selectedEvent.location)
                    intent.putExtra("eventStatus", selectedEvent.status)
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun FallEventItem(event: FallEvent, onClick: (FallEvent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(event) },
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "발생 시간: ${event.time}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "위치: ${event.location}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "상태: ${event.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class FallEvent(val time: String, val location: String, val status: String)
