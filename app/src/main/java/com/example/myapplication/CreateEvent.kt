// CreateEvent.kt
package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CreateEvent() {
    // State variables for user inputs
    var eventTitle by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var maxVolunteers by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    // Note: Upload thumbnail functionality would require additional implementation

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding around the content
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between items
        ) {
            Text(text = "Create Event", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Event Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (MM/DD/YYYY)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = maxVolunteers,
                onValueChange = { maxVolunteers = it },
                label = { Text("Max Volunteers") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = startTime,
                onValueChange = { startTime = it },
                label = { Text("Start Time (HH:MM)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = endTime,
                onValueChange = { endTime = it },
                label = { Text("End Time (HH:MM)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Placeholder for Upload Thumbnail
            Button(
                onClick = { /* Handle thumbnail upload */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Upload Thumbnail")
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Increase height for multi-line input
            )

            // Submit Button (Optional)
            Button(
                onClick = {
                    // Handle event creation logic
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Create Event")
            }
        }
    }
}
