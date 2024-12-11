package com.example.myapplication

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.Grey
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.concurrent.TimeUnit

@Composable
fun UserTile(user: User, loggedInUser: User, navController: NavController, event: Event) {

    // Info Date
    // need to retrieve current logged in user
    // check if logged in user is same as this user, Edit button
    // if logged in user is friends with this user, Unfriend button
    // if logged in user is not friends with this user, Friend button
    var buttonMsg = "Friend"
    if (loggedInUser.username == user.username) {
        buttonMsg = "Edit"
    }
    if (loggedInUser.friends.contains(user.username)) {
        buttonMsg = "Unfriend"
    }
    var markedAsAttendedMsg = remember { mutableStateOf("Attended?") }
    // need to add friend list to user class
    var imageURL = remember { mutableStateOf(user.profilePicture) }
    val painter = rememberImagePainter(imageURL.value)
    val db = FirebaseFirestore.getInstance()
    val userName = user.username
    val dateMade = user.joinDate
    val cleanups = user.totalNumberOfCleanups
    val points = user.score
    val description = user.description
    Log.d("bruh", loggedInUser.username + ", " + event.author)



    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Call the uploadImage function and handle the result in the callback
            uploadImage(it) { downloadUrl ->
                if (downloadUrl != null) {
                    updateProfilePicture(user = user, imageUrl = downloadUrl)
                    imageURL.value = downloadUrl
                    Log.d("ImageUpload", "Event Picture URI: $imageURL")
                } else {
                    Log.e("ImageUpload", "Failed to upload image.")
                }
            }
        }
    }

    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painter,
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {
                        if (loggedInUser.username == user.username) {
                            launcher.launch("image/*")
                        }
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(userName)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // join date
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "Date Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(dateMade)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // events attended
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = "Clock Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Complete $cleanups cleanups")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // points
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Clock Icon",
                tint = Grey
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text("Earned $points points")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "About Me",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .padding(bottom = 10.dp)
        )
        SubText(
            if (description == "") "This user has not set a description yet." else description, Grey
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = if (event.author != "" && loggedInUser.username == event.author && user.username != event.author) Arrangement.SpaceEvenly else Arrangement.End
        ) {
            if (event.author != "" && loggedInUser.username == event.author && user.username != event.author) {
                UnfilledButton(onClick = {
                    val pointsAdded = event.points.toLong()
                    if (markedAsAttendedMsg.value == "Attended?") {
                        markedAsAttendedMsg.value = "Attended!"
                        db.collection("Users")
                            .whereEqualTo("username", user.username)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    val userId = querySnapshot.documents[0].id
                                    db.collection("Users").document(userId)
                                        .update("score", FieldValue.increment(pointsAdded))
                                        .addOnSuccessListener {
                                            Log.d("added points", "Points updated successfully!")
                                        }
                                }
                            }.addOnFailureListener {
                                Log.d("added points", "Points couldn't be saved")
                            }
                    } else if (markedAsAttendedMsg.value == "Attended!") {
                        markedAsAttendedMsg.value = "Attended?"
                        db.collection("Users")
                            .whereEqualTo("username", user.username)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    val userId = querySnapshot.documents[0].id
                                    db.collection("Users").document(userId)
                                        .update("score", FieldValue.increment(pointsAdded * -1))
                                        .addOnSuccessListener {
                                            Log.d("added points", "Points updated successfully!")
                                        }
                                }
                            }.addOnFailureListener {
                                Log.d("added points", "Points couldn't be saved")
                            }
                    }
                }, msg = markedAsAttendedMsg.value, modifierWrapper = Modifier.width(150.dp))
            }
            FilledButton(onClick = {
                if (buttonMsg == "Edit") {
                    navController.navigate("edit_description")
                } else if (buttonMsg == "Friend") {
                    loggedInUser.sendFriendRequest(user,
                        onSuccess = {
                            buttonMsg = "Unfriend" // Update button to "Unfriend"
                        },
                        onFailure = { e ->
                            Log.e("Friend Request", "Error sending request", e)
                        })
                } else {
                    loggedInUser.unFriend(user,
                        onSuccess = {
                            buttonMsg = "Friend" // Update button to "Unfriend"
                        },
                        onFailure = { e ->
                            Log.e("Friend Request", "Error unfriending", e)
                        })
                }
            }, msg = buttonMsg, modifierWrapper = Modifier.width(120.dp))
        }
    }
}

