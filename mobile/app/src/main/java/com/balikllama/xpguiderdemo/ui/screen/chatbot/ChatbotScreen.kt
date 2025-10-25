package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    creditViewModel: CreditViewModel
) {

    ChatbotView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
    )
}