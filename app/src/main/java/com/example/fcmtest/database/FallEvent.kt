package com.example.fcmtest.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fall_events")
data class FallEvent(
    @PrimaryKey val Id: String = "",
    val time: String = "",
    var status: String = ""
)