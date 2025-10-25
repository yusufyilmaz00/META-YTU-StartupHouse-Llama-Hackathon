package com.balikllama.xpguiderdemo.ui.screen.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.auth.LoginRequest
import com.balikllama.xpguiderdemo.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLoginClicked() {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val request = LoginRequest(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                val response = authRepository.login(request)

                // BAŞARILI!
                // TODO: Dönen 'response.accessToken'i DataStore'a kaydet.
                // Bu, "Sonraki Adım"dır.

                _uiState.update {
                    it.copy(isLoading = false, loginSuccess = true)
                }

            } catch (e: HttpException) {
                // API'den gelen 401 (Unauthorized) gibi hatalar
                val errorBody = e.response()?.errorBody()?.string()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = errorBody ?: "Giriş hatası")
                }
            } catch (e: Exception) {
                // İnternet yok, vs.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Bilinmeyen hata")
                }
            }
        }
    }
}