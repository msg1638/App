package com.example.fcmtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FallEventDao {
    @Query("SELECT * FROM fall_events ORDER BY time ASC")
    suspend fun getAllEvents(): List<FallEvent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<FallEvent>)


    @Query("DELETE FROM fall_events WHERE Id IN(:eventsId)")
    suspend fun delete(eventsId : List<String>)

    @Query("UPDATE fall_events SET status = :status WHERE Id = :eventId")
    suspend fun updateEventStatus(eventId: String, status: String)

}