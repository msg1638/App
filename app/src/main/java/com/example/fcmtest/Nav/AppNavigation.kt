package com.example.fcmtest.Nav

import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fcmtest.Screen.AnalysisDetailScreen
import com.example.fcmtest.Screen.AnalysisListScreen
import com.example.fcmtest.Screen.FallDetailScreen
import com.example.fcmtest.Screen.ListScreen
import com.example.fcmtest.Screen.MainScreen
import com.example.fcmtest.Screen.ServerSettingScreen
import com.example.fcmtest.Screen.SettingScreen
import com.example.fcmtest.ViewModel.MainViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    model: MainViewModel,
    notificationPermissionLauncher: ActivityResultLauncher<String>
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(paddingValues, model)
        }
        composable("list") {
            ListScreen(navController, paddingValues, model)
        }
        composable("analysis") {
            AnalysisListScreen(navController, paddingValues, model)
        }
        composable("setting") {
            SettingScreen(navController, paddingValues, model, notificationPermissionLauncher)
        }
        composable("server_setting"){
            ServerSettingScreen(navController, paddingValues, model)
        }
        composable("FallDetail"){
            FallDetailScreen(navController, paddingValues, model)
        }
        composable("AnalysisDetail"){
            AnalysisDetailScreen(navController, paddingValues, model)

        }
    }
}