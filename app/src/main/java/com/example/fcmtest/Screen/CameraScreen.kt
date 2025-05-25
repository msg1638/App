package com.example.fcmtest.Screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fcmtest.Nav.ScreenRoute
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.GlobalLoadingScreen
import com.example.fcmtest.customUI.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    model: MainViewModel
) {
    val context = LocalContext.current
    val isVisible = model.showMap[ScreenRoute.CAMERA] == true
    if (isVisible) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.padding(paddingValues).fillMaxWidth().fillMaxHeight()){
                TopBar.StandardTopBar({navController.popBackStack()},"카메라 설정")
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.padding(12.dp).fillMaxWidth().fillMaxHeight(0.8f)){
                    Button(
                        modifier = Modifier.fillMaxWidth(0.85f).align(Alignment.TopCenter).fillMaxHeight(0.17f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.sea),          // 버튼 배경색
                            contentColor = Color.White            // 텍스트(또는 아이콘) 색상
                        ),
                        onClick = {
                            model.selectCamera(0, {Toast.makeText(context,"카메라 전환 성공!",Toast.LENGTH_SHORT).show()},
                                {Toast.makeText(context,"다시 시도해주세요.",Toast.LENGTH_SHORT).show()})
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.reload),
                                modifier = Modifier.size(32.dp),
                                contentDescription = "auto"
                            )
                            Text("AUTO",fontSize = 22.sp)
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(0.85f).align(Alignment.Center).fillMaxHeight(0.17f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.sea),          // 버튼 배경색
                            contentColor = Color.White            // 텍스트(또는 아이콘) 색상
                        ),
                        onClick = {
                            model.selectCamera(1, {Toast.makeText(context,"카메라 전환 성공!",Toast.LENGTH_SHORT).show()},
                                {Toast.makeText(context,"다시 시도해주세요.",Toast.LENGTH_SHORT).show()})
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.thermal),
                                modifier = Modifier.size(36.dp),
                                contentDescription = "thermal"
                            )
                            Text("열화상 카메라",fontSize = 22.sp)
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(0.85f).align(Alignment.BottomCenter).fillMaxHeight(0.17f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.sea),          // 버튼 배경색
                            contentColor = Color.White            // 텍스트(또는 아이콘) 색상
                        ),
                        onClick = {
                            model.selectCamera(2, {Toast.makeText(context,"카메라 전환 성공!",Toast.LENGTH_SHORT).show()},
                                {Toast.makeText(context,"다시 시도해주세요.",Toast.LENGTH_SHORT).show()})
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.cctv_camera),
                                modifier = Modifier.size(40.dp),
                                contentDescription = "cctv"
                            )
                            Text("CCTV",fontSize = 24.sp)
                        }

                    }
                }

            }
            GlobalLoadingScreen("카메라 설정 중...")


        }


    }
}
