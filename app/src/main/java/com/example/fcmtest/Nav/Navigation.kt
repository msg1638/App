package com.example.fcmtest.Nav

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fcmtest.R

@Composable
fun Navigation(
    navController: NavController,
    onItemSelected: (Int) -> Unit //클릭 시 할 행동
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Row(
        Modifier
            .shadow(
                elevation = 5.dp,
                spotColor = Color(0x33000000),
                ambientColor = Color(0x33000000)
            )
            .shadow(
                elevation = 14.dp,
                spotColor = Color(0x1F000000),
                ambientColor = Color(0x1F000000)
            )
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x24000000),
                ambientColor = Color(0x24000000)
            )
            .padding(0.dp)
            .fillMaxWidth()
            .height(64.dp)
            .background(color = Color(0xFF6200EE)),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
    ) {
        val items = listOf("홈", "리스트", "분석", "설정")
        val icons =
            listOf(R.drawable.home, R.drawable.list, R.drawable.analysis, R.drawable.setting)
        val routes = listOf(
            "main",
            "list",
            "analysis",
            "setting",
            "FallDetail",
            "AnalysisDetail",
            "server_setting"
        )
        val currentIndex = routes.indexOf(currentRoute).takeIf { it >= 0 }?.let {
            Log.d("현재 커런트 인덱스","$it")
            if(it > 3) (it - 3) else it
        } ?:0

        items.forEachIndexed { index, label ->
            val isClicked = index == currentIndex

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onItemSelected(index)
                    }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(24.dp)
                        .height(24.dp),
                    painter = painterResource(icons[index]),
                    contentDescription = label,
                    tint = if (isClicked) colorResource(R.color.sea) else Color.White
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (isClicked) Color.White else Color.LightGray
                )
            }
        }
    }
}