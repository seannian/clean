// DatabaseExample.kt
package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun DatabaseExample(name: String, onSignOut: () -> Unit) {
    // State variables
    var inputText by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("Hello $name!") }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Greeting Text
        Text(
            text = displayText,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input Field
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter some data") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    isLoading = true
                    val data = hashMapOf(
                        "name" to name,
                        "input" to inputText,
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("userInputs")
                        .add(data)
                        .addOnSuccessListener { documentReference ->
                            isLoading = false
                            feedbackMessage = "Data successfully written with ID: ${documentReference.id}"
                            inputText = ""
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            feedbackMessage = "Error adding document: ${e.message}"
                        }
                } else {
                    feedbackMessage = "Please enter some data before submitting."
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submitting...")
            } else {
                Text("Submit to Firestore")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feedback Message
        if (feedbackMessage.isNotEmpty()) {
            Text(
                text = feedbackMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = if (feedbackMessage.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Out Button
        Button(
            onClick = onSignOut,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatabaseExamplePreview() {
    MyApplicationTheme {
        DatabaseExample(name = "Android", onSignOut = {})
    }
}
