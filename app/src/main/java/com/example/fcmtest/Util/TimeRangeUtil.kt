package com.example.fcmtest.Util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
object TimeRangeUtil {
    fun getStartAndEndOfWeek(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfWeek = now.with(DayOfWeek.MONDAY).atStartOfDay()
        val endOfWeek = startOfWeek.plusDays(7).minusNanos(1)
        return Pair(startOfWeek.toEpochMilli(), endOfWeek.toEpochMilli())
    }

    fun getStartAndEndOfMonth(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfMonth = now.withDayOfMonth(1).atStartOfDay()
        val endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59)
        return Pair(startOfMonth.toEpochMilli(), endOfMonth.toEpochMilli())
    }

    fun getStartAndEndOfYear(): Pair<Long, Long> {
        val now = LocalDate.now()
        val startOfYear = LocalDate.of(now.year, 1, 1).atStartOfDay()
        val endOfYear = LocalDate.of(now.year, 12, 31).atTime(23, 59, 59)
        return Pair(startOfYear.toEpochMilli(), endOfYear.toEpochMilli())
    }

    private fun LocalDateTime.toEpochMilli(): Long {
        return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
