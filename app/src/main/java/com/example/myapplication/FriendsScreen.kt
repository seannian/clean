package com.example.myapplication

import SearchBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BratGreen

@Composable
fun FriendsScreen() {

    val user = User().apply {
        description = "teehee"
        joinDate = "01/01/2003"
        profilePicture = "" // Assuming an empty string for now
        totalNumberOfCleanups = 0
        email = "" // Assuming an empty string for now
        score = 0
        username = "User"
    }

    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TitleText("Friends", 32.dp)
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search for your friends",
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            UserTile(
                user = user
            )

            Button(
                onClick = { /* Handle unfriend action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Background color
                    contentColor = MaterialTheme.colorScheme.tertiary // Text color
                )
            ) {
                Text("Unfriend")
            }
        }

    }
}