package com.balikllama.xpguiderdemo.ui.screen.login


import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit
) {
    // ViewModel'den UI state'ini al
    val uiState by viewModel.uiState.collectAsState()

    // Login işlemi başarılı olduğunda yönlendirmeyi yap
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            // Splash'e yönlendirerek doğru akışın (ilgi alanı veya home) seçilmesini sağla
            navController.navigate(Routes.SPLASH) {
                popUpTo(Routes.LOGIN) { inclusive = true }            }
        }
    }

    // Yeni LoginView'a state'i ve event'leri bağla
    LoginView(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onLoginClicked = viewModel::login,
        onNavigateToRegister = onNavigateToRegister
    )
}