package com.example.myapplication;

class User (
    var description: String = "",
    var joinDate: String = "",
    var profilePicture: String = "",
    var totalNumberOfCleanups: Int = 0,
    var email: String = "",
    var score: Int = 0,
    var username: String = "",
    var friends: MutableList<String> = mutableListOf(),
    var friendRequests: MutableList<String> = mutableListOf()
)