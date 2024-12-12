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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController

@Composable
fun SignupScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText("Sign Up", 16.dp)

        Spacer(modifier = Modifier.padding(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            maxLines = 1,
            placeholder = { Text("Enter Your Email") }
        )

        Spacer(modifier = Modifier.padding(16.dp))

        PasswordOutlinedTextField(
            password = password,
            onPasswordChange = { password = it },
            isConfirm = false
        )

        Spacer(modifier = Modifier.padding(16.dp))

        PasswordOutlinedTextField(
            password = confirmPassword,
            onPasswordChange = { confirmPassword = it },
            isConfirm = true
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = {
                errorMessage = null
                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    errorMessage = "All fields are required."
                    return@Button
                }

                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match."
                    return@Button
                }

                isLoading = true
                coroutineScope.launch {
                    try {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            val uid = currentUser.uid
                            val db = FirebaseFirestore.getInstance()
                            val userDocument = db.collection("Users").document(uid).get().await()
                            if (userDocument.exists()) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "This user already exists. Please log in.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val joinDate = SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss",
                                    Locale.getDefault()
                                ).format(Date())
                                val user = User().apply {
                                    this.email = currentUser.email ?: ""
                                    this.username =
                                        currentUser.email?.substringBefore("@") ?: "Unknown"
                                    this.joinDate = joinDate
                                    this.description = ""
                                    this.profilePicture = ""
                                    this.totalNumberOfCleanups = 0
                                    this.score = 0
                                }
                                db.collection("Users").document(uid).set(user).await()
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Account created successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("navigationDrawer")
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

        Spacer(modifier = Modifier.padding(8.dp))

        Text("Or")

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            navController.navigate("loginScreen")
        }) {
            Text("Login", fontSize = 16.sp)
        }

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

@Composable
fun PasswordOutlinedTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isConfirm: Boolean
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        maxLines = 1,
        label = { if (isConfirm) Text("ConfirmPassword") else Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (passwordVisible) VisibilityIcon(onClick = {
                passwordVisible = false
            }) else VisibilityCancelIcon(onClick = {
                passwordVisible = true
            })
        }
    )
}