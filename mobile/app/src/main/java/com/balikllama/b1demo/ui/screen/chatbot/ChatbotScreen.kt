package com.balikllama.b1demo.ui.screen.chatbot

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.balikllama.b1demo.ui.screen.home.HomeView

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {

    ChatbotView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
    )
}