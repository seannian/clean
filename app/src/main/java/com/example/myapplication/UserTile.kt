package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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

@Composable
fun UserTile(user: User, loggedInUser: User, navController: NavController) {

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
    // need to add friend list to user class
    val imageURL = user.profilePicture
    val painter = rememberImagePainter(imageURL)

    val userName = user.username
    val dateMade = user.joinDate
    val cleanups = user.totalNumberOfCleanups
    val points = user.score
    val description = user.description

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
            horizontalArrangement = Arrangement.End
        ) {
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

    // Get the document reference of the user to unfriend
    firestore.collection("Users")
        .whereEqualTo("username", userToUnfriend.username)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                onFailure(Exception("Recipient user not found"))
                return@addOnSuccessListener
            }

            val document = documents.documents[0]
            val targetUserRef = document.reference
            val loggedInUserRef = firestore.collection("Users").document(this.username)

            firestore.runTransaction { transaction ->
                val snapshotLoggedInUser = transaction.get(loggedInUserRef)
                val snapshotUserToUnfriend = transaction.get(targetUserRef)

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
                    transaction.update(targetUserRef, "friends", userToUnfriendFriends)
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
            onFailure(e) // Handle failure when fetching the user
        }
}
