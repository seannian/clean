package com.example.myapplication

import com.google.firebase.Timestamp

data class Event(
    val title: String = "",
    val author: String = "",
    val eventPicUri: String = "",
    val date: Timestamp? = null,
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val location: String = "",
    val description: String = "",
    val maxAttendees: Int = 0,
    val points: Int = 0,
    val currentAttendees: Int = 0,
    val attendeesUsernames: List<String> = listOf(),
)
