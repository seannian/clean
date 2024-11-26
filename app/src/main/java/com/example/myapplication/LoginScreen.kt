package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
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
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.content.Context
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navigateToMainScreen: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText("Log In", 16.dp)

        Spacer(modifier = Modifier.padding(24.dp))

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            maxLines = 1,
            placeholder = { Text("Enter Your Email") }
        )

        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            maxLines = 1,
            placeholder = { Text("Enter Your Password") }
        )

        Spacer(modifier = Modifier.padding(24.dp))

        Button(
            onClick = {
                loginUser(auth, email, password, context, navigateToMainScreen)
            }
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text("Or")

        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = {
            // Navigate to the Sign Up Screen (Implement Sign Up Screen Navigation here)
        }) {
            Text("Sign Up")
        }
    }
}

fun loginUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    navigateToMainScreen: () -> Unit
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    navigateToMainScreen() // Navigate to main screen
                } else {
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    } else {
        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
    }
}