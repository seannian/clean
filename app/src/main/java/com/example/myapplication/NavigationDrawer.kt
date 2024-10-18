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
import androidx.compose.material3.NavigationDrawerItemColors
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

enum class Page() {
    Leaderboard, Events, My_Events, Past_Events, Friends
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer() {
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
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Drawer", tint = BratGreen)
                    }
                }
                NavigationDrawerItem(
                    label = { NavDrawerText("Leaderboard", if (currentPage == Page.Leaderboard) true else false) },
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
                    label = { NavDrawerText("Events", if (currentPage == Page.Events) true else false) },
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
                    label = { NavDrawerText("My Events", if (currentPage == Page.My_Events) true else false) },
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
                    label = { NavDrawerText("Past Events", if (currentPage == Page.Past_Events) true else false) },
                    selected = if (currentPage == Page.Past_Events) true else false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        currentPage = Page.Past_Events
                        navController.navigate("my_events")
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
                NavigationDrawerItem(
                    label = { NavDrawerText("Friends", if (currentPage == Page.Friends) true else false) },
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
                    label = { NavDrawerText("Log out", false) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent
                    )
                )
            }
        }
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
                    composable("leaderboard") { LeaderboardScreen() }
                    composable("events") { EventsScreen() }
                    composable("my_events") { MyEventsScreen() }
                    composable("past_events") { MyEventsScreen() }
                    composable("friends") { FriendsScreen() }
                }
            }
        )
    }
}