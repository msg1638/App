package com.example.fcmtest.customUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fcmtest.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LoadingState {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun show() {
        _isLoading.value = true
    }

    fun hide() {
        _isLoading.value = false
    }
}

@Composable
fun GlobalLoadingScreen(text : String = "") {
    val isLoading by LoadingState.isLoading.collectAsState()

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                CircularProgressIndicator()
                if(text != ""){
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        color = Color.White
                    )
                }
            }


        }
    }
}

@Composable
fun GlobalLoadingScreen(progress: Int) {
    val isLoading by LoadingState.isLoading.collectAsState()

    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${progress}%",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    color = Color.White
                )
            }
        }
    }
}


/*
@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))  // 반투명 배경
                .wrapContentSize(Alignment.Center),
        ) {
            Image(
                painter = painterResource(id = R.drawable.loading),
                contentDescription = "Loading",
                modifier = Modifier
                    .size(100.dp)
                    .rotateInfinite() // 커스텀 확장 함수로 회전 효과 추가
            )
        }
    }
}*/