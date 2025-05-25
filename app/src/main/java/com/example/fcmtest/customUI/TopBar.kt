package com.example.fcmtest.customUI

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fcmtest.R
import com.example.fcmtest.ViewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
object TopBar {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun HomeTopBar(
        selectedPeriod: String,
        model: MainViewModel,
        onPeriodSelected: (String) -> Unit
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp)
        ) {

            Text(
                text = "홈",
                modifier = Modifier.Companion
                    .padding(start = 24.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF343434),
                )
            )

            PeriodDropdownMenu(
                selectedOption = selectedPeriod,
                model = model,
                onOptionSelected = onPeriodSelected
            )
        }
    }

    @Composable
    fun ListTopBar(
        backClick: () -> Unit,
        selectedDelete: Boolean,   //휴지통을 열었는지 저장하는 변수
        clickDelete: (Boolean) -> Unit,    //위 변수에 값 전달하기 위한 함수
        selectedFilter : Boolean,
        clickFilter : (Boolean) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp)
        ) {
            IconButton(onClick = { backClick() }) {
                Icon(
                    modifier = Modifier.Companion
                        .padding(1.dp)
                        .width(16.dp)
                        .height(16.dp),
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "뒤로가기"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "리스트",
                    modifier = Modifier.Companion
                        .padding(end = 8.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF343434),
                    )
                )
                var DeleteCheck by remember { mutableStateOf(selectedDelete) }
                var FilterCheck by remember { mutableStateOf(selectedFilter) }
                Row(

                ){
                    IconButton(
                        onClick = {
                            FilterCheck = true
                            clickFilter(FilterCheck)
                            //필터
                        }
                    ) {
                        Icon(
                            modifier = Modifier.Companion
                                    .padding(0.dp)
                                    .width(22.5.dp)
                                    .height(20.dp),
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "필터"
                        )
                    }
                    IconButton(
                        onClick = {
                            DeleteCheck = !DeleteCheck
                            clickDelete(DeleteCheck)
                        }
                    ) {
                        Icon(
                            modifier = if (DeleteCheck)
                                Modifier.Companion
                                    .padding(0.dp)
                                    .width(22.5.dp)
                                    .height(29.35547.dp)
                                    .offset(x = -2.68.dp, y = -4.677735.dp)
                            else
                                Modifier.Companion
                                    .padding(0.dp)
                                    .width(22.5.dp)
                                    .height(20.dp),
                            painter = if (DeleteCheck)
                                painterResource(id = R.drawable.delete2)
                            else painterResource(id = R.drawable.delete),
                            contentDescription = "지우기"
                        )
                    }
                }


            }


        }
    }
    @Composable
    fun StandardTopBar(backClick: () -> Unit, title: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp)
        ) {
            IconButton(onClick = { backClick() }) {
                Icon(
                    modifier = Modifier.Companion
                        .padding(1.dp)
                        .width(16.dp)
                        .height(16.dp),
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "뒤로가기"
                )
            }
            Text(
                text = title,
                modifier = Modifier.Companion
                    .padding(end = 8.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF343434),
                )
            )
        }
    }
}
/*

    @Composable
    fun SettingsTopBar() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp)
        ) {
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.Companion
                        .padding(1.dp)
                        .width(16.dp)
                        .height(16.dp),
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "뒤로가기"
                )
            }
            Text(
                text = "설정",
                modifier = Modifier.Companion
                    .padding(end = 8.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF343434),
                )
            )
        }
    }
}*/
