package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CreateEvent() {
    // State variables for user inputs
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var eventPicUri by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding around the content
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // Makes content scrollable
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between items
        ) {
            Text(
                text = "Create Event",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Event Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = eventPicUri,
                onValueChange = { eventPicUri = it },
                label = { Text("Event Picture URI") },
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
                value = time,
                onValueChange = { time = it },
                label = { Text("Start Time (HH:MM AM/PM)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = maxAttendees,
                onValueChange = { maxAttendees = it },
                label = { Text("Max Attendees") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = points,
                onValueChange = { points = it },
                label = { Text("Points Earned") },
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

            // Create Event Button
            Button(
                onClick = {
                    // Handle event creation logic
                    val maxAttendeesInt = maxAttendees.toIntOrNull() ?: 0
                    val pointsInt = points.toIntOrNull() ?: 0
                    val event = Event(
                        title = title,
                        author = author,
                        eventPicUri = eventPicUri,
                        date = date,
                        time = time,
                        location = location,
                        description = description,
                        maxAttendees = maxAttendeesInt,
                        points = pointsInt
                    )

                    // Push event to Firebase
                    isLoading = true
                    db.collection("Events")
                        .add(event)
                        .addOnSuccessListener { documentReference ->
                            isLoading = false
                            feedbackMessage = "Event created successfully with ID: ${documentReference.id}"
                            // Clear input fields
                            title = ""
                            author = ""
                            eventPicUri = ""
                            date = ""
                            time = ""
                            location = ""
                            description = ""
                            maxAttendees = ""
                            points = ""
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            feedbackMessage = "Error adding event: ${e.message}"
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Creating Event...")
                } else {
                    Text(text = "Create Event")
                }
            }

            // Display feedback message
            if (feedbackMessage.isNotEmpty()) {
                Text(
                    text = feedbackMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (feedbackMessage.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateEventPreview() {
    MyApplicationTheme {
        CreateEvent()
    }
}