package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Grey

@Composable
fun NavDrawerText(content: String, selected: Boolean) {
    Text(
        text = content,
        fontSize = 20.sp,
        modifier = Modifier.padding(16.dp),
        color = if (selected) Color.Black else Grey
    )
}

@Composable
fun TitleText(content: String) {
    Text(
        text = content,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp),
        color = Color.Black
    )
}

@Composable
fun EventTitleText(content: String) {
    Text(
        text = content,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun SubText(content: String, color: Color) {
    Text(
        text = content,
        fontSize = 20.sp,
        color = color
    )
}
