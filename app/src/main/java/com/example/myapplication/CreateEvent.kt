package com.example.myapplication

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
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
import java.time.Instant
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEvent(user: User?, navController: NavController, eventTitle: String?) {
    val db = FirebaseFirestore.getInstance()

    var title by remember { mutableStateOf("") }
    var author = user
    var eventPicUri by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<Timestamp?>(Timestamp(Date(System.currentTimeMillis()))) }
    var startTime by remember { mutableStateOf<Timestamp?>(null) }
    var endTime by remember { mutableStateOf<Timestamp?>(null) }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxAttendees by remember { mutableStateOf("") }
    var points by remember { mutableStateOf(0) }
    var currentAttendees by remember { mutableStateOf(0) }
    var attendeesUsernames by remember { mutableStateOf(listOf<String>()) }

    var showModal by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        db.collection("Events").whereEqualTo("title", eventTitle).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val event = document.toObject(Event::class.java)
                    if (event != null) {
                        title = event.title
                        eventPicUri = event.eventPicUri
                        date = event.date
                        startTime = event.startTime
                        endTime = event.endTime
                        location = event.location
                        description = event.description
                        maxAttendees = event.maxAttendees.toString()
                        points = event.points
                        currentAttendees = event.currentAttendees
                        attendeesUsernames = event.attendeesUsernames
                    }
                }
            }
    }

    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImage(it) { downloadUrl ->
                if (downloadUrl != null) {
                    eventPicUri = downloadUrl
                    Log.d("ImageUpload", "Event Picture URI: $eventPicUri")
                } else {
                    Log.e("ImageUpload", "Failed to upload image.")
                }
            }
        }
    }

    fun clearFields() {
        title = ""
        author = user
        eventPicUri = ""
        date = null
        startTime = null
        endTime = null
        location = ""
        description = ""
        maxAttendees = ""
        points = 0
        currentAttendees = 0
        attendeesUsernames = listOf()
    }

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
            colors = OutlinedTextFieldDefaults.colors(
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
            colors = OutlinedTextFieldDefaults.colors(
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
                    value = date?.toFormattedString("MM-dd-yyyy") ?: "",
                    onValueChange = { input ->
                        try {
                            val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                            sdf.timeZone = TimeZone.getTimeZone("UTC")
                            val parsedDate = sdf.parse(input)
                            date = parsedDate?.let { Timestamp(it) }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    placeholder = {
                        Text(
                            SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
                                .format(Date()).replace("-", "/")
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(175.dp)
                        .padding(bottom = 16.dp)
                        .pointerInput(date) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    showModal = true
                                }
                            }
                        },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BratGreen,
                        unfocusedBorderColor = ForestGreen
                    )
                )
                if (showModal) {
                    DatePickerModal(
                        onDateSelected = { millis ->
                            val eventDate = millis?.let { Date(it) }
                            date = eventDate?.let { Timestamp(it) }
                        },
                        onDismiss = { showModal = false }
                    )
                }
            }
            Column {
                CreateEventLabel("Max Volunteers")
                OutlinedTextField(
                    value = maxAttendees.toString(),
                    onValueChange = { maxAttendees = it },
                    placeholder = { Text("40") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .width(100.dp)
                        .padding(bottom = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
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
                    Text(
                        text = if (startTime != null) SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(startTime!!.toDate()) else "Select Start Time"
                    )
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
                    Text(
                        text = if (endTime != null) SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(endTime!!.toDate()) else "Select End Time"
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .width(10.dp)
                .padding(bottom = 10.dp)
        )
        CreateEventLabel("Upload a Thumbnail")
        UnfilledButton(
            onClick = {
                launcher.launch("image/*")
            },
            msg = "Upload Thumbnail",
            modifierWrapper = Modifier.width(200.dp)
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
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BratGreen,
                unfocusedBorderColor = ForestGreen
            )
        )

        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (eventTitle != "hi") Arrangement.SpaceEvenly else Arrangement.End
        ) {
            if (eventTitle != "hi") {
                UnfilledButton(onClick = {
                    db.collection("Events")
                        .whereEqualTo("title", eventTitle)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            for (document in querySnapshot) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Document deleted successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("Firestore", "Error deleting document", e)
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error getting documents", e)
                        }
                    navController.popBackStack()
                }, "Delete", modifierWrapper = Modifier.width(100.dp))
            }
            FilledButton(
                {
                    val maxAttendeesInt = maxAttendees.toIntOrNull() ?: 0
                    points = 1
                    val timestampDate = date ?: Timestamp.now()
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
                            points = points,
                            currentAttendees = currentAttendees,
                            attendeesUsernames = attendeesUsernames
                        )
                    }

                    isLoading = true
                    if (event != null) {
                        if (eventTitle != null && eventTitle != "hi") {
                            db.collection("Events")
                                .whereEqualTo("title", eventTitle)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (!querySnapshot.isEmpty) {
                                        val document = querySnapshot.documents[0]
                                        val eventId = document.id
                                        db.collection("Events").document(eventId).set(event)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                feedbackMessage = "Event updated successfully!"
                                                clearFields()
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                feedbackMessage =
                                                    "Error updating event: ${e.message}"
                                            }
                                    }
                                }
                        } else {
                            // Create new event
                            db.collection("Events")
                                .add(event)
                                .addOnSuccessListener { documentReference ->
                                    isLoading = false
                                    feedbackMessage =
                                        "Event created successfully with ID: ${documentReference.id}"
                                    clearFields()
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    feedbackMessage = "Error adding event: ${e.message}"
                                }
                        }
                    }
                    navController.popBackStack()
                },
                if (eventTitle != "hi") "Save" else "Create Event",
                if (eventTitle != "hi") Modifier.width(100.dp) else Modifier.width(150.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millisecondsInOneDay = 24 * 60 * 60 * 1000
                val selectedDateMillis = datePickerState.selectedDateMillis
                if (selectedDateMillis != null) {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.timeInMillis = selectedDateMillis
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    onDateSelected(calendar.timeInMillis + millisecondsInOneDay)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}