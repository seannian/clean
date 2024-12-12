package com.example.myapplication

import SearchBar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.BratGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FriendsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val loggedInUser = remember { mutableStateOf(User()) }
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        loggedInUser.value = document.toObject(User::class.java)!!
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

    val user = User().apply {
        description = "teehee"
        joinDate = "01/01/2003"
        profilePicture = ""
        totalNumberOfCleanups = 0
        email = ""
        score = 0
        username = "User"
    }

    var searchQuery by remember { mutableStateOf("") }

    val filteredFriends = loggedInUser.value.friends.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            TitleText("Friends", 32.dp)
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholderText = "Search for your friends",
            )
        }
        if (filteredFriends.isNotEmpty()) {
            items(filteredFriends) { friendUsername ->
                val friendUser = User(username = friendUsername)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    UserTile(user = friendUser, loggedInUser = loggedInUser.value, navController = navController, event = Event())
                }
            }
        } else {
            item {
                Text("No friends found matching your search.", modifier = Modifier.padding(16.dp), color = Color.Gray)
            }
        }
    }
}
