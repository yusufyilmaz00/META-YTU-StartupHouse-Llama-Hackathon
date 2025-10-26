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

    // TODO: "Devam Et" butonuna basıldığında ve ViewModel'deki kaydetme işlemi
    // tamamlandığında Home'a yönlendirecek bir LaunchedEffect eklenecek.
    // Örnek:
     //LaunchedEffect(uiState.isSelectionSaved) {
     //    if (uiState.isSelectionSaved) {
      //       navController.navigate(Routes.HOME) { popUpTo(Routes.INTEREST_SELECTION) { inclusive = true } }
      //   }
    // }

    InterestSelectionView(
        modifier = modifier,
        uiState = uiState,
        onInterestClicked = viewModel::onInterestClicked,
        onContinueClicked = {
            viewModel.onContinueClicked()
            // Geçici olarak direkt yönlendirme yapalım
            navController.navigate(Routes.HOME) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    )
}