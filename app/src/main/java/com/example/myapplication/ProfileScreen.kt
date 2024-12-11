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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

            UserTile(
                user = user.value,
                loggedInUser = user.value,
                navController = navController,
                event = Event()
            )


            TitleText("Friend Requests", 0.dp)
            friendRequest(user.value)
        }
    }

}

@Composable
fun friendRequest(user: User) {
    // Info Date

    val imageURL = user.profilePicture
    val painter = rememberImagePainter(imageURL)

    val userName = user.username
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(120.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            "$userName sent you a friend request!",
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
        )
        {
            FilledButton(
                {},
                msg = "Accept",
                modifierWrapper = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 5.dp))
            UnfilledButton(
                {},
                msg = "Decline",
                modifierWrapper = Modifier.height(40.dp)
            )
        }
    }
}