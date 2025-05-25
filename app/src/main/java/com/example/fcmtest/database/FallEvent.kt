package com.example.fcmtest.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fall_events")
data class FallEvent(
    @PrimaryKey val Id: String = "",
    val timestamp: Long = 0,      // 밀리초 저장
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0,
    val second : Int = 0,
    var status: String = "",
    var analysis: String = ""
)