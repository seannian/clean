package com.example.myapplication

import LinearProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.ForestGreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun LeaderboardScreen(user: User) {
    var topUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var globalProgress by remember { mutableFloatStateOf(0.0f) }
    val db = FirebaseFirestore.getInstance()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("Users")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(100)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }
                topUsers = users

                val totalScore = users.sumOf { it.score.toLong() }
                val maxScore = 100
                globalProgress = totalScore.toFloat() / maxScore.toFloat()
                isLoading = false
                println("Total Score: $totalScore, Max Score: $maxScore, Progress: $globalProgress")
            }
            .addOnFailureListener { e ->
                println("Error fetching data: ${e.message}")
                globalProgress = 0.0f
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Use the calculated globalProgress to dynamically show the percentage
            TitleText("Global Impact Bar (${(globalProgress * 100).toInt()}%)", 16.dp)
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
//        globalProgress = 1.0f
            LinearProgressBar(globalProgress)  // Pass the dynamic value here
            HorizontalDivider(
                color = ForestGreen,
                thickness = 5.dp,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .width(100.dp)
            )
            TitleText("Leaderboard", 16.dp)
            Spacer(modifier = Modifier.padding(bottom = 32.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
}