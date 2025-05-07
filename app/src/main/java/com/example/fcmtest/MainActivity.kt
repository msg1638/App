@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fcmtest

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fcmtest.Factory.MainViewModelFactory
import com.example.fcmtest.Nav.AppNavGraph
import com.example.fcmtest.Nav.Navigation
import com.example.fcmtest.Util.ScreenState
import com.example.fcmtest.ViewModel.MainViewModel

class MainActivity : ComponentActivity() {
    var state = ScreenState.MAIN

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val context = LocalContext.current
            val model: MainViewModel = viewModel(
                factory = MainViewModelFactory(context.applicationContext as Application)
            )
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                model.updatePermissionResult(granted)
            }
            LaunchedEffect(Unit) {
                model.loadEvents()
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
    override fun onResume(){
        super.onResume()
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
    var selectedIndex by remember { mutableStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
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
                        2 -> navController.navigate("analysis") // 분석
                        3 -> navController.navigate("setting")  // 설정
                    }
                }
            )
        }
    ) { paddingValues ->
        AppNavGraph(navController, paddingValues, model, notificationPermissionLauncher)

    }
}



