package com.balikllama.xpguiderdemo.ui.screen.login

import android.widget.Toast

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Yan Etki 1: Başarı durumunu dinle ve navigasyonu tetikle
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess() // AppNavGraph'a "başarılı" bilgisini yolla
        }
    }

    // Yan Etki 2: Hata mesajlarını dinle ve Toast göster
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            // Hatanın tekrar tekrar gösterilmemesi için ViewModel'de
            // "hata gösterildi" event'i tetiklenebilir.
            // viewModel.onErrorShown()
        }
    }

    LoginView(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginClicked = viewModel::onLoginClicked,
        onNavigateToRegister = onNavigateToRegister,
        modifier = modifier
    )
}