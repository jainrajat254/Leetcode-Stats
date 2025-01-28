package com.example.leetcode.data

data class UserStats(
    val username: String,
    val totalSolved: Int,
    val easySolved: Int,
    val mediumSolved: Int,
    val hardSolved: Int,
    val acceptanceRate: Double,
    val ranking: Int,
    val submissionCalendar: List<Boolean>,
    val submittedToday: Boolean
)
