package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector?) {
    object Login : Screen("login", "Login", null)
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Search : Screen("search", "Busca", Icons.Filled.Search)
    object Library : Screen("library", "Biblioteca", Icons.Filled.LibraryMusic)
    object Profile : Screen("profile", "Perfil", Icons.Filled.Person)
    object Settings : Screen("settings", "Settings", null)
    object Player : Screen("player", "Now Playing", null)
}

val bottomNavItems = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Profile)
val bottomNavRoutes = bottomNavItems.map { it.route }.toSet()

@Composable
fun AppNavigation(
    darkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (bottomNavRoutes.contains(currentRoute)) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = com.example.ui.theme.NeonPink,
                                selectedTextColor = com.example.ui.theme.NeonPink,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(onNavigateToPlayer = { navController.navigate(Screen.Player.route) })
            }
            composable(Screen.Search.route) {
                SearchScreen()
            }
            composable(Screen.Library.route) {
                LibraryScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSettingsClick = { navController.navigate(Screen.Settings.route) },
                    onLogoutClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(darkTheme = darkTheme, onThemeChanged = onThemeChanged, onBack = { navController.popBackStack() })
            }
            composable(Screen.Player.route) {
                PlayerScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
