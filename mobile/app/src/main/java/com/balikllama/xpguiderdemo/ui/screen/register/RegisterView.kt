package com.balikllama.xpguiderdemo.ui.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterView(
    uiState: RegisterUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Kayıt Ol", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChanged,
                label = { Text("E-posta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.errorMessage != null
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                label = { Text("Şifre (min 6 karakter)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errorMessage != null
            )

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChanged,
                label = { Text("Şifre Tekrar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errorMessage != null
            )

            Button(
                onClick = onRegisterClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("KAYIT OL")
                }
            }

            TextButton(onClick = onNavigateToLogin) {
                Text("Zaten hesabın var mı? Giriş Yap")
            }
        }
    }
}