// TestCreateUser.kt
package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TestCreateUser(navController: NavController) {
    // State variables for Description and Profile Picture
    var description by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    // Get current user from FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Handle case where user is not logged in
    if (currentUser == null) {
        Text("User not authenticated.")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Changed to Top to accommodate scrolling
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Display Current User Info (Optional)
        Text(
            text = "Creating Profile for: ${currentUser.displayName ?: "Unknown User"}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Description Input Field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture URL Input Field
        OutlinedTextField(
            value = profilePicture,
            onValueChange = { profilePicture = it },
            label = { Text("Profile Picture URL") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("https://example.com/image.png") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submission Button
        Button(
            onClick = {
                if (description.isNotBlank() && profilePicture.isNotBlank()) {
                    isSubmitting = true

                    // Get current timestamp as joinDate
                    val joinDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                    // Create User object
                    val user = User().apply {
                        this.description = description
                        this.profilePicture = profilePicture
                        this.joinDate = joinDate
                        this.email = currentUser.email ?: ""
                        this.username = currentUser.displayName ?: "Unknown"
                        // Initialize other fields as needed
                        this.totalNumberOfCleanups = 0
                        this.score = 0
                    }

                    // Save to Firestore under "Users" collection with UID as document ID
                    db.collection("Users")
                        .document(currentUser.uid)
                        .set(user)
                        .addOnSuccessListener {
                            isSubmitting = false
                            feedbackMessage = "Profile created successfully."
                            // Optionally navigate back or to another screen
                            navController.popBackStack()
                        }
                        .addOnFailureListener { e ->
                            isSubmitting = false
                            feedbackMessage = "Error creating profile: ${e.message}"
                        }
                } else {
                    feedbackMessage = "Please fill in all fields."
                }
            },
            enabled = !isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submitting...")
            } else {
                Text("Create Profile")
            }
        }

        // Feedback Message
        if (feedbackMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = feedbackMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = if (feedbackMessage.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Back Button
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to DatabaseExample")
        }
    }
}
