package com.example.fcmtest.Util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class Date(timestamp : String){
    val year: Int
    val month: Int
    val day: Int
    val hour: Int
    val minute: Int
    val second : Int

    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:m:s")
        val dateTime = LocalDateTime.parse(timestamp, formatter)

        year = dateTime.year
        month = dateTime.monthValue
        day = dateTime.dayOfMonth
        hour = dateTime.hour
        minute = dateTime.minute
        second = dateTime.second
    }
    fun getFormattedDate(): String {
        return "${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분"
    }


}