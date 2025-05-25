package com.example.fcmtest.Screen.EventItem

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fcmtest.R
import com.example.fcmtest.database.FallEvent


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FallEventItem(
    index: Int,
    event: FallEvent,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: (FallEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .shadow(
                elevation = 30.dp,
                spotColor = Color(0x123629B7),
                ambientColor = Color(0x123629B7)
            )
            .height(88.dp)
            .fillMaxWidth()
            .clipToBounds()
            .clickable {
                onClick(event)
                onCheckedChange(isSelected)
            }
            .background(color = Color(0xFF125151), shape = RoundedCornerShape(size = 15.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEF6F8)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 상단 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${event.month}월",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF343434),
                    )
                )
                Text(
                    text = "${event.year}/${event.month}/${event.day}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF979797)
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // 상태
                Row {
                    Text(
                        text = "상태",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF979797)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.status,
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(600),
                            color = if (event.status == "읽지 않음") Color(0xFFFF4267) else Color(
                                0xFF3629B7
                            )
                        )
                    )
                }

                // 발생 시각
                Row {
                    Text(
                        text = "발생 시각",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF979797)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${event.hour} : ${event.minute}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF3629B7)
                        )
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisEventItem(
    event: FallEvent,
    onClick: (FallEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .shadow(
                elevation = 30.dp,
                spotColor = Color(0x123629B7),
                ambientColor = Color(0x123629B7)
            )
            .fillMaxWidth()
            .height(88.dp)
            .padding(horizontal = 8.dp)
            .background(color = Color(0xFF125151), shape = RoundedCornerShape(size = 15.dp))
            .clickable{
                onClick(event)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEEF6F8)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 상단 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${event.month}월",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF343434),
                    )
                )
                Text(
                    text = "${event.year}/${event.month}/${event.day}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF979797)
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // 상태
                Row {
                    Text(
                        text = "상태",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF979797)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.analysis,
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(600),
                            color = if (event.analysis == "미분석") Color(0xFFFF4267) else Color(
                                0xFF3629B7
                            )
                        )
                    )
                }

                // 발생 시각
                Row {
                    Text(
                        text = "발생 시각",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF979797)
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${event.hour} : ${event.minute}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF3629B7)
                        )
                    )
                }
            }
        }
    }
}