package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EventsScreen() {

    RequestLocationPermission()

    val sjsu = LatLng(37.3352, -121.8811)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sjsu, 15f)
    }

    val upcomingEvents = remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            db.collection("Events").whereLessThanOrEqualTo(Timestamp.now().toString(), "date").get()
                .addOnSuccessListener { events ->
                    upcomingEvents.value = events.mapNotNull { event ->
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
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            cameraPositionState = cameraPositionState
        )

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
                if (upcomingEvents.value.isNotEmpty()) {
                    upcomingEvents.value.map { event ->
                        item { EventComponent(event) }
                    }
                } else {
                    item {
                        Text(
                            "There are no upcoming events",
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
