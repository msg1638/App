package com.example.fcmtest.customUI

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fcmtest.Util.FilterState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@Composable
fun FilterScreen(
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onDismissRequest: () -> Unit
) {
    val readOptions = listOf("전체", "읽지 않음", "읽음")
    val analysisOptions = listOf("전체", "미분석", "분석")
    val dateOptions = listOf("전체", "범위 선택")

    // 날짜 상태


    // 다이얼로그 상태
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val formatDate: (Long?) -> String = { millis ->
        millis?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
        } ?: ""
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
            ) {
                // 상단 타이틀과 닫기 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "필터",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 분석 필터
                FilterSection(
                    title = "분석",
                    options = analysisOptions,
                    selectedIndex = filterState.selectedAnalysis,
                    onOptionSelected = {
                        onFilterChange(filterState.copy(selectedAnalysis = it))
                    }
                ) {}

                Spacer(modifier = Modifier.height(24.dp))

                // 읽음 필터
                FilterSection(
                    title = "읽음",
                    options = readOptions,
                    selectedIndex = filterState.selectedRead,
                    onOptionSelected = {
                        //selectedRead = it
                        onFilterChange(filterState.copy(selectedRead = it))
                    }
                ) {}

                Spacer(modifier = Modifier.height(24.dp))

                // 날짜 필터 + 추가 UI
                FilterSection(
                    title = "날짜 범위",
                    options = dateOptions,
                    selectedIndex = filterState.selectedDate,
                    onOptionSelected = {
                        onFilterChange(filterState.copy(selectedDate = it))

                    }
                ) {
                    if (filterState.selectedDate == 1) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = formatDate(filterState.startDate),
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable{ showStartDatePicker = true },
                                trailingIcon = {
                                    IconButton(onClick = { showStartDatePicker = true }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                label = { Text("시작 날짜") },
                                readOnly = true,
                                singleLine = true,
                                enabled = true
                            )
                            OutlinedTextField(
                                value = formatDate(filterState.endDate),
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable{ showEndDatePicker = true },
                                trailingIcon = {
                                    IconButton(onClick = { showEndDatePicker = true }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Select date"
                                        )
                                    }
                                },
                                label = { Text("종료 날짜") },
                                readOnly = true,
                                singleLine = true,
                                enabled = true
                            )
                        }
                    }
                }
            }
        }
    }
    //0 날짜 다이얼로그
    if (showStartDatePicker) {
        DatePickerModal(
            onDateSelected = {
                onFilterChange(filterState.copy(startDate = it))
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        DatePickerModal(
            onDateSelected = {
                onFilterChange(filterState.copy(endDate = it))
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
}

@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    content : @Composable () -> Unit
) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Black,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(options) { index, label ->
            FilterButton(
                text = label,
                selected = selectedIndex == index,
                onClick = { onOptionSelected(index) }
            )
        }
    }
    content()
}



@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(100.dp)
            .height(44.dp), // 텍스트 공간 확보
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF3629B7) else Color(0xFFF2F1F9),
            contentColor = if (selected) Color.White else Color(0xFF343434)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}
