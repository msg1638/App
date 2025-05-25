package com.example.fcmtest.analytics

data class StatsResultCore(
    val total: Int = 0,
    val readCount: Int = 0,
    val analyzedCount: Int = 0
)

data class StatsResult(
    val total: Int = 0,
    val readCount: Int = 0,
    val analyzedCount: Int = 0,
    var timeRangeCount: List<Int> = listOf(0, 0, 0, 0) //00~05,06~11,12~17,18~23
)
data class TimeRangeCountResult(
    val slot0: Int, // 00~05시
    val slot1: Int, // 06~11시
    val slot2: Int, // 12~17시
    val slot3: Int  // 18~23시
) {
    fun toList(): List<Int> = listOf(slot0, slot1, slot2, slot3)
}