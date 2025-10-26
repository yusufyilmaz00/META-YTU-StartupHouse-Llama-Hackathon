package com.balikllama.xpguiderdemo.ui.screen.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TestViewModel
) {
    // ViewModel'den gelen UI durumunu dinle
    val uiState by viewModel.uiState.collectAsState()

    // --- YÖNLENDİRME MANTIĞI ---
    // isTestCompleted artık SADECE yönlendirme için kullanılacak.
    // submitTestResults() çağrıldıktan sonra true olacak.
    if (uiState.isTestCompleted) {
        // LaunchedEffect kullanarak bu Composable'ın recomposition döngüsüne girmesini engelle.
        LaunchedEffect(Unit) {
            navController.navigate(Routes.CHATBOT) {
                // İsteğe bağlı: Test ekranlarını geri yığınından temizle
                popUpTo(Routes.TEST_GRAPH) { inclusive = true }
            }
        }
    }

    // UI katmanına (View) state'i ve event lambda'larını (fonksiyonları) iletiyoruz.
    TestView(
        modifier = modifier,
        uiState = uiState,
        // Cevap seçildiğinde ViewModel'deki fonksiyonu çağır.
        onAnswerSelected = viewModel::onAnswerSelected,
        onBackPressed = viewModel::goToPreviousQuestion,
        onSubmitClicked = viewModel::submitTestResults // Butona basıldığında ViewModel'i çağır
    )
}
