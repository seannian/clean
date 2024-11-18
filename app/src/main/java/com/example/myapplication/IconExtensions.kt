package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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