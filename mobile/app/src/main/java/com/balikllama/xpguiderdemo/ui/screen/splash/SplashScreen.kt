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
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        // Küçük bir gecikme ekleyerek splash'in görünmesini sağlayalım
        delay(1000)

        val destination = if (viewModel.isAuthenticated) {
            // Eğer kullanıcı giriş yapmışsa, kurulumu tamamlayıp tamamlamadığını DB'den kontrol et
            if (viewModel.isSetupCompleted()) {
                Routes.HOME
            } else {
                Routes.INTEREST_SELECTION
            }
        } else {
            // Kullanıcı giriş yapmamışsa, ana başlangıç rotasına git
            Routes.REGISTER // Veya AppNavGraph'ın başlangıcı ne ise...
        }

        // Yönlendirmeyi yap ve Splash'i geri yığından kaldır
        navController.navigate(destination) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    // Splash ekranının UI'ı...
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
