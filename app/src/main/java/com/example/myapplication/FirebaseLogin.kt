// FirebaseLogin.kt
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
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun FirebaseLogin() {
    // Declare required variables and Firebase setup
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val activity = context as? Activity

    val clientId = context.getString(R.string.default_web_client_id)  // Ensure this is set in your strings.xml

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
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
                firebaseAuthWithGoogle(auth, account.idToken!!, activity)
            } catch (e: ApiException) {
                Log.e("FirebaseLogin", "Google Sign-In failed.", e)
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
        if (currentUser != null && currentRoute != "databaseExample") {
            navController.navigate("databaseExample") {
                popUpTo("signin") { inclusive = true }
            }
        } else if (currentUser == null && currentRoute != "signin") {
            navController.navigate("signin") {
                popUpTo("databaseExample") { inclusive = true }
            }
        }
    }

    // Set up the navigation host
    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "signin" else "databaseExample"
    ) {
        composable("signin") {
            SignInScreen {
                signIn(googleSignInClient, signInLauncher)
            }
        }
        composable("databaseExample") {
            DatabaseExample(
                name = currentUser?.displayName ?: "User",
                onSignOut = {
                    signOut(auth, googleSignInClient, activity)
                },
                navController = navController // Pass navController here
            )
        }
        // Add the new route for TestCreateUser
        composable("testCreateUser") {
            TestCreateUser(navController = navController)
        }
    }
}

private fun signIn(
    googleSignInClient: GoogleSignInClient,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun signOut(
    auth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    activity: Activity?
) {
    auth.signOut()
    googleSignInClient.signOut().addOnCompleteListener {
        // Sign-out handled, navigation will change automatically
    }
}

private fun firebaseAuthWithGoogle(
    auth: FirebaseAuth,
    idToken: String,
    activity: Activity?
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("FirebaseLogin", "Firebase sign-in successful.")
            } else {
                Log.e("FirebaseLogin", "Firebase sign-in failed.", it.exception)
            }
        }
}

@Composable
fun SignInScreen(onSignInClick: () -> Unit) {
    // UI for sign-in screen
    Column(
        // Add appropriate modifiers for styling
    ) {
        Button(onClick = onSignInClick) {
            Text("Sign In with Google")
        }
    }
}
