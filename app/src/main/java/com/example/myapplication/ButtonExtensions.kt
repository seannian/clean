package com.example.myapplication

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BratGreen

@Composable
fun PrimaryButton(content: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = BratGreen,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = content,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}