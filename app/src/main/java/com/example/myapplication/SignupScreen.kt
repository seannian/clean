package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController

@Composable
fun SignupScreen(navigateToMainScreen: () -> Unit) {
    // Initialize Firebase Auth
    val auth = FirebaseAuth.getInstance()

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Coroutine scope for asynchronous tasks
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Added padding for better layout
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText("Sign Up", 24.sp) // Updated to 24.sp for default title size

        Spacer(modifier = Modifier.height(24.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            placeholder = { Text("Enter Your Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            placeholder = { Text("Enter Your Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            placeholder = { Text("Re-enter Your Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button
        Button(
            onClick = {
                // Clear previous messages
                errorMessage = null

                // Input validation
                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    errorMessage = "All fields are required."
                    return@Button
                }

                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match."
                    return@Button
                }

                // Proceed with sign-up
                isLoading = true
                coroutineScope.launch {
                    try {
                        // Create user with email and password
                        auth.createUserWithEmailAndPassword(email, password).await()
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            val uid = currentUser.uid
                            val db = FirebaseFirestore.getInstance()

                            // Check if user exists in Firestore
                            val userDocument = db.collection("Users").document(uid).get().await()
                            if (userDocument.exists()) {
                                // User already exists in Firestore
                                isLoading = false
                                Toast.makeText(context, "Account created successfully.", Toast.LENGTH_SHORT).show()
                                navigateToMainScreen()
                            } else {
                                // User does not exist in Firestore, create new user document
                                val joinDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                                val user = User().apply {
                                    this.email = currentUser.email ?: ""
                                    this.username = currentUser.email?.substringBefore("@") ?: "Unknown"
                                    this.joinDate = joinDate
                                    // Initialize other fields as needed
                                    this.description = ""
                                    this.profilePicture = ""
                                    this.totalNumberOfCleanups = 0
                                    this.score = 0
                                }
                                // Save to Firestore
                                db.collection("Users").document(uid).set(user).await()
                                isLoading = false
                                Toast.makeText(context, "Account created successfully.", Toast.LENGTH_SHORT).show()
                                navigateToMainScreen()
                            }
                        } else {
                            isLoading = false
                            errorMessage = "Failed to get current user."
                        }
                    } catch (e: Exception) {
                        isLoading = false
                        errorMessage = e.localizedMessage ?: "Sign Up failed."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Sign Up", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display error message
        errorMessage?.let { msg ->
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TitleText(text: String, fontSize: TextUnit = 24.sp) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall.copy(fontSize = fontSize)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    MyApplicationTheme(dynamicColor = false) {
        SignupScreen(navigateToMainScreen = {})
    }
}