package com.example.myapplication

import LinearProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.ForestGreen
import com.example.myapplication.ui.theme.Gold
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun LeaderboardScreen(user: User) {
    var topUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    // Fetch the top 100 users when the screen is first shown
    LaunchedEffect(Unit) {
        db.collection("Users")
            .orderBy("score", Query.Direction.DESCENDING) // Sort by score in descending order
            .limit(100) // Limit to top 100 users
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { document ->
                    document.toObject(User::class.java) // Convert document to User object
                }
                topUsers = users
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText("Global Impact Bar (60%)", 16.dp)
        LinearProgressBar(0.6f) // Connect to database to show real progress
        HorizontalDivider(
            color = ForestGreen,
            thickness = 5.dp,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .width(100.dp)
        )
        TitleText("Leaderboard", 16.dp)

        // Display users using LazyColumn with extra spacing between users
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Adds spacing between items
        ) {
            items(topUsers.size) { index ->
                val currentUser = topUsers[index]
                LeaderboardUser(
                    medal = index,
                    user = currentUser
                )
            }
        }
    }
}
