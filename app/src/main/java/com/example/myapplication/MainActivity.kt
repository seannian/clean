// MainActivity.kt (Complete with Logging)
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log // Import logging
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.google.firebase.FirebaseApp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "MainActivity" // Define tag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "Firebase initialized. Current user: ${auth.currentUser}")

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this matches your Firebase setup
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        Log.d(TAG, "GoogleSignInClient configured.")

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                // Declare currentUser as a mutable state
                var currentUser by remember { mutableStateOf<FirebaseUser?>(auth.currentUser) }

                // Observe authentication state
                DisposableEffect(auth) {
                    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                        currentUser = firebaseAuth.currentUser
                        Log.d(TAG, "Auth state changed. Current user: $currentUser")
                    }
                    auth.addAuthStateListener(authStateListener)
                    onDispose {
                        auth.removeAuthStateListener(authStateListener)
                    }
                }

                // Handle navigation based on authentication state
                LaunchedEffect(currentUser) {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    Log.d(TAG, "LaunchedEffect triggered. Current user: $currentUser, Current route: $currentRoute")
                    if (currentUser != null && currentRoute != "greeting") {
                        navController.navigate("greeting") {
                            popUpTo("signin") { inclusive = true }
                        }
                        Log.d(TAG, "Navigating to 'greeting' route.")
                    } else if (currentUser == null && currentRoute != "signin") {
                        navController.navigate("signin") {
                            popUpTo("greeting") { inclusive = true }
                        }
                        Log.d(TAG, "Navigating to 'signin' route.")
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = if (currentUser == null) "signin" else "greeting"
                ) {
                    composable("signin") {
                        SignInScreen(
                            onSignInClick = { signIn() }
                        )
                        Log.d(TAG, "SignInScreen composable displayed.")
                    }
                    composable("greeting") {
                        Greeting(
                            name = currentUser?.displayName ?: "Android",
                            onSignOut = { signOut() }
                        )
                        Log.d(TAG, "Greeting composable displayed.")
                    }
                }
            }
        }
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Sign-in intent result received.")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d(TAG, "Google Sign-In successful. Account: ${account.email}")
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Handle error
            Log.e(TAG, "Google Sign-In failed.", e)
        }
    }

    private fun signIn() {
        Log.d(TAG, "Initiating Google sign-in.")
        val signInIntent: Intent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "Authenticating with Firebase using Google ID token.")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success, navigation is handled by LaunchedEffect
                    Log.d(TAG, "Firebase sign-in successful.")
                } else {
                    // If sign-in fails, display a message to the user.
                    Log.e(TAG, "Firebase sign-in failed.", task.exception)
                }
            }
    }

    private fun signOut() {
        Log.d(TAG, "Signing out user.")
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d(TAG, "User signed out.")
            // After sign-out, navigation is handled by LaunchedEffect
        }
    }
}
