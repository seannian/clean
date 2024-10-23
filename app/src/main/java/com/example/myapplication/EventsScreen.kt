package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EventsScreen() {

    RequestLocationPermission()
    val scrollState = rememberScrollState()
    val sjsu = LatLng(37.3352, -121.8811)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sjsu, 15f)
    }
    val event = Event(title = "SJSU Tower Lawn Clean-up Event", author = "user1", eventPicUri = "", date = "Friday, 10/24/2024", time = "5:00 pm - 7:00 pm", location = "Tower Lawn", maxAttendees = 40, points = 3)
    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            cameraPositionState = cameraPositionState
        )
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
            TitleText("Events")
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                EventComponent(event)
            }
        }
    }
}