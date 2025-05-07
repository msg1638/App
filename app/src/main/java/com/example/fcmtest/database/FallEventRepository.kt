package com.example.fcmtest.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fcmtest.Util.TimeRangeUtil
import com.example.fcmtest.Util.getFallEvent
import com.example.fcmtest.analytics.StatsResult

class FallEventRepository(context: Context) {
    private val fallEventDao = AppDatabase.getDatabase(context).fallEventDao()

    suspend fun getAllFallEvents(): List<FallEvent> {
        return fallEventDao.getAllEvents()
    }

    suspend fun saveFallEvents(events: List<FallEvent>) {
        fallEventDao.insertEvents(events)
    }
    suspend fun deleteFallEvents(events: List<FallEvent>){
        fallEventDao.delete(events.map{it.Id})

    }
    suspend fun addFallEvents(events: List<FallEvent>){
        fallEventDao.insertEvents(events)
    }
    suspend fun addFallEvents(events: FallEvent){
        fallEventDao.insertEvent(events)
    }
    suspend fun updateFallEventStatus(eventId: String, status: String) {
        fallEventDao.updateEventStatus(eventId, status)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getStats(Period : String) : StatsResult{
        var timepair : Pair<Long,Long>
        if(Period == "이번달") timepair = TimeRangeUtil.getStartAndEndOfMonth()
        else if(Period == "이번년도") timepair = TimeRangeUtil.getStartAndEndOfYear()
        else timepair = TimeRangeUtil.getStartAndEndOfWeek()
        Log.d("시작","${timepair.first}")
        Log.d("끝","${timepair.second}")
        val statcore = fallEventDao.getStatsInRange(timepair.first,timepair.second)
        val stat = StatsResult(statcore.total,statcore.readCount,statcore.analyzedCount)
        stat.timeRangeCount = fallEventDao.getTimeRangeCounts(timepair.first, timepair.second).toList()
        return stat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun example1() {
        saveFallEvents(listOf(
            getFallEvent("fall_1", "2025-05-6 14:30:05", "읽지 않음", "미분석"),
            getFallEvent("fall_2", "2025-05-07 15:00:11", "읽음", "미분석"),
            getFallEvent("fall_3", "2025-05-8 09:15:15", "읽지 않음", "분석"),
            getFallEvent("fall_4", "2025-4-30 09:15:33", "읽지 않음", "분석"),
            getFallEvent("fall_5", "2025-04-30 14:30:05", "읽지 않음", "분석"),
            getFallEvent("fall_6", "2025-03-27 15:00:11", "읽음", "미분석"),
            getFallEvent("fall_7", "2025-02-27 09:15:15", "읽지 않음", "분석"),
            getFallEvent("fall_8", "2024-12-28 09:15:33", "읽지 않음", "미분석")
        ))

    }

}