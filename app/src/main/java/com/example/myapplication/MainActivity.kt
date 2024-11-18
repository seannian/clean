package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme(dynamicColor = false) {
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
        composable("functionTest") {
            FunctionTest(navController = navController)
        }
        composable("createEvent") {
            CreateEvent(navController)
        }
        composable("loginScreen") {
            LoginScreen()
        }
        composable("signupScreen") {
            SignupScreen()
        }
        composable("friendScreen") {
            FriendsScreen()
        }
        /*        composable("leaderboardScreen") {
                    LeaderboardScreen()
                }*/
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
            Button(onClick = { navController.navigate("functionTest") }) {
                Text(text = "Go to Function Test")
            }
            Button(onClick = { navController.navigate("friendScreen") }) {
                Text(text = "Go to Friends Screen")
            }
            // need user integration
            /*            Button(onClick = { navController.navigate("leaderboardScreen") }) {
                            Text(text = "Go to Leaderboard Screen")
                        }*/
            Button(onClick = { navController.navigate("loginScreen") }) {
                Text(text = "Go to Login Screen")
            }
            Button(onClick = { navController.navigate("signupScreen") }) {
                Text(text = "Go to Signup Screen")
            }
        }
    }
}
