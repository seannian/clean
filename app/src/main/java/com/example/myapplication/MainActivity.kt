package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            androidx.compose.material3.MaterialTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("firebaseLogin") {
            FirebaseLogin()
        }
        composable("navigationDrawer") {
            NavigationDrawer()
        }
        composable("functionTest") {
            FunctionTest(navController = navController)
        }
        composable("createEvent") {
            CreateEvent()
        }
        composable("loginScreen") {
            LoginScreen()
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { navController.navigate("firebaseLogin") }) {
                Text(text = "Go to Firebase Login")
            }
            Button(onClick = { navController.navigate("navigationDrawer") }) {
                Text(text = "Go to Navigation Drawer")
            }
            Button(onClick = { navController.navigate("functionTest") }) {
                Text(text = "Go to Function Test")
            }
            Button(onClick = { navController.navigate("loginScreen") }) {
                Text(text = "Go to Login Screen")
            }
        }
    }
}