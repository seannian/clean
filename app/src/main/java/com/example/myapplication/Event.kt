package com.example.myapplication

import com.google.firebase.Timestamp

data class Event(
    val title: String = "",
    val author: String = "",
    val eventPicUri: String = "",
    val date: Timestamp? = null,  // Use Firestore Timestamp
    val startTime: Timestamp? = null,  // Changed to Firestore Timestamp
    val endTime: Timestamp? = null,    // Changed to Firestore Timestamp
    val location: String = "",
    val description: String = "",
    val maxAttendees: Int = 0,
    val points: Int = 0,
    val currentAttendees: Int = 0,  // New count for current attendees
    val attendeesUsernames: List<String> = listOf()  // New list for usernames of attendees
)
