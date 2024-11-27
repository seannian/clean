package com.example.myapplication

import LinearProgressBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.ForestGreen
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun LeaderboardScreen(user: User) {
    var topUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

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
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText("Global Impact Bar (60%)", 16.dp)
        LinearProgressBar(0.6f)
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
