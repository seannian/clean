package com.example.myapplication

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.ForestGreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(user: User?, navController: NavController) {
    // State variables for user inputs
    var title by remember { mutableStateOf("") }
    var author = user
    var eventPicUri by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf<Timestamp?>(null) }
    var endTime by remember { mutableStateOf<Timestamp?>(null) }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }

    // New state variables for currentAttendees and attendeesUsernames
    var currentAttendees by remember { mutableStateOf(0) }
    var attendeesUsernames by remember { mutableStateOf(listOf<String>()) }

    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Great! Let's plan your clean-up event!",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp,
                modifier = Modifier.width(350.dp),
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
            modifier = Modifier.fillMaxWidth(),
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
                            SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                                .format(Date()).replace("-", "/")
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                CreateEventLabel("Start Time")
                Button(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour: Int, minute: Int ->
                                calendar.set(Calendar.HOUR_OF_DAY, hour)
                                calendar.set(Calendar.MINUTE, minute)
                                startTime = Timestamp(calendar.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BratGreen)
                ) {
                    Text(text = if (startTime != null) SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startTime!!.toDate()) else "Select Start Time")
                }
            }
            Column {
                CreateEventLabel("End Time")
                Button(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour: Int, minute: Int ->
                                calendar.set(Calendar.HOUR_OF_DAY, hour)
                                calendar.set(Calendar.MINUTE, minute)
                                endTime = Timestamp(calendar.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BratGreen)
                ) {
                    Text(text = if (endTime != null) SimpleDateFormat("hh:mm a", Locale.getDefault()).format(endTime!!.toDate()) else "Select End Time")
                }
            }
        }

        CreateEventLabel("Upload a Thumbnail")
        UnfilledButton({}, "Upload Thumbnail")

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UnfilledButton({}, "Save as Draft")
            FilledButton(
                {
                    val maxAttendeesInt = maxAttendees.toIntOrNull() ?: 0
                    val pointsInt = points.toIntOrNull() ?: 0
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    val parsedDate = dateFormat.parse(date)
                    val timestampDate = if (parsedDate != null) Timestamp(parsedDate) else Timestamp.now()
                    val event = user?.let {
                        Event(
                            title = title,
                            author = it.username,
                            eventPicUri = eventPicUri,
                            date = timestampDate,
                            startTime = startTime,
                            endTime = endTime,
                            location = location,
                            description = description,
                            maxAttendees = maxAttendeesInt,
                            points = pointsInt,
                            currentAttendees = currentAttendees,  // Set to initial value
                            attendeesUsernames = attendeesUsernames // Set to initial value
                        )
                    }

                    isLoading = true

                    if (event != null) {
                        db.collection("Events")
                            .add(event)
                            .addOnSuccessListener { documentReference ->
                                isLoading = false
                                feedbackMessage =
                                    "Event created successfully with ID: ${documentReference.id}"
                                // Clear input fields
                                title = ""
                                author = user
                                eventPicUri = ""
                                date = ""
                                startTime = null
                                endTime = null
                                location = ""
                                description = ""
                                maxAttendees = ""
                                points = ""
                                currentAttendees = 0
                                attendeesUsernames = listOf()
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                feedbackMessage = "Error adding event: ${e.message}"
                            }
                    }
                },
                "Create Event"
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

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