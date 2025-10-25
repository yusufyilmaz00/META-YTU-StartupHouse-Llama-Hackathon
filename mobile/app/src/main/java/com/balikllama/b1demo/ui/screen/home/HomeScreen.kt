package com.balikllama.b1demo.ui.screen.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.balikllama.b1demo.viewmodel.CreditViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel= viewModel(),
    windowSizeClass: WindowSizeClass,
    creditViewModel: CreditViewModel,
    homeViewModel: HomeViewModel = viewModel()
) {
    val credits by creditViewModel.userCredits.collectAsState()
    val isLoading by creditViewModel.isLoading.collectAsState()

    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
        creditInfo = if (isLoading) "..." else credits.toString(),
        onRefreshCredits = { creditViewModel.loadUserCredits() }
    )
}