package com.balikllama.xpguiderdemo.ui.screen.profile

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileView(
        modifier = modifier,
        uiState = uiState
    )
}
