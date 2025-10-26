package com.balikllama.xpguiderdemo.repository

import android.content.Context
import android.content.SharedPreferences
import com.balikllama.xpguiderdemo.model.auth.LoginResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private companion object {
        // Oturum anahtarları
        const val KEY_USER_ID = "user_id"
        const val KEY_EMAIL = "email"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"

        // Kurulum anahtarları
        const val KEY_INTEREST_SELECTION_COMPLETED = "interest_selection_completed"
        const val KEY_SELECTED_INTEREST_IDS = "selected_interest_ids"
    }

    // --- OTURUM YÖNETİMİ ---

    /**
     * Başarılı bir giriş sonrası tüm oturum bilgilerini kaydeder.
     */
    fun saveAuthTokens(loginResponse: LoginResponse) {
        prefs.edit()
            .putString(KEY_USER_ID, loginResponse.userId)
            .putString(KEY_EMAIL, loginResponse.email)
            .putString(KEY_ACCESS_TOKEN, loginResponse.accessToken)
            .putString(KEY_REFRESH_TOKEN, loginResponse.refreshToken)
            .apply()
    }

    /**
     * Cihazda kayıtlı Access Token'ı getirir.
     * @return Token varsa String, yoksa null döner.
     */
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * Tüm oturum bilgilerini temizler (Çıkış yap).
     */
    fun clearAuthTokens() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_EMAIL)
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .apply()
    }

    // --- İLK KURULUM YÖNETİMİ ---

    fun isInterestSelectionCompleted(): Boolean {
        return prefs.getBoolean(KEY_INTEREST_SELECTION_COMPLETED, false)
    }

    fun saveInterestSelection(interestIds: Set<String>) {
        prefs.edit()
            .putBoolean(KEY_INTEREST_SELECTION_COMPLETED, true)
            .putStringSet(KEY_SELECTED_INTEREST_IDS, interestIds)
            .apply()
    }
}