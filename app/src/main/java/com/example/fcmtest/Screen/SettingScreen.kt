package com.example.fcmtest.Screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel
import com.example.fcmtest.customUI.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingScreen(navController: NavController, paddingValues: PaddingValues, model : MainViewModel
,notificationPermissionLauncher: ActivityResultLauncher<String>
) {
    val context = LocalContext.current
    //var NF = NotificationPermission(context)
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(31.28.dp)
    )
    {
        item {
            TopBar.StandardTopBar({ navController.popBackStack() }, "설정")
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = "기본",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFADADAD)
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "서버 설정",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.rubik)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.next_button),
                        modifier = Modifier
                            .padding(0.dp)
                            .width(37.dp)
                            .height(31.dp)
                            .clickable {
                                navController.navigate("server_setting")
                                //동작 추가
                            },
                        contentDescription = "서버 설정 버튼"
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .height(36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "푸시 알림",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.rubik)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000)
                        )
                    )
                    val isGranted by model.isGranted.collectAsState()
                    var isChecked by remember { mutableStateOf(isGranted) }
                    LaunchedEffect(isGranted) {
                        isChecked = isGranted
                    }
                    Switch(
                        modifier = Modifier
                            .padding(0.dp)
                            .width(56.dp)
                            .height(27.32.dp),
                        checked = isChecked,
                        onCheckedChange = { checked->
                            Log.d("checked","${checked}")
                            Log.d("isChecked","${isChecked}")
                            Log.d("isGranted","${isGranted}")
                            model.refreshPermissionStatus()


                            if(checked && !isGranted){
                                Log.d("sdf","gi")
                                if(!ActivityCompat.shouldShowRequestPermissionRationale(
                                        context as Activity,
                                        android.Manifest.permission.POST_NOTIFICATIONS)
                                    ){
                                    Toast.makeText(context,
                                        "알림은 설정 앱에서 활성화할 수 있습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent().apply {
                                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                    context.startActivity(intent)

                                }
                                else{
                                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            else if(!checked && isGranted){
                                Toast.makeText(context,
                                    "알림은 설정 앱에서 비활성화할 수 있습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent().apply {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                context.startActivity(intent)
                            }

                        }
                    )
                }
            }
        }
    }
}
