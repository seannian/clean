package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyEventsScreen(myEvents: Boolean) { // doubles as Past Events screen
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TitleText(if (myEvents) "My Events" else "Past Events")
    }
}