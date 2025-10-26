package com.balikllama.xpguiderdemo.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.balikllama.xpguiderdemo.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Kullanıcının oturum açıp açmadığını kontrol eder.
     */
    val isAuthenticated = userRepository.getAccessToken() != null

    // Bu fonksiyon artık asenkron çalışacak
    suspend fun isSetupCompleted(): Boolean {
        return userRepository.isSetupCompleted()
    }
}