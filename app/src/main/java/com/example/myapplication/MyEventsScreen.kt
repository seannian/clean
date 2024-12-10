package com.example.myapplication

import SearchBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


@Composable
fun MyEventsScreen(
    user: User,
    isMyEvents: Boolean,
    navController: NavController
) { // doubles as Past Events screen
    val allEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    val filteredEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("Events")

            val queryCondition: Query = if (isMyEvents) {
                query.whereEqualTo("author", user.username)
            } else {
                query.whereLessThan("date", Timestamp.now())
            }

            queryCondition.get()
                .addOnSuccessListener { events ->
                    val fetchedEvents = events.mapNotNull { event ->
                        event.toObject(Event::class.java)
                    }
                    allEvents.value = fetchedEvents
                    filteredEvents.value = fetchedEvents // Initialize with all events
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

    // Update filtered events when the search query changes
    LaunchedEffect(searchQuery) {
        filteredEvents.value = if (searchQuery.isEmpty()) {
            allEvents.value
        } else {
            allEvents.value.filter { event ->
                event.title.contains(searchQuery, ignoreCase = true) ||
                        event.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TitleText(if (isMyEvents) "My Events" else "Past Events", 32.dp)

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
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholderText = if (isMyEvents) "Search for your events" else "Search for past events",
                )
                LazyColumn(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
                    if (filteredEvents.value.isNotEmpty()) {
                        items(filteredEvents.value) { event ->
                            EventComponent(
                                event,
                                if (isMyEvents) "My Events" else "Past Events",
                                navController
                            )
                        }
                    } else {
                        item {
                            Text(
                                if (isMyEvents) "No matching events found" else "No past events found",
                                modifier = Modifier.padding(start = 16.dp),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        if (isMyEvents) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                HostCleanupButton(
                    onClick = { navigateToCreateEvent(navController) },
                    "Host a Clean-up"
                )
            }
        }
    }
}

fun navigateToCreateEvent(navController: NavController) {
    val title = "hi"
    navController.navigate("create_events/$title")
}
