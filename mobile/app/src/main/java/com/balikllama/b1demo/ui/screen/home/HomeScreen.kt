package com.balikllama.b1demo.ui.screen.home

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.balikllama.b1demo.ui.navigation.Routes
import com.balikllama.b1demo.viewmodel.CreditViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // HomeView'a state'i ve event lambda'larını iletiyoruz
    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        onAddCredit = { viewModel.addCredit(5) },
        onDecreaseCredit = { viewModel.decreaseCredit(10)},
        onNavigateToDbTest = { navController.navigate( Routes.DATABASE_TEST) }
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel() // Hilt ile ViewModel'i alıyoruz
) {
    // ViewModel'den UI state'ini alıyoruz
    val uiState by viewModel.uiState.collectAsState()

    // HomeView'a state'i ve event lambda'larını iletiyoruz
    HomeView(
        modifier = modifier,
        windowSizeClass = windowSizeClass,
        uiState = uiState,
        onAddCredit = { viewModel.addCredit(5) },
        onDecreaseCredit = { viewModel.decreaseCredit(10)},
        onNavigateToDbTest = {}
    )
}