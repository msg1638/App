package com.example.fcmtest.Screen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.LoadingState
import com.example.fcmtest.customUI.TopBar
import com.example.fcmtest.customUI.column_chart

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(paddingValues: PaddingValues, model: MainViewModel) {
    val context = LocalContext.current
    var backPressedTime = 0L
    val isLoading = LoadingState.isLoading.collectAsState().value
    BackHandler {
        if(System.currentTimeMillis() - backPressedTime <= 400L) {
            (context as Activity).finish() // 앱 종료
        } else {
            // 특정한 시간 이상으로 차이가 난다면 토스트로 한 번 더 버튼을 누르라고 알림
            Toast.makeText(context, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        // 뒤로가기 버튼을 눌렀던 시간을 저장
        backPressedTime = System.currentTimeMillis()
    }
    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            TopBar.HomeTopBar(
                model.selectedPeriod.value,
                model,
                {
                    model.selectedPeriod.value = it
                    model.getStatsResult(model.selectedPeriod.value)
                }
            )
            if(!isLoading){
                if (model.stat.value.total == 0) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.12f))
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.55f),
                            painter = painterResource(R.drawable.noevent),
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxWidth(0.48f)
                                .fillMaxHeight(0.48f)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE8E8E8),
                                    shape = RoundedCornerShape(size = 24.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "총 발생 건수",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = model.stat.value.total.toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )

                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxWidth(0.48f)
                                .fillMaxHeight(0.48f)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE8E8E8),
                                    shape = RoundedCornerShape(size = 24.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "분석됨",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = model.stat.value.analyzedCount.toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )

                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth(0.48f)
                                .fillMaxHeight(0.48f)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE8E8E8),
                                    shape = RoundedCornerShape(size = 24.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "읽지 않음",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = (model.stat.value.total - model.stat.value.readCount).toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )

                        }
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .fillMaxWidth(0.48f)
                                .fillMaxHeight(0.48f)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFE8E8E8),
                                    shape = RoundedCornerShape(size = 24.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "발생 빈도가 높은 시간",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )
                            Text(
                                text = (model.stat.value.timeRangeCount.max()).toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.space_grotesk)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF1E1E1E),
                                    textAlign = TextAlign.Center,
                                )
                            )
                        }
                    }
                    //여기
                    Box(modifier = Modifier.fillMaxSize().background(Color.White).border(width = 1.dp,color = Color.White, shape = RoundedCornerShape(30.dp))){
                        column_chart(model.stat.value)
                    }
                }
            }
        }
        GlobalLoadingScreen()
    }

}

