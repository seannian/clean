package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.BratGreen
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.White

@Composable
fun FilledButton(onClick: () -> Unit, msg: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    )
    {
        Text(
            text = msg,
        )
    }
}

@Composable
fun UnfilledButton(onClick: () -> Unit, msg: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = White
        ),
        border = BorderStroke(width = 1.dp, color = BratGreen)
    )
    {
        Text(
            text = msg
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonTest() {
    MyApplicationTheme {
        FilledButton({}, "test")
    }
}


