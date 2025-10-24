package com.balikllama.b1demo.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.balikllama.b1demo.ui.screen.chatbot.ChatbotScreen
import com.balikllama.b1demo.ui.screen.home.HomeScreen
import com.balikllama.b1demo.ui.screen.profile.ProfileScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier, windowSizeClass: WindowSizeClass) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Routes.HOME
    ) {

        composable(route = Routes.HOME) {
            HomeScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass
            )
        }

        composable(route = Routes.CHATBOT) {
            ChatbotScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass
            )
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen(
                modifier = modifier.fillMaxSize(),
                navController= navController,
                windowSizeClass = windowSizeClass
            )
        }
        // new screens
    }
}