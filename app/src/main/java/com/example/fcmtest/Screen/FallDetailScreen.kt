package com.example.fcmtest.Screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.example.fcmtest.FallAPIClient
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.FullScreenVideoPlayer
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.TopBar
import com.example.fcmtest.customUI.VideoPlayer


@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FallDetailScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    val context = LocalContext.current
    val event = model.getDetailEvent()
    val videoUrl = FallAPIClient.GetVideoUrl(event.Id)
    var isFullScreen by remember { mutableStateOf(false) }
    val viewModel = model.detailmodel.videomodel
    var isAnalyzed by remember { mutableStateOf(event.analysis) }
    val isVisible = model.showMap[ScreenRoute.FALL_DETAIL] == true
    Log.d("아이디", event.Id)
    LaunchedEffect(Unit) {
        viewModel.preparePlayer(videoUrl, forceReload = true)
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.d("FallDetailScreen", "Disposing ExoPlayer")
            viewModel.releasePlayer()// <- 이 함수에서 player?.release() 호출되도록 구현돼야 함
        }
    }
    if (isVisible) {
        if (isFullScreen) {
            FullScreenVideoPlayer(
                player = viewModel.player,
                onExitFullScreen = { isFullScreen = false }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    TopBar.StandardTopBar(
                        {
                            navController.popBackStack()
                        },
                        "자세히 보기"
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        modifier = Modifier
                            .padding(17.dp),
                        text = "클립 보기",
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
                            text = "분석 결과 보기",
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.poppins)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF979797)
                            )
                        )
                        Box(
                        ) {
                            Button(
                                onClick = {
                                    if (event.analysis == "미분석") {
                                        model.fetchAnalysisResult(
                                            event,
                                            onSuccess = {
                                                model.updateAnalysis(event)
                                                event.analysis = "분석"
                                                isAnalyzed = event.analysis
                                                navController.navigate("AnalysisDetail")
                                            },
                                            onFailure = {
                                                Toast.makeText(
                                                    context,
                                                    "분석 결과를 가져오지 못했습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    } else {

                                        navController.navigate("AnalysisDetail")
                                        model.getAnalysisResult(
                                            event,
                                            onFailure = {
                                                Toast.makeText(
                                                    context,
                                                    "분석 결과를 가져오지 못했습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            })
                                    }
                                },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(44.dp)
                                    .align(Alignment.Center)
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
                                                colors = listOf(
                                                    Color(0xFF4834D4),
                                                    Color(0xFF686DE0)
                                                )
                                            ),
                                            shape = RoundedCornerShape(50)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = if (isAnalyzed == "미분석") "분석하기" else "결과보기",
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily(Font(R.font.rubik)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFFFFFFFF),
                                            textAlign = TextAlign.Center,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                GlobalLoadingScreen()
            }
        }
    }
}