package com.balikllama.b1demo.ui.screen.profile

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.balikllama.b1demo.viewmodel.CreditViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    creditViewModel: CreditViewModel
) {

    ProfileView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
    )
}