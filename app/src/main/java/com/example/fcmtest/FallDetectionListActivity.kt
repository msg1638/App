package com.example.fcmtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fcmtest.database.FallEvent

class FallDetectionListActivity : ComponentActivity() {
    private val model: FallDetectionListModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                TopBanner.create(this@FallDetectionListActivity, "낙상 감지 목록", BackButton = true)
                FallDetectionListScreen(model)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        model.loadEvents() // 최신 데이터 다시 불러오기
    }
}

@Composable
fun FallDetectionListScreen(model: FallDetectionListModel) {
    val context = LocalContext.current
    val _fallEvents = model.fallEvents.observeAsState()
    val fallEvents: List<FallEvent> = _fallEvents.value ?: emptyList()

    // 체크된 항목을 추적하기 위한 상태 변수
    val selectedEvents = remember { mutableStateListOf<FallEvent>() }

    LaunchedEffect(Unit) {
        model.loadEvents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 삭제 버튼 추가
        Button(
            onClick = {
                model.deleteSelectedEvents(selectedEvents)
                selectedEvents.clear() // 삭제 후 선택 목록 초기화
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            enabled = selectedEvents.isNotEmpty() // 선택된 항목이 있을 때만 활성화
        ) {
            Text("삭제하기")
        }

        LazyColumn {
            itemsIndexed(fallEvents) { index, event ->
                FallEventItem(
                    index = index + 1,
                    event = event,
                    isSelected = selectedEvents.contains(event),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            selectedEvents.add(event)
                        } else {
                            selectedEvents.remove(event)
                        }
                    },
                    onClick = { selectedEvent ->
                        model.updateStatus(index)
                        val intent = Intent(context, FallDetailActivity::class.java)
                        intent.putExtra("eventId", selectedEvent.Id)
                        intent.putExtra("eventTime", selectedEvent.time)
                        intent.putExtra("eventStatus", "읽음")
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun FallEventItem(
    index: Int,
    event: FallEvent,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: (FallEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clickable { onClick(event) },
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 체크박스 추가
            Checkbox(
                checked = isSelected,
                onCheckedChange = { checked -> onCheckedChange(checked) }
            )

            Column {
                Text(
                    text = "${"%03d".format(index)}. 낙상 번호: ${event.Id}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "         발생시간: ${event.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "         상태: ${event.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



