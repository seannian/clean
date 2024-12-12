package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = remember { mutableStateOf(User()) }

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        user.value = document.toObject(User::class.java)!!
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        item {
            TitleText("My Profile", 0.dp)
        }
        item {
            UserTile(user = user.value, loggedInUser = user.value, navController = navController, event = Event())
        }
        item {
            TitleText("Friend Requests", 0.dp)
        }
        items(user.value.friendRequests) { friendRequestUsername ->
            friendRequest(friendRequestUsername, user)
        }
    }
}

@Composable
fun friendRequest(friendRequestUsername: String, user: MutableState<User>) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(120.dp)
    ) {
        Image(
            painter = rememberImagePainter("https://via.placeholder.com/150"),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            "$friendRequestUsername sent you a friend request!",
            minLines = 2,
            maxLines = 2,
            modifier = Modifier.width(140.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .width(100.dp)
        ) {
            FilledButton(
                onClick = { acceptFriendRequest(friendRequestUsername, user, db, auth) },
                msg = "Accept",
                modifierWrapper = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 5.dp))
            UnfilledButton(
                onClick = { declineFriendRequest(friendRequestUsername, user, db, auth) },
                msg = "Decline",
                modifierWrapper = Modifier.height(40.dp)
            )
        }
    }
}

private fun acceptFriendRequest(
    friendRequestUsername: String,
    user: MutableState<User>,
    db: FirebaseFirestore,
    auth: FirebaseAuth
) {
    val currentUserId = auth.currentUser?.uid ?: return
    val userRef = db.collection("Users").document(currentUserId)

    val friendRefQuery = db.collection("Users").whereEqualTo("username", friendRequestUsername).get()
    friendRefQuery.addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
            val friendDoc = querySnapshot.documents[0]
            val friendRef = friendDoc.reference

            db.runTransaction { transaction ->
                val userSnapshot = transaction.get(userRef)
                val friendSnapshot = transaction.get(friendRef)

                val currentFriendRequests = userSnapshot.get("friendRequests") as? List<String> ?: emptyList()
                val currentUserFriends = userSnapshot.get("friends") as? List<String> ?: emptyList()
                val currentFriendFriends = friendSnapshot.get("friends") as? List<String> ?: emptyList()

                val updatedFriendRequests = currentFriendRequests.toMutableList().apply {
                    remove(friendRequestUsername)
                }
                val updatedUserFriends = currentUserFriends.toMutableList().apply {
                    add(friendRequestUsername)
                }
                val updatedFriendFriends = currentFriendFriends.toMutableList().apply {
                    add(user.value.username)
                }

                transaction.update(userRef, "friendRequests", updatedFriendRequests)
                transaction.update(userRef, "friends", updatedUserFriends)
                transaction.update(friendRef, "friends", updatedFriendFriends)
            }.addOnSuccessListener {
                Log.d("Firestore", "Friend request accepted successfully")

                val updatedRequests = user.value.friendRequests.toMutableList()
                updatedRequests.remove(friendRequestUsername)

                val updatedFriends = user.value.friends.toMutableList()
                updatedFriends.add(friendRequestUsername)

                user.value = User(
                    description = user.value.description,
                    joinDate = user.value.joinDate,
                    profilePicture = user.value.profilePicture,
                    totalNumberOfCleanups = user.value.totalNumberOfCleanups,
                    email = user.value.email,
                    score = user.value.score,
                    username = user.value.username,
                    friends = updatedFriends,
                    friendRequests = updatedRequests
                )
            }.addOnFailureListener { e ->
                Log.e("Firestore", "Error accepting friend request", e)
            }
        } else {
            Log.e("Firestore", "Friend document not found for username: $friendRequestUsername")
        }
    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error querying friend document", e)
    }
}

private fun declineFriendRequest(
    friendRequestUsername: String,
    user: MutableState<User>,
    db: FirebaseFirestore,
    auth: FirebaseAuth
) {
    val currentUserId = auth.currentUser?.uid ?: return
    val userRef = db.collection("Users").document(currentUserId)

    db.runTransaction { transaction ->
        val userSnapshot = transaction.get(userRef)
        val updatedFriendRequests = (userSnapshot.get("friendRequests") as? MutableList<String>)
            ?.apply { remove(friendRequestUsername) } ?: mutableListOf()

        transaction.update(userRef, "friendRequests", updatedFriendRequests)
    }.addOnSuccessListener {
        Log.d("Firestore", "Friend request declined successfully")

        val updatedRequests = user.value.friendRequests.toMutableList()
        updatedRequests.remove(friendRequestUsername)

        user.value = User(
            description = user.value.description,
            joinDate = user.value.joinDate,
            profilePicture = user.value.profilePicture,
            totalNumberOfCleanups = user.value.totalNumberOfCleanups,
            email = user.value.email,
            score = user.value.score,
            username = user.value.username,
            friends = user.value.friends,
            friendRequests = updatedRequests
        )
    }.addOnFailureListener {
        Log.e("Firestore", "Error declining friend request", it)
    }
}