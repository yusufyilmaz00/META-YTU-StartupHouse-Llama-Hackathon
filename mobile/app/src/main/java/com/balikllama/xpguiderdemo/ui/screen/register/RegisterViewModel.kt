package com.balikllama.xpguiderdemo.ui.screen.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.auth.RegisterRequest
import com.balikllama.xpguiderdemo.repository.AuthRepository
import com.balikllama.xpguiderdemo.model.auth.Metadata
import com.balikllama.xpguiderdemo.repository.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onRegisterClicked() {
        if (_uiState.value.isLoading) return

        val currentState = _uiState.value
        // Basit validasyon
        if (currentState.name.isBlank() || currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Tüm alanlar doldurulmalıdır.") }
            return
        }
        if (currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Şifreler uyuşmuyor") }
            return
        }
        if (currentState.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Şifre en az 6 karakter olmalı") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val request = RegisterRequest(
                email = currentState.email.trim(),
                password = currentState.password,
                metadata = Metadata(name = currentState.name.trim())
            )

            // AuthRepository'nin ApiResult döndürdüğünü varsayarak
            when (val result = authRepository.register(request)) {
                is ApiResult.Success -> {
                    // BAŞARILI!
                    _uiState.update {
                        it.copy(isLoading = false, registerSuccess = true)
                    }
                }
                is ApiResult.Error -> {
                    // BAŞARISIZ!
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }


    // UI'ın hatayı bir kez gösterdikten sonra temizlemesi için
    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}