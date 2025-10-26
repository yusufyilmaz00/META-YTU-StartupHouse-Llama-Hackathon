package com.balikllama.xpguiderdemo.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        // Yönlendirme mantığını burada yap
        val destination = when {
            // 1. Önce oturum açık mı diye bak
            viewModel.isLoggedIn() -> {
                // 2. Oturum açıksa, ilgi alanı seçmiş mi diye bak
                if (viewModel.isSetupComplete()) {
                    Routes.HOME
                } else {
                    Routes.INTEREST_SELECTION
                }
            }
            // 3. Oturum açık değilse, direkt login/register akışına yönlendir
            else -> Routes.LOGIN // Veya bir "Welcome" ekranı varsa oraya
        }

        navController.navigate(destination) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }

    // Yönlendirme yapılırken ekranda bir yükleme göstergesi göster
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}