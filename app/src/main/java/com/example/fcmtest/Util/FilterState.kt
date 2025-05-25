package com.example.fcmtest.Util
data class FilterState(
    val selectedRead: Int = 0,
    val selectedAnalysis: Int = 0,
    val selectedDate: Int = 0,
    val startDate: Long? = null,
    val endDate: Long? = null,
)