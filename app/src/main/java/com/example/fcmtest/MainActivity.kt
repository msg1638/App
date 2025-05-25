@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fcmtest

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fcmtest.Factory.MainViewModelFactory
import com.example.fcmtest.Nav.AppNavGraph
import com.example.fcmtest.Nav.Navigation
import com.example.fcmtest.Nav.NavigationWatcher
import com.example.fcmtest.ViewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private val model: MainViewModel by viewModels {
        MainViewModelFactory(application)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                model.updatePermissionResult(granted)
            }
            SettingsMainScreen(
                navController = navController,
                model = model,
                permissionLauncher
            )
        }
    }
    override fun onStart() {
        super.onStart()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume(){
        super.onResume()
        model.refreshPermissionStatus()
    }
}
@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun SettingsMainScreen(
    navController: NavHostController,
    model : MainViewModel,
    notificationPermissionLauncher: ActivityResultLauncher<String>
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
        // 사용

    Scaffold(
        containerColor = Color.White,
        /*topBar = {
            var selectedPeriod by remember{ mutableStateOf("이번주") }
            TopBanner.HomeTopBar(
                selectedPeriod,
                {selectedPeriod = it}
            )
        },*/
        bottomBar = {
            Navigation(
                navController = navController,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> navController.navigate("main")   // 홈
                        1 -> navController.navigate("list")   // 리스트
                        2 -> navController.navigate("Camera") // 분석
                        3 -> navController.navigate("setting")  // 설정
                    }
                }
            )
        }
    ) { paddingValues ->
        NavigationWatcher(navController, model)
        AppNavGraph(navController, paddingValues, model, notificationPermissionLauncher)

    }
}



