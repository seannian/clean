package com.example.myapplication

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.ForestGreen
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(navController: NavController) {
    // State variables for user inputs
    var title by remember { mutableStateOf("") }
    var author = "" // should be user
    var eventPicUri by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Great! Let's plan your clean-up event!",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp,
                modifier = Modifier
                    .padding(0.dp)
                    .width(350.dp),
                color = Color.Black
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Drawer",
                    tint = BratGreen,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        HorizontalDivider(
            color = ForestGreen,
            thickness = 5.dp,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .width(100.dp)
        )

        CreateEventLabel("Event Title")
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Enter your event title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BratGreen,
                unfocusedBorderColor = ForestGreen
            )
        )
        CreateEventLabel("Location")
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            placeholder = { Text("1 Washington Sq.") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BratGreen,
                unfocusedBorderColor = ForestGreen
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                CreateEventLabel("Date")
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    placeholder = {
                        Text(
                            SimpleDateFormat(
                                "MM-dd-yyyy",
                                Locale.getDefault()
                            ).format(Date()).replace("-", "/")
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BratGreen,
                        unfocusedBorderColor = ForestGreen
                    )
                )
            }
            Column {
                CreateEventLabel("Max Volunteers")
                OutlinedTextField(
                    value = maxAttendees,
                    onValueChange = { maxAttendees = it },
                    placeholder = { Text("40") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BratGreen,
                        unfocusedBorderColor = ForestGreen
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                CreateEventLabel("Start Time")
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    placeholder = {
                        Text(
                            SimpleDateFormat(
                                "hh:mm a",
                                Locale.getDefault()
                            ).format(Date())
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BratGreen,
                        unfocusedBorderColor = ForestGreen
                    )
                )
            }
            Column {
                CreateEventLabel("End Time")
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    placeholder = {
                        Text(
                            SimpleDateFormat(
                                "hh:mm a",
                                Locale.getDefault()
                            ).format(Date())
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .width(150.dp)
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = BratGreen,
                        unfocusedBorderColor = ForestGreen
                    )
                )
            }
        }

        CreateEventLabel("Upload a Thumbnail")
        UnfilledButton(
            {}, "Upload Thumbnail",
        )

        Spacer(modifier = Modifier.padding(bottom = 10.dp))

        CreateEventLabel("Description")
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Give a brief description about the event") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = BratGreen,
                unfocusedBorderColor = ForestGreen
            )
        )

        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UnfilledButton(
                {}, "Save as Draft",
            )

            // Create Event Button
            FilledButton(
                {
                    val maxAttendeesInt = maxAttendees.toIntOrNull() ?: 0
                    val pointsInt = points.toIntOrNull() ?: 0
                    val event = Event(
                        title = title,
                        author = "", // replace with current user
                        eventPicUri = eventPicUri,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
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
                            feedbackMessage =
                                "Event created successfully with ID: ${documentReference.id}"
                            // Clear input fields
                            title = ""
                            author = ""
                            eventPicUri = ""
                            date = ""
                            startTime = ""
                            endTime = ""
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
                "Create Event",
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
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

fun uploadThumbnail() {

}