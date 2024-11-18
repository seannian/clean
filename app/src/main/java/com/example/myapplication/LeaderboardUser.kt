package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.appcompat.widget.TintInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.ui.theme.BratGreen

@SuppressLint("RestrictedApi")
@Composable
fun LeaderboardUser(medal: Boolean, tint: Color, user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (medal) MedalIcon(tint) else StarIcon()
        SubText(user.username, Color.Black)
        SubText(user.score.toString(), Color.Black)
    }
}