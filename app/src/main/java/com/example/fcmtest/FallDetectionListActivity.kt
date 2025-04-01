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
import androidx.compose.foundation.lazy.itemsIndexed
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
                TopBanner.create(this@FallDetectionListActivity, "낙상 감지 목록", BackButton = true)
                FallDetectionListScreen()
            }
        }
    }
}

@Composable
fun FallDetectionListScreen() {
    val context = LocalContext.current
    val fallEvents = listOf(
        FallEvent("fall_1", "2025-03-27 14:30", "읽지 않음"),
        FallEvent("fall_2", "2025-03-27 15:00", "읽음"),
        FallEvent("fall_3", "2025-03-28 09:15", "읽지 않음"),
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn {
            itemsIndexed(fallEvents) { index, event ->
                FallEventItem(index + 1, event) { selectedEvent ->
                    val intent = Intent(context, FallDetailActivity::class.java)
                    intent.putExtra("eventId", selectedEvent.Id)
                    intent.putExtra("eventTime", selectedEvent.time)
                    intent.putExtra("eventStatus", selectedEvent.status)
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun FallEventItem(index : Int, event: FallEvent, onClick: (FallEvent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .padding(vertical = 4.dp)
            .clickable { onClick(event) },
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${"%03d".format(index)}. 낙상 번호: ${event.Id}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "         발생시간: ${event.time}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "         상태: ${event.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class FallEvent(val Id : String, val time: String, val status: String)
