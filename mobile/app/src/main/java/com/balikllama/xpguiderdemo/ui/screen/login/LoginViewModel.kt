package com.balikllama.xpguiderdemo.ui.screen.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.auth.LoginRequest
import com.balikllama.xpguiderdemo.repository.ApiResult
import com.balikllama.xpguiderdemo.repository.AuthRepository
import com.balikllama.xpguiderdemo.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun login() { // Parametreler artık state'ten alınacak
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // State'ten email ve password'u al
            val loginRequest = LoginRequest(_uiState.value.email, _uiState.value.password)
            val result = authRepository.login(loginRequest)

            when (result) {
                is ApiResult.Success -> {
                    // 1. Token'ları SharedPreferences'e kaydet
                    userPreferencesRepository.saveAuthTokens(result.data)
                    // 2. UI'a başarılı olduğunu bildir
                    _uiState.update {
                        it.copy(isLoading = false, loginSuccess = true)
                    }
                }
                is ApiResult.Error -> {
                    // UI'a hata mesajını bildir
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
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