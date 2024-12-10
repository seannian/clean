package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.Alignment
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendeeScreen(authorName: String, attendeesUsernames: List<String>) {
    val db = FirebaseFirestore.getInstance()
    val author = remember { mutableStateOf(User()) }
    val auth = FirebaseAuth.getInstance()
    val allAttendees = remember { mutableStateListOf<User>() }
    val loggedInUser = remember { mutableStateOf(User()) }
    val isLoading = remember { mutableStateOf(true) }
    Log.d("is this working", "bruh")

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        loggedInUser.value = document.toObject(User::class.java)!!
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "get failed with ", exception)
                }
        } else {
            Log.d("Auth", "No authenticated user found")
        }
        val authorQuerySnapshot = db.collection("Users")
            .whereEqualTo("username", authorName)
            .get()
            .await()

        if (!authorQuerySnapshot.isEmpty) {
            val document = authorQuerySnapshot.documents[0]
            author.value = document.toObject(User::class.java)!!
            Log.d("attendee", "Author: ${author.value.username}")
        }

        attendeesUsernames.forEach { attendeeName: String ->
            if (attendeeName != "hi") {
                val attendeeQuerySnapshot = db.collection("Users")
                    .whereEqualTo("username", attendeeName)
                    .get()
                    .await()
                if (!attendeeQuerySnapshot.isEmpty) {
                    val document = attendeeQuerySnapshot.documents[0]
                    val user = document.toObject(User::class.java)!!
                    allAttendees.add(user)
                }
            }
        }

        // Update loading state after fetching data
        isLoading.value = false
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                item {
                    TitleText("Event Host", 32.dp)
                    Row(
                        modifier = Modifier.padding(
                            start = 32.dp,
                            end = 32.dp,
                            top = 8.dp,
                        )
                    ) {
                        UserTile(user = author.value, loggedInUser = loggedInUser.value)
                    }

                    TitleText("Attendees", 32.dp)
                    Column(
                        modifier = Modifier.padding(
                            start = 32.dp,
                            end = 32.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    ) {
                        if (allAttendees.isNotEmpty()) {
                            allAttendees.forEach { attendee: User ->
                                UserTile(user = attendee, loggedInUser = loggedInUser.value)
                            }
                        } else {
                            SubText("No volunteers have signed up yet.", Color.Gray)
                        }
                    }
                }
            }
        }
    }

}