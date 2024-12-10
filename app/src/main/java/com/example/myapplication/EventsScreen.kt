package com.example.myapplication

import SearchBar
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EventsScreen(navController: NavController) {

    RequestLocationPermission()

    val sjsu = LatLng(37.3352, -121.8811)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sjsu, 15f)
    }

    val allEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    val filteredEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("Events")
                .whereGreaterThanOrEqualTo("date", Timestamp.now())
                .get()
                .addOnSuccessListener { events ->
                    val fetchedEvents = events.documents.mapNotNull { document ->
                        document.toObject(Event::class.java)
                    }
                    allEvents.value = fetchedEvents
                    filteredEvents.value = fetchedEvents // Initialize with all events
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    Log.d("fetch events error", exception.message ?: "Unknown error")
                    isLoading = false
                }
        } catch (e: Exception) {
            isLoading = false
            Log.d("Firebase query error", e.message ?: "Unknown error")
        }
    }

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

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search for other events",
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            cameraPositionState = cameraPositionState
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                item { TitleText("Events", 16.dp) }
                item { Spacer(modifier = Modifier.padding(bottom = 16.dp)) }
                if (filteredEvents.value.isNotEmpty()) {
                    items(filteredEvents.value) { event ->
                        EventComponent(event, "Events", navController)
                    }
                } else {
                    item {
                        Text(
                            "No events match your search",
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}}
