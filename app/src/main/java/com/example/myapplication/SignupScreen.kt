package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.checkerframework.checker.units.qual.C

@Composable
fun SignupScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText("Sign Up", 16.dp)

        Spacer(modifier = Modifier.padding(24.dp))

        var username by remember { mutableStateOf("") }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            maxLines = 1,
            placeholder = { Text("Enter Your Username") }
        )

        Spacer(modifier = Modifier.padding(24.dp))

        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            maxLines = 1,
            placeholder = { Text("Enter Your Password") }
        )

        Spacer(modifier = Modifier.padding(24.dp))

        var confirmPassword by remember { mutableStateOf("") }

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            maxLines = 1,
            placeholder = { Text("Enter Your Password") }
        )

        Spacer(modifier = Modifier.padding(24.dp))

        UnfilledButton({}, "Sign Up")


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    MyApplicationTheme(dynamicColor = false) {
        SignupScreen()
    }
}

