package com.example.fcmtest.Util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fcmtest.database.FallEvent
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun getFallEvent(id : String, time : String, status : String, analysis : String) : FallEvent{
    val date = Date(time)
    val calendar = Calendar.getInstance()
    calendar.set(date.year, date.month - 1, date.day, date.hour, date.minute)

    val fallevent = FallEvent(
        Id = id,
        timestamp = calendar.timeInMillis,
        year = date.year,
        month = date.month,
        day = date.day,
        hour = date.hour,
        minute = date.minute,
        second = date.second,
        status = status,
        analysis = analysis
        )
    return fallevent
}