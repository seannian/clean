package com.example.myapplication

data class Event(
    var title: String = "",
    var author: String = "",
    var eventPicUri: String = "",
    var date: String = "",
    var time: String = "",
    var location: String = "",
    var maxAttendees: Int = 0,
    var points: Int = 3
)
