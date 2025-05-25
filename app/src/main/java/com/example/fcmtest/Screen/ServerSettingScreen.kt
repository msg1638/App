package com.example.fcmtest.Screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.fcmtest.FallAPIClient
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.LoadingState
import com.example.fcmtest.customUI.TopBar
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServerSettingScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    Log.d("서버 ip", FallAPIClient.GetServerUrl())
    val context = LocalContext.current
    val serverstatus by model.serverstatus.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val isVisible = model.showMap[ScreenRoute.SERVER_SETTING] == true
    if (isVisible) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                TopBar.StandardTopBar({ navController.popBackStack() }, "서버 설정")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(30))
                        .padding(horizontal = 3.dp)
                        .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(30)),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 "http://"
                    Text(
                        text = "http://",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    // 중간 TextField (입력창)
                    var serverIp by remember { mutableStateOf("") }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        TextField(
                            value = serverIp,
                            onValueChange = { serverIp = it },
                            placeholder = { Text("your server_ip", color = Color.LightGray) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                cursorColor = Color.Gray,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )

                        )
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (!serverIp.isEmpty()) {
                                        if (model.setServerIp(serverIp)) {

                                            Log.d("서버 ip설정", FallAPIClient.GetServerUrl())
                                            LoadingState.show()
                                            model.checkStatus()
                                            FallAPIClient.sendTokenToServer()
                                            LoadingState.hide()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "유효한 ip를 입력해주세요.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                }
                            },
                            modifier = Modifier
                                .width(105.dp)
                                .height(44.dp)
                                .align(Alignment.CenterEnd)
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
                                            colors = listOf(Color(0xFF4834D4), Color(0xFF686DE0))
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "연결하기",
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .offset(y = 19.dp)
                        .padding(horizontal = 6.dp)
                ) {
                    Text(
                        text = "현재 상태:",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.rubik)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF4A4A4A),
                            textAlign = TextAlign.Center,
                        )
                    )
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(180.dp)
                            .height(149.dp),
                        painter = painterResource(id = if (serverstatus) R.drawable.done else R.drawable.emptystate),
                        contentDescription = "연결 상태",
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        text = if (!serverstatus) "서버와의 연결이 끊어졌습니다... " else "서버와의 연결이 양호합니다!",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.rubik)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF4A4A4A),
                            textAlign = TextAlign.Center,
                        )
                    )

                }


            }
            GlobalLoadingScreen()
        }
    }
}