package com.balikllama.xpguiderdemo.ui.screen.testresults

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes
import com.balikllama.xpguiderdemo.ui.screen.test.TestViewModel
import com.balikllama.xpguiderdemo.ui.screen.testresult.TestResultView

@Composable
fun TestResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    // NavHost'ta aynı backstack entry'sine bağlı olduğu için
    // Hilt aynı TestViewModel örneğini buraya da verecektir.
    viewModel: TestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // --- LOGLAMA NOKTASI 4: Screen'e gelen UIState'i kontrol et ---
    Log.d("TestResultsScreen", "UIState alındı. Sonuç sayısı: ${uiState.results.size}. Sonuçlar: ${uiState.results}")

    if (uiState.isTestCompleted) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.CHATBOT) {
                // Geri yığınından testle ilgili tüm ekranları temizle
                popUpTo(Routes.TEST_GRAPH) { inclusive = true }
            }
        }
    }

    TestResultView(
        modifier = modifier,
        results = uiState.results,
        onAnalyzeClicked = {
            viewModel.submitTestResults()
        }
    )
}
