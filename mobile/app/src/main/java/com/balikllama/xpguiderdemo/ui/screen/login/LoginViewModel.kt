package com.balikllama.xpguiderdemo.ui.screen.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.auth.AuthTokens
import com.balikllama.xpguiderdemo.model.auth.LoginRequest
import com.balikllama.xpguiderdemo.repository.ApiResult
import com.balikllama.xpguiderdemo.repository.AuthRepository
import com.balikllama.xpguiderdemo.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentState = _uiState.value
            val loginRequest = LoginRequest(currentState.email, currentState.password)

            when (val result = authRepository.login(loginRequest)) {
                is ApiResult.Success -> {
                    // 1. Token'ları kaydet
                    val authResult = AuthTokens(accessToken = result.data.accessToken, refreshToken = result.data.refreshToken)
                    userRepository.saveAuthTokens(authResult)

                    // 2. Aktif kullanıcının email'ini kaydet
                    userRepository.saveCurrentUserEmail(currentState.email)

                    // 3. UI'ı güncelle
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    /**
     * Hata mesajı gösterildikten sonra UI state'i temizlemek için kullanılır.
     */
    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}