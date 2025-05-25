package com.example.fcmtest.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.fcmtest.Util.TimeRangeUtil
import com.example.fcmtest.Util.getFallEvent
import com.example.fcmtest.analytics.StatsResult
import com.example.fcmtest.customUI.LoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FallEventRepository(context: Context) {
    private val fallEventDao = AppDatabase.getDatabase(context).fallEventDao()
    val eventFlow = getAllFallEvents()
    fun getAllFallEvents(): Flow<List<FallEvent>> {
        LoadingState.show()
        val allevent = fallEventDao.getAllEvents()
        LoadingState.hide()
        return allevent
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
    suspend fun updateFallEventReadStatus(eventId: String) {
        fallEventDao.updateEventReadStatus(eventId)
    }
    suspend fun updateFallEventAnalysisStatus(eventId: String) {
        fallEventDao.updateEventAnalysisStatus(eventId)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getStats(Period : String) : Flow<StatsResult>{
        var timepair : Pair<Long,Long>
        if(Period == "이번달") timepair = TimeRangeUtil.getStartAndEndOfMonth()
        else if(Period == "이번년도") timepair = TimeRangeUtil.getStartAndEndOfYear()
        else timepair = TimeRangeUtil.getStartAndEndOfWeek()
        Log.d("시작","${timepair.first}")
        Log.d("끝","${timepair.second}")
        val statcore = fallEventDao.getStatsInRange(timepair.first,timepair.second)
        val timeRangeCount = fallEventDao.getTimeRangeCounts(timepair.first, timepair.second)
        return combine(statcore,timeRangeCount) { stat, timerange ->
            StatsResult(stat.total, stat.readCount, stat.analyzedCount, timerange.toList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun example1() {
        saveFallEvents(listOf(
            getFallEvent("fall_1", "2025-05-8 05:30:05", "읽음", "분석"),
            getFallEvent("fall_2", "2025-05-7 08:30:05", "읽음", "분석"),
            getFallEvent("fall_2561", "2025-05-6 14:30:05", "읽음", "미분석"),
            getFallEvent("fall_5", "2025-05-8 16:30:05", "읽음", "미분석"),
            getFallEvent("fall_6", "2025-05-7 18:30:05", "읽음", "미분석"),
            getFallEvent("fall_2567", "2025-05-6 22:30:05", "읽음", "미분석"),
            getFallEvent("fall_8", "2025-05-8 22:30:05", "읽음", "분석"),
            getFallEvent("fall_9", "2025-05-7 22:30:05", "읽음", "미분석"),
            getFallEvent("fall_25619", "2025-05-6 22:30:05", "읽지 않음", "미분석")
        ))

    }

}