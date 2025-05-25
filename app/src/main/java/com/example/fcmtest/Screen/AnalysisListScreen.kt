package com.example.fcmtest.Screen

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fcmtest.R
import com.example.fcmtest.Screen.EventItem.AnalysisEventItem
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.TopBar

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisListScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    val context = LocalContext.current
    //val _fallEvents = model.fallEvents.observeAsState()
    //val fallEvents: List<FallEvent> = _fallEvents.value ?: emptyList()
    // 체크된 항목을 추적하기 위한 상태 변수
    var selectedTab by remember { mutableIntStateOf(0) }
    val items = listOf("전체", "미분석", "분석")
    val coroutineScope = rememberCoroutineScope()
    val events by model.getAnalysisEvents(selectedTab).collectAsState(initial = emptyList())
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            TopBar.StandardTopBar({ navController.popBackStack() }, "분석하기")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, label ->
                    Button(
                        onClick = {
                            if (selectedTab != index) {
                                selectedTab = index
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp) // 버튼 간 간격 추가
                            .defaultMinSize(minWidth = 90.dp, minHeight = 44.dp), // 텍스트 공간 확보
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index) Color(0xFF3629B7) else Color(
                                0xFFF2F1F9
                            ), // 선택된 탭만 진한 색
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins)),
                                color = if (selectedTab == index) Color(0xFFFFFFFF) else Color(
                                    0xFF343434
                                ),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(21.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                events.forEachIndexed { index, event ->
                    item {
                        AnalysisEventItem(
                            event = event,
                            onClick = { selectedEvent ->
                                model.updateStatus(event)
                                model.setDetailEvent(event)
                                model.fetchAnalysisResult(selectedEvent,
                                    onSuccess = {
                                        navController.navigate("AnalysisDetail")
                                    },
                                    onFailure = {
                                        Toast.makeText(
                                            context,
                                            "분석 결과를 가져오지 못했습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                )
                            }
                        )

                    }

                }

            }
        }
        GlobalLoadingScreen(model.analysismodel.progress.collectAsState().value)
    }
}


