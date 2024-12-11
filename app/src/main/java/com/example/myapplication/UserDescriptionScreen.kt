package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun editUserDescription() {

    var description by remember { mutableStateOf("") }

    Column {
        TitleText(
            text = "Thank you for taking the time to describe yourself!",
            fontSize = 40.sp
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.width(96.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("") },
            minLines = 7,
            placeholder = { Text("Give a brief description about yourself") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        FilledButton(
            {},
            msg = "Save",
            modifierWrapper = Modifier.width(96.dp).align(Alignment.End))
    }
}

@Preview
@Composable
fun testUserDesc() {
    MaterialTheme {
        editUserDescription()
    }
}