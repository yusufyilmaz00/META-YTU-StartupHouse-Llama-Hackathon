package com.balikllama.b1demo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.balikllama.b1demo.ui.navigation.Routes

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    alwaysShowLabel: Boolean = true
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    fun isSelected(route: String): Boolean {
        val dest = currentDestination ?: return false
        return dest.route == route || dest.hierarchy.any { it.route?.startsWith(route) == true }
    }

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // HOME
        NavigationBarItem(
            selected = isSelected(Routes.HOME),
            onClick = {
                navController.navigate(Routes.HOME) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            },
            icon = {
                Icon(
                    imageVector = if (isSelected(Routes.HOME)) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Go to Home"
                )
            },
            label = { Text("Home", style = MaterialTheme.typography.labelMedium) },
            alwaysShowLabel = alwaysShowLabel,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // CHATBOT
        NavigationBarItem(
            selected = isSelected(Routes.CHATBOT),
            onClick = {
                navController.navigate(Routes.CHATBOT) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            },
            icon = {
                Icon(
                    imageVector = if (isSelected(Routes.CHATBOT)) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search movies"
                )
            },
            label = { Text("Chatbot", style = MaterialTheme.typography.labelMedium) },
            alwaysShowLabel = alwaysShowLabel,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // PROFILE
        NavigationBarItem(
            selected = isSelected(Routes.PROFILE),
            onClick = {
                navController.navigate(Routes.PROFILE) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                }
            },
            icon = {
                Icon(
                    imageVector = if (isSelected(Routes.PROFILE)) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                    contentDescription = "Open Watchlist"
                )
            },
            label = { Text("Profile", style = MaterialTheme.typography.labelMedium) },
            alwaysShowLabel = alwaysShowLabel,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}