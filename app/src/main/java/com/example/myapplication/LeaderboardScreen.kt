package com.example.myapplication

import LinearProgressBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.ForestGreen
import com.example.myapplication.ui.theme.Gold

@Composable
fun LeaderboardScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleText("Global Impact Bar (60%)", 16.dp)
        LinearProgressBar(0.6f) // need to connect to database
        HorizontalDivider(
            color = ForestGreen,
            thickness = 5.dp,
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .width(100.dp)
        )
        TitleText("Leaderboard", 16.dp)
        // LeaderboardUser(true, Gold, user) // need to have db show the top 100 ig
    }
}