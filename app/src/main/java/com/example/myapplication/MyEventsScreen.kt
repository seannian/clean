package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.Timestamp

@Composable
fun MyEventsScreen(user: User, isMyEvents: Boolean) { // doubles as Past Events screen
    val allEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Events")

            val queryCondition: Query = if (isMyEvents) {
                query.whereEqualTo("author", user.username)
            } else {
                query.whereGreaterThan("date", Timestamp.now())
            }

            queryCondition.get()
                .addOnSuccessListener { events ->
                    allEvents.value = events.mapNotNull { event ->
                        event.toObject(Event::class.java)
                    }
                    isLoading.value = false
                }
                .addOnFailureListener { exception ->
                    exception.message?.let { Log.d("fetch events error", it) }
                    isLoading.value = false
                }
        } catch (e: Exception) {
            isLoading.value = false
            Log.d("Firebase query error", e.message ?: "Unknown error")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleText(if (isMyEvents) "My Events" else "Past Events", 16.dp)

        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                item { TitleText("Events", 16.dp) }
                if (allEvents.value.isNotEmpty()) {
                    items(allEvents.value) { event ->
                        EventComponent(event)
                    }
                } else {
                    item {
                        Text(
                            "You have not created any events",
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}