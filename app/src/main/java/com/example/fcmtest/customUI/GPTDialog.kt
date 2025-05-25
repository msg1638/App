package com.example.fcmtest.customUI

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fcmtest.ChatGptFallResponder

@Composable
fun ChatGptResponseDialog(
    fallType: String,
    fallJoint: String,
    apiKey: String,
    onResult : (String) -> Unit,
    onError : () -> Unit,
    onDismiss: () -> Unit
) {
    var userInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var requestSent by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (result == null && error == null && requestSent) {
        // 요청 전송
        LaunchedEffect(Unit) {
            val responder = ChatGptFallResponder(apiKey)
            responder.getFallResponse(
                fallDirection = fallType,
                additionalInput = userInput,
                hurt_joint = fallJoint,
                onStart = { LoadingState.show() },
                onResult = {
                    LoadingState.hide()
                    result = it
                },
                onError = {
                    LoadingState.hide()
                    error = it
                }
            )
        }
    }

    if (result == null && error == null && !requestSent) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("추가 설명 입력") },
            text = {
                Column {
                    Text("상황에 대한 추가 설명이 있다면 입력해 주세요.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        placeholder = { Text("예: 머리를 부딪혔어요") },
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { requestSent = true }) {
                    Text("보내기")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("취소")
                }
            }
        )
    }

    result?.let {
        onResult(it)
        /*val paragraphs = it.split("\n\n") // 전체 문단 분리
        val situationAnalysis = paragraphs.getOrNull(0) ?: "내용 없음"

        // 응급 대응 파트는 2번째 문단부터
        val emergencyParagraphs = paragraphs.drop(1).joinToString("\n\n")

        // "1. ", "2. ", ... 형태로 시작하는 응급 단계 문단 분리
        val emergencySteps = Regex("""(?=\d+\.)""") // 숫자 + 점 + 공백 패턴으로 나누기
            .split(emergencyParagraphs)
            .map { it.trim() }
            .filter { it.isNotBlank() }
        // 페이지 구성: 첫 페이지는 "상황 분석", 나머지는 응급 대응 단계별
        val pages = listOf("상황 분석" to situationAnalysis) +
                emergencySteps.mapIndexed { index, step ->
                    "응급 단계 ${index + 1}" to step
                }


        val pagerState = rememberPagerState(pageCount = { pages.size })
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        repeat(pages.size) { index ->
                            val color =
                                if (pagerState.currentPage == index) Color.Blue else Color.Gray
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(8.dp)
                                    .background(color, shape = CircleShape)
                            )
                        }
                    }
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = onDismiss
                    ) {
                        Text("확인")
                    }
                }

            },
            title = { Text("상황 분석 및 응급처치") },
            text = {
                Dialog(onDismissRequest = onDismiss) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .heightIn(min = 300.dp, max = 600.dp) // 제한 가능
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "상황 분석 및 응급처치",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f) // 공간 나눔
                            ) { page ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = pages[page].first,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = pages[page].second)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 하단 인디케이터 + 버튼
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(modifier = Modifier.weight(1f)) {
                                    repeat(pages.size) { index ->
                                        val color =
                                            if (pagerState.currentPage == index) Color.Blue else Color.Gray
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(8.dp)
                                                .background(color, shape = CircleShape)
                                        )
                                    }
                                }
                                TextButton(onClick = onDismiss) {
                                    Text("확인")
                                }
                            }
                        }
                    }
                }

            }
        )*/
    }


    error?.let {
        onError()
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("닫기")
                }
            },
            title = { Text("오류 발생") },
            text = { Text(it) }
        )
    }
}
