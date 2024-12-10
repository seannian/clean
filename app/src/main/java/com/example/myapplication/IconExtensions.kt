package com.example.myapplication

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BratGreen
import com.example.myapplication.ui.theme.Grey

@Composable
fun ClockIcon() {
    val svgPainter = painterResource(id = R.drawable.clock_icon)

    Icon(
        painter = svgPainter,
        contentDescription = "Clock Icon",
        modifier = Modifier
            .padding(3.dp)
            .size(20.dp),
        tint = Grey
    )
}

@Composable
fun MedalIcon(tint: Color) {
    val svgPainter = painterResource(id = R.drawable.medal)

    Icon(
        painter = svgPainter,
        contentDescription = "Medal Icon",
        modifier = Modifier
            .padding(3.dp)
            .size(20.dp),
        tint = tint
    )
}

@Composable
fun StarIcon() {
    val svgPainter = painterResource(id = R.drawable.star)

    Icon(
        painter = svgPainter,
        contentDescription = "Star Icon",
        modifier = Modifier
            .padding(3.dp)
            .size(20.dp),
        tint = BratGreen
    )
}

@Composable
fun RefreshIcon(onClick: () -> Unit) {
    val svgPainter = painterResource(id = R.drawable.refresh_icon)

    Button(modifier = Modifier
        .padding(top = 48.dp, bottom = 32.dp, end = 32.dp)
        .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Grey
        ),
        elevation = null,
        onClick = { onClick() }) {
        Icon(
            modifier = Modifier
                .size(25.dp),
            painter = svgPainter,
            contentDescription = "Refresh Icon",
            tint = Grey,
        )
    }
}