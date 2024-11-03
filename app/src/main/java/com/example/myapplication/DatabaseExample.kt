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
import com.google.firebase.firestore.Query

@Composable
fun DatabaseExample(name: String, onSignOut: () -> Unit) {
    // State variables
    var inputText by remember { mutableStateOf("") }
    var displayText by remember { mutableStateOf("Hello $name!") }
    var isLoading by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var isAddingUsers by remember { mutableStateOf(false) }
    var isQueryingUsers by remember { mutableStateOf(false) }
    var topUsers by remember { mutableStateOf<List<Map<String, Any>>?>(null) }

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

        // Delete All Data Button
        Button(
            onClick = { showDeleteConfirmation = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            enabled = !isDeleting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isDeleting) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Deleting...")
            } else {
                Text("Delete All Data")
            }
        }

        // Confirmation Dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete **all** data? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                            isDeleting = true
                            feedbackMessage = "Deleting all data..."

                            // Define all collections to delete
                            val collectionsToDelete = listOf("userInputs", "Users")

                            // Function to delete all documents in a collection
                            fun deleteCollection(collectionName: String, onComplete: (Boolean, String) -> Unit) {
                                db.collection(collectionName)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        if (querySnapshot.isEmpty) {
                                            onComplete(true, "No documents found in '$collectionName'.")
                                            return@addOnSuccessListener
                                        }

                                        val batch = db.batch()
                                        for (document in querySnapshot.documents) {
                                            batch.delete(document.reference)
                                        }

                                        batch.commit()
                                            .addOnSuccessListener {
                                                onComplete(true, "All documents in '$collectionName' deleted successfully.")
                                            }
                                            .addOnFailureListener { e ->
                                                onComplete(false, "Error deleting '$collectionName': ${e.message}")
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        onComplete(false, "Error retrieving '$collectionName': ${e.message}")
                                    }
                            }

                            // Counter to track completed deletions
                            var completed = 0
                            var hasError = false
                            var errorMessage = ""

                            // Iterate through each collection and delete
                            for (collection in collectionsToDelete) {
                                deleteCollection(collection) { success, message ->
                                    if (!success && !hasError) {
                                        hasError = true
                                        errorMessage = message
                                    }
                                    completed++
                                    if (completed == collectionsToDelete.size) {
                                        isDeleting = false
                                        feedbackMessage = if (hasError) {
                                            "Deletion completed with errors: $errorMessage"
                                        } else {
                                            "All data successfully deleted."
                                        }
                                    }
                                }
                            }

                            // Handle case where there are no collections to delete
                            if (collectionsToDelete.isEmpty()) {
                                isDeleting = false
                                feedbackMessage = "No collections found to delete."
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirmation = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Sample Users Button
        Button(
            onClick = {
                isAddingUsers = true
                val usersCollection = db.collection("Users")
                val batch = db.batch()
                for (i in 1..10) {
                    val username = "User$i"
                    val email = "user$i@example.com"
                    val score = (0..100).random()
                    val userData = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "score" to score
                    )
                    val newUserRef = usersCollection.document()
                    batch.set(newUserRef, userData)
                }
                batch.commit()
                    .addOnSuccessListener {
                        isAddingUsers = false
                        feedbackMessage = "10 sample users added successfully."
                    }
                    .addOnFailureListener { e ->
                        isAddingUsers = false
                        feedbackMessage = "Error adding sample users: ${e.message}"
                    }
            },
            enabled = !isAddingUsers,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isAddingUsers) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adding Users...")
            } else {
                Text("Add Sample Users")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show Top Scoring Users Button
        Button(
            onClick = {
                isQueryingUsers = true
                db.collection("Users")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(5)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        topUsers = querySnapshot.documents.map { it.data ?: emptyMap() }
                        isQueryingUsers = false
                        feedbackMessage = "Top users retrieved successfully."
                    }
                    .addOnFailureListener { e ->
                        isQueryingUsers = false
                        feedbackMessage = "Error querying users: ${e.message}"
                    }
            },
            enabled = !isQueryingUsers,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isQueryingUsers) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Querying Users...")
            } else {
                Text("Show Top Scoring Users")
            }
        }

        // Display Top Scoring Users
        if (topUsers != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Top Scoring Users:", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            topUsers!!.forEach { user ->
                val username = user["username"] as? String ?: "Unknown"
                val score = when (val scoreValue = user["score"]) {
                    is Long -> scoreValue
                    is Int -> scoreValue.toLong()
                    else -> 0L
                }
                Text(
                    text = "Username: $username, Score: $score",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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
        DatabaseExample(name = "John Doe", onSignOut = {})
    }
}
