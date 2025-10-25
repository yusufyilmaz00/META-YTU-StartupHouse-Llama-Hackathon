package com.balikllama.xpguiderdemo.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing


@Composable
fun LoginView(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.XL),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.M)
        ) {
            Text(
                text = "Giriş Yap",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChanged,
                label = { Text("E-posta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage != null,
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                label = { Text("Şifre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.errorMessage != null,
                shape = MaterialTheme.shapes.medium
            )

            // Hata mesajı gösterimi (varsa)
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = Spacing.XS)
                )
            }

            Button(
                onClick = onLoginClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "GİRİŞ YAP",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            TextButton(
                onClick = onNavigateToRegister,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Hesabın yok mu? Kayıt Ol",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}