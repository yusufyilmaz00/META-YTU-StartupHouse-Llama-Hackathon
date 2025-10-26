package com.balikllama.xpguiderdemo.ui.screen.calculationtest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.components.AppTopBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing
import com.balikllama.xpguiderdemo.ui.navigation.Routes
import com.balikllama.xpguiderdemo.ui.screen.test.TestViewModel

@Composable
fun CalculationTestScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    calculationViewModel: CalculationTestViewModel = hiltViewModel(),
    // Sonuçları göstermek için paylaşılan TestViewModel'i de alıyoruz.
    sharedTestViewModel: TestViewModel
) {
    val uiState by calculationViewModel.uiState.collectAsState()

    // Bu effect, Composable ekrana geldiğinde sadece bir kez çalışır.
    LaunchedEffect(Unit) {
        calculationViewModel.runPredefinedTest()
    }

    // Bu effect, hesaplama tamamlandığında çalışır.
    LaunchedEffect(uiState.isCalculationComplete) {
        if (uiState.isCalculationComplete) {
            // 1. Hesaplanan sonuçları, sonuç ekranının kullanacağı paylaşılan ViewModel'e aktar.
            sharedTestViewModel.setTestResults(uiState.results)

            // 2. Sonuç ekranına yönlendir.
            navController.navigate(Routes.TEST_RESULTS) {
                // Bu test ekranını geri yığınından kaldır ki geri tuşuyla dönülmesin.
                popUpTo(Routes.CALCULATION_TEST) { inclusive = true }
            }
        }
    }

    // Arayüz (Hesaplama sırasında görünecek olan)
    AppTheme {
        Scaffold(
            topBar = { AppTopBar(title = "Hesaplama Testi", creditInfo = "") }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Spacing.M))
                        Text(
                            text = "Önceden tanımlı senaryo hesaplanıyor...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
