package com.balikllama.xpguiderdemo.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.balikllama.xpguiderdemo.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Kullanıcının oturum açıp açmadığını kontrol eder.
     */
    fun isLoggedIn(): Boolean {
        // Access token var mı diye bakmak yeterli.
        return userPreferencesRepository.getAccessToken() != null
    }

    /**
     * İlk kurulumu (ilgi alanı seçimi) tamamlayıp tamamlamadığını kontrol eder.
     */
    fun isSetupComplete(): Boolean {
        // SharedPreferences'ten kurulumun tamamlanıp tamamlanmadığını okur.
        return userPreferencesRepository.isInterestSelectionCompleted()
    }
}