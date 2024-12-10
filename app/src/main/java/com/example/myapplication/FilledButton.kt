package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.BratGreen
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.White

@Composable
fun FilledButton(onClick: () -> Unit, msg: String, modifierWrapper: Modifier = Modifier) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = modifierWrapper

    )
    {
        Text(
            text = msg,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun UnfilledButton(onClick: () -> Unit, msg: String, modifierWrapper: Modifier = Modifier) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = White
        ),
        border = BorderStroke(width = 1.dp, color = BratGreen),
        modifier = modifierWrapper
    )
    {
        Text(
            text = msg,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HostCleanupButton(onClick: () -> Unit, msg: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .width(300.dp)
            .height(75.dp)
    )
    {
        Text(
            text = msg,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
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


