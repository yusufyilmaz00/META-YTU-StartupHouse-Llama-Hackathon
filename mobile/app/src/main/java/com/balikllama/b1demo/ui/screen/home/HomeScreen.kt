package com.balikllama.b1demo.ui.screen.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {

    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
    )
}