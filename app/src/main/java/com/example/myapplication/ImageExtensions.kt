package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

@Composable
fun rememberImagePainter(
    imageURL: String,
    placeholderRes: Int = R.drawable.ic_launcher_background,
    errorRes: Int = R.drawable.ic_launcher_background
): Painter {
    return rememberAsyncImagePainter(
        model = imageURL,
        error = painterResource(errorRes), // Fallback image on error
        placeholder = painterResource(placeholderRes) // Placeholder during loading
    )
}