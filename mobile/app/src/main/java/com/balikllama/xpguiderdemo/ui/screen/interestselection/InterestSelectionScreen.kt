package com.balikllama.xpguiderdemo.ui.screen.interestselection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes

@Composable
fun InterestSelectionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: InterestSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- 1. YENİ BÖLÜM: YÖNLENDİRME MANTIĞI ---
    LaunchedEffect(uiState.isSelectionSaved) {
        // isSelectionSaved durumu 'true' olduğunda bu blok çalışır.
        if (uiState.isSelectionSaved) {
            // Home ekranına git ve bu ekranı geri yığınından (backstack) kaldır.
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.INTEREST_SELECTION) { inclusive = true }
            }
        }
    }
    // --- YENİ BÖLÜM SONU ---

    InterestSelectionView(
        modifier = modifier,
        uiState = uiState,
        onInterestClicked = viewModel::onInterestClicked,
        // Butona tıklandığında ViewModel'deki yeni fonksiyonu çağır.
        onContinueClicked = viewModel::onContinueClicked
    )
}