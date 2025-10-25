package com.balikllama.xpguiderdemo.ui.screen.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TestViewModel = hiltViewModel()
) {
    // ViewModel'den gelen UI durumunu dinle
    val uiState by viewModel.uiState.collectAsState()

    // Testin tamamlanıp tamamlanmadığını kontrol et
    LaunchedEffect(uiState.isTestCompleted) {
        if (uiState.isTestCompleted) {
            // Test bittiğinde, sonuç ekranına yönlendir.
            // Şimdilik Home ekranına geri dönüyoruz.
            // İleride burayı "navController.navigate(Routes.RESULTS)" gibi bir hedefe değiştirebiliriz.
            navController.popBackStack(Routes.HOME, false)
        }
    }

    // UI katmanına (View) state'i ve event lambda'larını (fonksiyonları) iletiyoruz.
    TestView(
        modifier = modifier,
        uiState = uiState,
        // Cevap seçildiğinde ViewModel'deki fonksiyonu çağır.
        onAnswerSelected = { answer ->
            viewModel.onAnswerSelected(answer)
        },
        // Geri butonuna basıldığında ViewModel'deki fonksiyonu çağır.
        onBackPressed = {
            viewModel.goToPreviousQuestion()
        }
    )
}
