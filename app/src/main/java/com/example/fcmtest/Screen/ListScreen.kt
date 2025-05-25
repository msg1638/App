package com.example.fcmtest.Screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.R
import com.example.fcmtest.Screen.EventItem.FallEventItem
import com.example.fcmtest.Util.FilterState
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.FilterScreen
import com.example.fcmtest.customUI.TopBar
import com.example.fcmtest.database.FallEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    //val _fallEvents = model.fallEvents.observeAsState()
    //val fallEvents: List<FallEvent> = _fallEvents.value ?: emptyList()
    // 삭제를 위해 체크된 항목을 추적하기 위한 상태 변수
    var isDeleteMode by remember { mutableStateOf(false) }
    val selectedEvents = remember { mutableStateListOf<FallEvent>() }
    var isFilter by remember { mutableStateOf(false) }
    var filterstate by remember { mutableStateOf<FilterState>(FilterState()) }
    val isVisible = model.showMap[ScreenRoute.LIST] == true

    val events by model.getFilteredEvents(filterstate)
        .collectAsState(initial = emptyList())
    if (!isDeleteMode) {
        Log.d("선택초기화", "delete")
        selectedEvents.clear()

    }
    if (isVisible) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopBar.ListTopBar(
                    { navController.popBackStack() },
                    isDeleteMode,
                    { isDeleteMode = it },
                    isFilter,
                    { isFilter = it }
                )


                Spacer(modifier = Modifier.height(21.dp))

                if (!events.isEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        events.forEachIndexed { index, event ->
                            Log.d("일반 이벤트", event.Id)
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(88.dp)
                                        .padding(horizontal = 8.dp)
                                        .clipToBounds(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    val isSelected = selectedEvents.contains(event)
                                    Log.d("선택", "$isSelected")
                                    if (isDeleteMode && isSelected) {
                                        Image(
                                            modifier = Modifier
                                                .padding(0.dp)
                                                .width(37.dp)
                                                .height(38.73441.dp),
                                            painter = painterResource(R.drawable.bin),
                                            contentDescription = "선택됨"
                                        )
                                    }
                                    FallEventItem(
                                        index = index + 1,
                                        event = event,
                                        isSelected = isSelected,
                                        onCheckedChange = { isChecked ->
                                            if (isDeleteMode) {
                                                if (!isChecked) {
                                                    selectedEvents.add(event)
                                                    Log.d("더함", "$index")
                                                } else {
                                                    selectedEvents.remove(event)
                                                    Log.d("뺌", "$index")
                                                }
                                            }
                                        },
                                        onClick = { selectedEvent ->
                                            if (!isDeleteMode) {
                                                model.updateStatus(event)
                                                model.setDetailEvent(event)
                                                navController.navigate("FallDetail")
                                            }
                                        }
                                    )
                                }
                            }

                        }

                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.65f),
                            painter = painterResource(R.drawable.emptylist),
                            contentDescription = "이벤트없음"
                        )
                        Text(
                            text = "감지된 낙상이 없습니다!",
                            style = TextStyle(
                                fontSize = 20.sp,
                                lineHeight = 28.sp,
                                fontFamily = FontFamily(Font(R.font.poppins)),
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                                textAlign = TextAlign.Center,
                            )
                        )

                    }

                }

            }

            if (isDeleteMode) {

                Button(
                    onClick = {
                        model.deleteSelectedEvents(selectedEvents)
                    },
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .width(170.dp)
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(50),
                            ambientColor = Color(0x80686DE0),
                            spotColor = Color(0x80686DE0)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF686DE0), Color(0xFF4834D4))
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(22.5.dp),
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "image description"
                            )
                            Text(
                                text = "지우기",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFFFFFFFF),
                                )
                            )
                        }
                    }
                }
            }
            if (isFilter) {
                FilterScreen(
                    filterstate,
                    { new_state ->
                        filterstate = new_state
                    },
                    { isFilter = false }
                )

            }

        }
    }
}

