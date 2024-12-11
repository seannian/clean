package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.Grey
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

@Composable
fun EventComponent(event: Event, parentPage: String, navController: NavController) {
    val placeholderPainter = painterResource(id = R.drawable.cs_160_project_logo)
    var user by remember { mutableStateOf<User?>(null) }
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    var joinButtonMsg by remember { mutableStateOf("Join") }

    LaunchedEffect(auth.currentUser) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        user = document.toObject(User::class.java)
                        if (user?.let { event.attendeesUsernames.contains(it.username) } == true) {
                            joinButtonMsg = "Unjoin"
                        }
                        Log.d("Firestore", "User retrieved: $user")
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "get failed with ", exception)
                }
        } else {
            Log.d("Auth", "No authenticated user found")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp)
            .clickable {
                val authorName = event.author
                val attendeesUsernames =
                    if (event.attendeesUsernames.isEmpty()) "hi" else event.attendeesUsernames.joinToString(
                        ","
                    )
                Log.d("in event component", authorName)
                navController.navigate("attendee_screen/$authorName/$attendeesUsernames")
            },
    ) {
        Column(modifier = Modifier.height(100.dp)) {
            Box(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .background(Color.Gray)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                if (event.eventPicUri.isNotEmpty()) {
                    AsyncImage(
                        model = event.eventPicUri,
                        contentDescription = "Event picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = placeholderPainter,
                        error = placeholderPainter
                    )
                } else {
                    Image(
                        painter = placeholderPainter,
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            EventTitleText(event.title)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User Icon",
                    tint = Grey
                )
                Spacer(modifier = Modifier.width(10.dp))
                SubText(event.author, Grey)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Date Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(event.date?.toFormattedString("MM-dd-yyyy") ?: "Date not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClockIcon()
        Spacer(modifier = Modifier.width(10.dp))
        SubText(
            event.startTime?.toFormattedString("hh:mm a") + " - " + event.endTime?.toFormattedString(
                "hh:mm a"
            ), Grey
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Place, contentDescription = "Location Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(if (event.location.isNotEmpty()) event.location else "Location not set", Grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Person, contentDescription = "Attendee Icon", tint = Grey)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(
            if (event.maxAttendees == 1) "${event.maxAttendees} person maximum" else "${event.maxAttendees} people maximum",
            Grey
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star Icon", tint = BratGreen)
        Spacer(modifier = Modifier.width(10.dp))
        SubText(
            if (event.points == 1) "${event.points} point" else "${event.points} points",
            Color.Black
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Description",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
                    .padding(bottom = 10.dp)
            )
            SubText(
                event.description, Grey
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if (parentPage == "Past Events") {
            Text(
                text = "Completed",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                color = Color.Black
            )
        } else if (parentPage == "My Events") {
            UnfilledButton(onClick = {
                try {
                    val title = event.title
                    navController.navigate("create_events/$title")
                } catch (e: Exception) {
                    Log.e("NavigationError", "Error navigating with event JSON", e)
                }
            }, "Edit", modifierWrapper = Modifier.width(100.dp))
        } else {
            PrimaryButton(joinButtonMsg,
                onClick = {
                    user?.let { currentUser ->
                        firestore.collection("Events")
                            .whereEqualTo("title", event.title)
                            .whereEqualTo("author", event.author)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    val document = documents.documents[0]
                                    val eventRef = document.reference
                                    firestore.runTransaction { transaction ->
                                        val snapshot = transaction.get(eventRef)
                                        val currentAttendees = snapshot.getLong("currentAttendees") ?: 0
                                        val attendeesUsernames = snapshot.get("attendeesUsernames") as? List<String> ?: emptyList()
                                        val maxAttendees = snapshot.getLong("maxAttendees") ?: 0

                                        val isJoining = !attendeesUsernames.contains(currentUser.username)

                                        // Prevent joining if the event is at full capacity
                                        if (isJoining && currentAttendees >= maxAttendees) {
                                            // Do nothing, or show a message (not shown here)
                                            return@runTransaction null
                                        }

                                        val newAttendeesCount = if (isJoining) {
                                            currentAttendees + 1
                                        } else {
                                            if (currentAttendees > 0) currentAttendees - 1 else 0
                                        }

                                        val updatedAttendeesUsernames = attendeesUsernames.toMutableList()
                                        if (isJoining) {
                                            updatedAttendeesUsernames.add(currentUser.username)
                                        } else {
                                            updatedAttendeesUsernames.remove(currentUser.username)
                                        }

                                        // Calculate new points based on newAttendeesCount
                                        // Formula: ceil((newAttendeesCount / 2.0) + 0.5)
                                        val newPoints = ceil((newAttendeesCount.toDouble() / 2.0) + 0.5).toInt()

                                        transaction.update(eventRef, "currentAttendees", newAttendeesCount)
                                        transaction.update(eventRef, "attendeesUsernames", updatedAttendeesUsernames)
                                        transaction.update(eventRef, "points", newPoints)

                                        joinButtonMsg = if (isJoining) "Unjoin" else "Join"
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "Firestore",
                                            "Event updated successfully with new attendee"
                                        )
                                    }.addOnFailureListener { e ->
                                        Log.d("Firestore", "Failed to update event: ${e.message}")
                                    }
                                } else {
                                    Log.d("Firestore", "Event not found")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.d("Firestore", "Failed to retrieve event: ${e.message}")
                            }
                    } ?: run {
                        Log.d("Auth", "User not authenticated")
                    }
                })
        }
    }
}

/**
 * Extension function to convert Timestamp to a formatted String
 */
fun Timestamp.toFormattedString(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(this.toDate())
}