fun updateProfilePicture(user: User, imageUrl: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("Users").whereEqualTo("username", user.username)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val userId = querySnapshot.documents[0].id
                db.collection("Users").document(userId)
                    .update("profilePicture", imageUrl)
                    .addOnSuccessListener {
                        Log.d("edit_profilePicture", "Profile picture updated successfully!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("edit_profilePicture", "Failed to update profile picture", e)
                    }
            } else {
                Log.e("edit_profilePicture", "User not found in the database")
            }
        }.addOnFailureListener { e ->
            Log.e("edit_profilePicture", "Failed to query user", e)
        }
}

fun User.sendFriendRequest(recipient: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("Users")
        .whereEqualTo("username", recipient.username)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val document = documents.documents[0]
                val targetUserRef = document.reference

                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(targetUserRef)
                    val friendRequests = snapshot.get("friendRequests") as? List<String> ?: emptyList()
                    if (!friendRequests.contains(this.username)) {
                        // Add the current user to the recipient's friend requests list
                        val updatedFriendRequests = friendRequests.toMutableList()
                        updatedFriendRequests.add(this.username)

                        transaction.update(targetUserRef, "friendRequests", updatedFriendRequests)
                    } else {
                        throw Exception("Friend request already sent")
                    }
                }.addOnSuccessListener {
                    onSuccess() // Callback to update the button message
                }.addOnFailureListener { e ->
                    onFailure(e) // Handle failure
                }
            } else {
                // Recipient user not found
                onFailure(Exception("Recipient user not found"))
            }
        }
        .addOnFailureListener { e ->
            onFailure(e) // Handle failure
        }
}

fun User.unFriend(userToUnfriend: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    // Retrieve the logged-in user's document reference by username
    firestore.collection("Users")
        .whereEqualTo("username", this.username)
        .get()
        .addOnSuccessListener { loggedInUserDocuments ->
            if (loggedInUserDocuments.isEmpty) {
                onFailure(Exception("Logged-in user not found"))
                return@addOnSuccessListener
            }

            val loggedInUserDoc = loggedInUserDocuments.documents[0]
            val loggedInUserRef = loggedInUserDoc.reference

            // Retrieve the user to unfriend's document reference by username
            firestore.collection("Users")
                .whereEqualTo("username", userToUnfriend.username)
                .get()
                .addOnSuccessListener { userToUnfriendDocuments ->
                    if (userToUnfriendDocuments.isEmpty) {
                        onFailure(Exception("Recipient user not found"))
                        return@addOnSuccessListener
                    }

                    val userToUnfriendDoc = userToUnfriendDocuments.documents[0]
                    val userToUnfriendRef = userToUnfriendDoc.reference

                    // Perform the transaction to update the friends list
                    firestore.runTransaction { transaction ->
                        val snapshotLoggedInUser = transaction.get(loggedInUserRef)
                        val snapshotUserToUnfriend = transaction.get(userToUnfriendRef)

                        // Get friends lists or create an empty list if it doesn't exist
                        val loggedInUserFriends = snapshotLoggedInUser.get("friends") as? MutableList<String> ?: mutableListOf()
                        val userToUnfriendFriends = snapshotUserToUnfriend.get("friends") as? MutableList<String> ?: mutableListOf()

                        // Check if both users are friends
                        if (loggedInUserFriends.contains(userToUnfriend.username) && userToUnfriendFriends.contains(this.username)) {
                            // Remove each other from friends lists
                            loggedInUserFriends.remove(userToUnfriend.username)
                            userToUnfriendFriends.remove(this.username)

                            // Update the friends lists in Firestore
                            transaction.update(loggedInUserRef, "friends", loggedInUserFriends)
                            transaction.update(userToUnfriendRef, "friends", userToUnfriendFriends)
                        } else {
                            throw Exception("Not friends with this user")
                        }
                    }.addOnSuccessListener {
                        onSuccess() // Callback to indicate success
                    }.addOnFailureListener { e ->
                        onFailure(e) // Handle failure
                    }
                }
                .addOnFailureListener { e ->
                    onFailure(e) // Handle failure when fetching the user to unfriend
                }
        }
        .addOnFailureListener { e ->
            onFailure(e) // Handle failure when fetching the logged-in user
        }
}