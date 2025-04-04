package com.example.fcmtest.database

import android.content.Context

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
    suspend fun updateFallEventStatus(eventId: String, status: String) {
        fallEventDao.updateEventStatus(eventId, status)
    }

    suspend fun example1() {
        saveFallEvents(listOf(
            FallEvent("fall_1", "2025-03-27 14:30", "읽지 않음"),
            FallEvent("fall_2", "2025-03-27 15:00", "읽지 않음"),
            FallEvent("fall_3", "2025-03-28 09:15", "읽지 않음")
        ))
    }

}