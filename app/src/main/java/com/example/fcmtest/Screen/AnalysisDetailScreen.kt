package com.example.fcmtest.Screen


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fcmtest.BuildConfig
import com.example.fcmtest.FallAPIClient
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.R
import com.example.fcmtest.Util.AIScript
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.ChatGptResponseDialog
import com.example.fcmtest.customUI.FullScreenVideoPlayer
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.TopBar
import com.example.fcmtest.customUI.VideoPlayer


@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisDetailScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    val context = LocalContext.current
    val event = model.getDetailEvent()
    val videoUrl = FallAPIClient.GetAnalysisVideoUrl(event.Id)
    val viewModel = model.analysismodel.videomodel
    var isFullScreen by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedScript by remember { mutableStateOf(0) }
    var script by remember { mutableStateOf<String?>(null) }
    var gpt by remember { mutableStateOf(false) }
    val items = listOf("분석 결과", "AI 응급처치 메뉴얼")
    val isVisible = model.showMap[ScreenRoute.ANALYSIS_DETAIL] == true
    var pages by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    val pagerState1 = rememberPagerState(pageCount = { 1 })
    val pagerState2 = rememberPagerState(pageCount = { pages.size - 1 })
    val Joint = model.analysismodel.getFallJoint()
    val injuryJoints: List<String>
    if (Joint.isBlank()) injuryJoints = listOf("-") else injuryJoints =
        Joint.split(",")
    Log.d("관절", injuryJoints[0])
    val jointState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.preparePlayer(videoUrl, forceReload = true)
    }
    LaunchedEffect(script) {
        if (!script.isNullOrBlank()) {
            pages = AIScript(script!!).getScript()
        }
    }
    if (isVisible) {
        if (isFullScreen) {
            FullScreenVideoPlayer(
                player = viewModel.player,
                onExitFullScreen = { isFullScreen = false }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    TopBar.StandardTopBar(
                        {
                            navController.popBackStack()
                        },
                        "분석 결과"
                    )


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
                                    .defaultMinSize(
                                        minWidth = 90.dp,
                                        minHeight = 44.dp
                                    ), // 텍스트 공간 확보
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
                    Spacer(modifier = Modifier.height(12.dp))
                    if (selectedTab == 0) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 30.dp,
                                            spotColor = Color(0x123629B7),
                                            ambientColor = Color(0x123629B7)
                                        )
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .border(
                                            width = 1.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(30.dp)
                                        )
                                ) {
                                    Column() {
                                        Text(
                                            modifier = Modifier
                                                .padding(17.dp),
                                            text = "3d 클립 보기",
                                            // Title / 3
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp,
                                                fontFamily = FontFamily(Font(R.font.poppins)),
                                                fontWeight = FontWeight(600),
                                                color = Color(0xFF343434),
                                            )
                                        )
                                        VideoPlayer(
                                            player = viewModel.player,
                                            isFullScreen = false,
                                            onFullScreenToggle = { isFullScreen = true }
                                        )
                                        Spacer(modifier = Modifier.height(22.dp))
                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = 14.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "날짜",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF979797)
                                                )
                                            )
                                            Text(
                                                text = "${event.year}/${event.month}/${event.day}",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(600),
                                                    color = Color(0xFF3629B7)
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(18.dp))
                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = 14.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "발생 시각",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF979797)
                                                )
                                            )
                                            val formattedTime = String.format(
                                                "%s %d:%02d",
                                                if (event.hour < 12) "오전" else "오후",
                                                if (event.hour == 0 || event.hour == 12) 12 else event.hour % 12,
                                                event.minute
                                            )
                                            Text(
                                                text = formattedTime,
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(600),
                                                    color = Color(0xFF3629B7)
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(18.dp))
                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = 14.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "낙상 종류",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF979797)
                                                )
                                            )
                                            Text(
                                                text = if (model.analysismodel.getFallType()
                                                        .isBlank()
                                                ) "-"
                                                else "${model.analysismodel.getFallType()} 낙상",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF979797)
                                                )
                                            )

                                        }
                                        Spacer(modifier = Modifier.height(18.dp))

                                        var isExpanded by remember { mutableStateOf(false) }

                                        val scrollProgress by remember {
                                            derivedStateOf {
                                                val layoutInfo = jointState.layoutInfo
                                                val visibleItems = layoutInfo.visibleItemsInfo

                                                if (visibleItems.isNotEmpty()) {
                                                    val firstVisibleItem = visibleItems.first()
                                                    val firstIndex =
                                                        jointState.firstVisibleItemIndex
                                                    val firstItemScrollOffset =
                                                        jointState.firstVisibleItemScrollOffset

                                                    // 전체 길이 = 모든 아이템 수 * 평균 아이템 너비 (대략 추정)
                                                    val averageItemSize =
                                                        visibleItems.map { it.size }.average()
                                                            .toFloat()
                                                    val totalContentWidth =
                                                        layoutInfo.totalItemsCount * averageItemSize

                                                    // 뷰포트 길이 = 현재 화면에 보여지는 너비
                                                    val viewportWidth = layoutInfo.viewportEndOffset

                                                    val totalScrollableWidth =
                                                        totalContentWidth - viewportWidth

                                                    // 현재까지 스크롤한 거리 = (index * 평균 너비 + 오프셋)
                                                    val scrolledX =
                                                        firstIndex * averageItemSize + firstItemScrollOffset

                                                    if (totalScrollableWidth > 0) {
                                                        (scrolledX / totalScrollableWidth).coerceIn(
                                                            0f,
                                                            1f
                                                        )
                                                    } else {
                                                        0f
                                                    }
                                                } else {
                                                    0f
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .padding(horizontal = 14.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(0.6f),
                                                text = "예상 손상 부위",
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                                    fontWeight = FontWeight(500),
                                                    color = Color(0xFF979797)
                                                )
                                            )
                                            IconButton(
                                                onClick = {
                                                    isExpanded = !isExpanded
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                    contentDescription = "펼쳐보기"
                                                )
                                            }
                                            Column(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                if (isExpanded) {
                                                    injuryJoints.forEach {
                                                        Text(
                                                            text = it,
                                                            style = TextStyle(
                                                                fontSize = 18.sp,
                                                                lineHeight = 24.sp,
                                                                fontFamily = FontFamily(
                                                                    Font(
                                                                        R.font.poppins
                                                                    )
                                                                ),
                                                                fontWeight = FontWeight(500),
                                                                color = Color(0xFF979797)
                                                            )
                                                        )

                                                    }
                                                } else {
                                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                                                        LazyRow(
                                                            state = jointState
                                                        ) {
                                                            items(injuryJoints.size) { index ->
                                                                Text(
                                                                    text = if (index == injuryJoints.lastIndex) injuryJoints[index] else "${injuryJoints[index]}, ",
                                                                    style = TextStyle(
                                                                        fontSize = 18.sp,
                                                                        lineHeight = 24.sp,
                                                                        fontFamily = FontFamily(
                                                                            Font(
                                                                                R.font.poppins
                                                                            )
                                                                        ),
                                                                        fontWeight = FontWeight(500),
                                                                        color = Color(0xFF979797)
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                    LinearProgressIndicator(
                                                        progress = scrollProgress.coerceIn(
                                                            0f,
                                                            1f
                                                        ),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(4.dp),
                                                        color = Color.Blue,
                                                        backgroundColor = Color.LightGray
                                                    )
                                                }

                                            }
                                        }



                                        Spacer(modifier = Modifier.height(18.dp))

                                    }
                                }
                            }
                        }
                    } else {
                        if (pages.isEmpty()) {
                            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    modifier = Modifier.scale(2.5f),
                                    painter = painterResource(R.drawable.ambulance),
                                    contentDescription = "ai스크립트 없음"
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "아래 버튼을 눌러 AI 분석 결과를 확인해보세요.",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        lineHeight = 28.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins)),
                                        fontWeight = FontWeight(600),
                                        color = Color(0xFF000000),
                                        textAlign = TextAlign.Center,
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = { gpt = !gpt },
                                        modifier = Modifier
                                            .width(240.dp)
                                            .height(66.dp)
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
                                        shape = RoundedCornerShape(50)

                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Color(0xFF4834D4),
                                                            Color(0xFF686DE0)
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(50)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("AI분석하기")
                                        }
                                    }
                                }

                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.8f)
                                    .padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        Modifier
                                            .align(Alignment.TopStart)
                                    ) {
                                        BottomTabItem(
                                            "상황 분석",
                                            0,
                                            selected = selectedScript == 0
                                        ) { selectedScript = it }
                                    }
                                    Row(
                                        Modifier
                                            .align(Alignment.TopEnd)
                                    ) {
                                        BottomTabItem(
                                            "응급 처치",
                                            1,
                                            selected = selectedScript == 1
                                        ) { selectedScript = it }
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(
                                            RoundedCornerShape(
                                                bottomStart = 16.dp,
                                                bottomEnd = 16.dp
                                            )
                                        )
                                        .background(Color(0xFFFFFFCC))
                                ) {
                                    var pagerState: PagerState
                                    if (selectedScript == 0) pagerState = pagerState1
                                    else pagerState = pagerState2
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f) // 공간 나눔
                                    ) { page ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = if (selectedScript == 0) pages[page].first
                                                else pages[page + 1].first,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = if (selectedScript == 0) pages[page].second
                                                else pages[page + 1].second,
                                                fontSize = 18.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // 하단 인디케이터 + 버튼
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        repeat(pagerState.pageCount) { index ->
                                            val color =
                                                if (pagerState.currentPage == index) Color.Blue else Color.Gray
                                            Box(
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .size(8.dp)
                                                    .background(color, shape = CircleShape)
                                            )
                                        }

                                    }
                                }
                            }
                        }

                    }

                }
                GlobalLoadingScreen("결과 가져오는 중...")
                if (gpt) {
                    ChatGptResponseDialog(
                        model.analysismodel.getFallType(),
                        model.analysismodel.getFallJoint(),
                        BuildConfig.gpt_key,
                        { script = it },
                        {
                            Toast.makeText(context, "스크립트를 가져오는 것에 실패했습니다.", Toast.LENGTH_SHORT)
                                .show()
                        },
                        { gpt = !gpt }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomTabItem(text: String, index: Int, selected: Boolean, onClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(44.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(if (selected) Color(0xFFFFFFCC) else Color(0xFFEDEDED))
            .clickable { onClick(index) }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.TopCenter),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = Color.Black
        )
    }
}