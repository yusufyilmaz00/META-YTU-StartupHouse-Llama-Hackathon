package com.balikllama.xpguiderdemo.ui.screen.register

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    // AppNavGraph'tan gelen navigasyon event'leri
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            Toast.makeText(context, "Kayıt başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_LONG).show()
            onRegisterSuccess() // AppNavGraph'a "başarılı" bilgisini yolla
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.onErrorShown() // Hatanın tekrar gösterilmemesi için ViewModel'i uyar
        }
    }

    RegisterView(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onRegisterClicked = viewModel::onRegisterClicked,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}