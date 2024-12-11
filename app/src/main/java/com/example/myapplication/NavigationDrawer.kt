package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.myapplication.ui.theme.BratGreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson

enum class Page() {
    Leaderboard, Events, My_Events, Past_Events, Friends, Profile
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer() {
    val gson = Gson()
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(auth.currentUser) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        user = document.toObject(User::class.java)
                        Log.d("Firestore", "User retrieved: $user")
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "get failed with ", exception)
                }
        } else {
            Log.d("Auth", "No authenticated user found")
        }
    }

    val navController = rememberNavController()
    var currentPage by remember { mutableStateOf(Page.Events) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        scope.launch { drawerState.close() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Drawer",
                            tint = BratGreen
                        )
                    }
                }
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "Leaderboard",
                            if (currentPage == Page.Leaderboard) true else false
                        )
                    },
                    selected = if (currentPage == Page.Leaderboard) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Leaderboard
                        navController.navigate("leaderboard")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "Events",
                            if (currentPage == Page.Events) true else false
                        )
                    },
                    selected = if (currentPage == Page.Events) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Events
                        navController.navigate("events")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "My Events",
                            if (currentPage == Page.My_Events) true else false
                        )
                    },
                    selected = if (currentPage == Page.My_Events) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.My_Events
                        navController.navigate("my_events")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "Past Events",
                            if (currentPage == Page.Past_Events) true else false
                        )
                    },
                    selected = if (currentPage == Page.Past_Events) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Past_Events
                        navController.navigate("past_events")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "Friends",
                            if (currentPage == Page.Friends) true else false
                        )
                    },
                    selected = if (currentPage == Page.Friends) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Friends
                        navController.navigate("friends")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = {
                        NavDrawerText(
                            "Profile",
                            if (currentPage == Page.Profile) true else false
                        )
                    },
                    selected = if (currentPage == Page.Profile) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Profile
                        navController.navigate("profile")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = { NavDrawerText("Log out", false) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        auth.signOut()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }

                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
            }
        },
        gesturesEnabled = false
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.cs_160_project_logo),
                            contentDescription = "Clean our Community Logo"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                            Log.d("Drawer", "Menu icon clicked")
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "events", // Set initial screen
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("loginScreen") {
                        LoginScreen(navController)
                    }
                    composable("leaderboard") {
                        user?.let { LeaderboardScreen(it) }
                    }
                    composable("events") { EventsScreen(navController) }
                    composable("navigationDrawer") {
                        MainActivity()
                    }
                    composable("my_events") {
                        user?.let { MyEventsScreen(it, true, navController) }
                    }
                    composable("past_events") {
                        user?.let { MyEventsScreen(it, false, navController) }
                    }
                    composable("friends") { FriendsScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                    composable("edit_description") { EditUserDescription(navController) }
                    composable(
                        "create_events/{title}"
                    ) { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title")
                        user?.let { CreateEvent(it, navController, title) }
                    }
                    composable(
                        "attendee_screen/{authorName}/{attendeesUsernames}",
                    ) { backStackEntry ->
                        val authorName = backStackEntry.arguments?.getString("authorName")
                        val attendeesString =
                            backStackEntry.arguments?.getString("attendeesUsernames")
                        val attendeesUsernames = attendeesString?.split(",") ?: emptyList()
                        if (authorName != null) {
                            Log.d("hello?", authorName)
                        } else {
                            Log.d("it was null", "bruh")
                        }
                        if (authorName != null) {
                            AttendeeScreen(
                                authorName = authorName,
                                attendeesUsernames = attendeesUsernames,
                                navController = navController
                            )
                        }
                    }
                }
            }
        )
    }
}