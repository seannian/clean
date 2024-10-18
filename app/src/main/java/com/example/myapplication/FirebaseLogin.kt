package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun FirebaseLogin() {
    // Declare required variables and Firebase setup
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val clientId = context.getString(R.string.default_web_client_id)  // Call getString() here

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)  // Use the clientId here
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    var currentUser by remember { mutableStateOf(auth.currentUser) }
    val navController = rememberNavController()
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(auth, account.idToken!!, context as Activity)
            } catch (e: ApiException) {
                Log.e("MainScreen", "Google Sign-In failed.", e)
            }
        }
    )

    // Listen to auth state changes
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
        }
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    // Handle navigation based on user authentication
    LaunchedEffect(currentUser) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentUser != null && currentRoute != "greeting") {
            navController.navigate("greeting") {
                popUpTo("signin") { inclusive = true }
            }
        } else if (currentUser == null && currentRoute != "signin") {
            navController.navigate("signin") {
                popUpTo("greeting") { inclusive = true }
            }
        }
    }

    // Set up the navigation host
    NavHost(navController = navController, startDestination = if (currentUser == null) "signin" else "drawer") {
        composable("signin") {
            SignInScreen {
                signIn(googleSignInClient, signInLauncher)
            }
        }
        composable("drawer") {
            NavigationDrawer()
        }
    }
}

private fun signIn(googleSignInClient: GoogleSignInClient, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun signOut(auth: FirebaseAuth, googleSignInClient: GoogleSignInClient, context: Activity) {
    auth.signOut()
    googleSignInClient.signOut().addOnCompleteListener(context) {
        // Sign-out handled, navigation will change automatically
    }
}

private fun firebaseAuthWithGoogle(auth: FirebaseAuth, idToken: String, context: Activity) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                Log.d("MainScreen", "Firebase sign-in successful.")
            } else {
                Log.e("MainScreen", "Firebase sign-in failed.", task.exception)
            }
        }
}

@Composable
fun SignInScreen(onSignInClick: () -> Unit) {
    // UI for sign-in screen
    Button(onClick = onSignInClick) {
        Text("Sign In with Google")
    }
}

@Composable
fun GreetingScreen(name: String, onSignOut: () -> Unit) {
    // UI for greeting screen
    Column {
        Text(text = "Hello, $name")
        Button(onClick = onSignOut) {
            Text("Sign Out")
        }
    }
}
