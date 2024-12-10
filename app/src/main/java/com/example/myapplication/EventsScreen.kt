package com.example.myapplication

import SearchBar
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.json.JSONObject
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Properties
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.Layout
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.File
import java.io.InputStreamReader
import java.net.URLEncoder

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
    var markerQuery by remember { mutableStateOf("") }

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

    LaunchedEffect(markerQuery) {
        filteredEvents.value = if (markerQuery.isEmpty()) {
            allEvents.value
        } else {
            allEvents.value.filter { event ->
                event.location.contains(markerQuery, ignoreCase = true)
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
        ) {
            filteredEvents.value.forEach { event ->
                CustomMapMarker(
                    event = event,
                    onClick = {
                        markerQuery = event.location
                    }
                )
            }
        }

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
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TitleText("Events", 16.dp)
                        RefreshIcon(onClick = {
                            markerQuery = ""
                        })
                    }
                }
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
}

@Composable
fun CustomMapMarker(
    event: Event,
    onClick: () -> Unit
) {
    val location = getCoordinates(event.location, "AIzaSyCB90tDKU7br2yap1rANMrLii6BAathdkU")
    Log.d("coordinates", event.title + ": " + location)
    val markerState = remember { MarkerState(position = location) }
    val shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 0.dp)

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(event.eventPicUri)
            .allowHardware(false)
            .build()
    )

    MarkerComposable(
        keys = arrayOf(event.title, painter.state),
        state = markerState,
        title = event.title,
        anchor = Offset(0.5f, 1f),
        onClick = {
            onClick()
            true
        }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape)
                .background(Color.LightGray)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (event.eventPicUri.isNotEmpty()) {
                Image(
                    painter = painter,
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = event.title.take(1).uppercase(),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

fun getCoordinates(address: String, apiKey: String): LatLng {
    val encodedAddress = URLEncoder.encode(address, "UTF-8") // Properly encode the address
    Log.d("encodedAddress", encodedAddress)
    Log.d("apiKey", apiKey)
    val url =
        "https://maps.googleapis.com/maps/api/geocode/json?address=$encodedAddress&key=$apiKey"

    var latLng = LatLng(37.3352, -121.8811)

    try {
        val thread = Thread {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)

            if (json.getString("status") == "OK") {
                val location = json.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                val latitude = location.getDouble("lat")
                val longitude = location.getDouble("lng")
                latLng = LatLng(latitude, longitude)
                Log.d("in getCoordinates", latLng.toString())
            } else {
                Log.d("in getCoordinates", "Error: ${json.getString("status")}")
            }
        }
        thread.start()
        thread.join()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return latLng
}