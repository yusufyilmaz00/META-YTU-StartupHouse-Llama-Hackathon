package com.balikllama.xpguiderdemo.ui.screen.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.auth.RegisterRequest
import com.balikllama.xpguiderdemo.repository.AuthRepository
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

        // Basit validasyon
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Şifreler uyuşmuyor") }
            return
        }

        if (_uiState.value.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Şifre en az 6 karakter olmalı") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                val response = authRepository.register(request)

                // BAŞARILI!
                _uiState.update {
                    it.copy(isLoading = false, registerSuccess = true)
                }

            } catch (e: HttpException) {
                // API'den gelen 400 (örn: "User already registered") hatası
                val errorBody = e.response()?.errorBody()?.string()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = errorBody ?: "Kayıt hatası")
                }
            } catch (e: Exception) {
                // İnternet yok, vs.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Bilinmeyen hata")
                }
            }
        }
    }

    // UI'ın hatayı bir kez gösterdikten sonra temizlemesi için
    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}