package com.balikllama.b1demo.ui.screen.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel= viewModel(),
    windowSizeClass: WindowSizeClass
) {
    val credits by viewModel.userCredits.collectAsState()

    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
        creditInfo = credits.toString(),
        onRefreshCredits = { viewModel.loadUserCredits() }
    )
}