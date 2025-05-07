package com.example.fcmtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fcmtest.analytics.StatsResultCore
import com.example.fcmtest.analytics.TimeRangeCountResult

@Dao
interface FallEventDao {
    @Query("SELECT * FROM fall_events ORDER BY timestamp ASC")
    suspend fun getAllEvents(): List<FallEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<FallEvent>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: FallEvent)

    @Query("DELETE FROM fall_events WHERE Id IN(:eventsId)")
    suspend fun delete(eventsId : List<String>)

    @Query("UPDATE fall_events SET status = :status WHERE Id = :eventId")
    suspend fun updateEventStatus(eventId: String, status: String)

    @Query("UPDATE fall_events SET analysis = :analysis WHERE Id = :eventId")
    suspend fun updateEventAnalysis(eventId: String, analysis: String)

    @Query("""SELECT COUNT(*) as total,
            SUM(CASE WHEN status = "읽음" THEN 1 ELSE 0 END) as readCount,
            SUM(CASE WHEN analysis = "분석" THEN 1 ELSE 0 END) as analyzedCount
        FROM fall_events WHERE timestamp BETWEEN :startTime AND :endTime
    """)
    suspend fun getStatsInRange(startTime: Long, endTime: Long): StatsResultCore

    @Query("""
    SELECT 
        SUM(CASE WHEN hour BETWEEN 0 AND 5 THEN 1 ELSE 0 END) AS slot0,
        SUM(CASE WHEN hour BETWEEN 6 AND 11 THEN 1 ELSE 0 END) AS slot1,
        SUM(CASE WHEN hour BETWEEN 12 AND 17 THEN 1 ELSE 0 END) AS slot2,
        SUM(CASE WHEN hour BETWEEN 18 AND 23 THEN 1 ELSE 0 END) AS slot3
    FROM fall_events
    WHERE timestamp BETWEEN :startTime AND :endTime
""")
    suspend fun getTimeRangeCounts(startTime: Long, endTime: Long): TimeRangeCountResult


}