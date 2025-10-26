package com.balikllama.xpguiderdemo.repository

import android.content.SharedPreferences
import com.balikllama.xpguiderdemo.data.local.dao.UserStatusDao
import com.balikllama.xpguiderdemo.data.local.entity.UserStatusEntity
import com.balikllama.xpguiderdemo.model.auth.AuthTokens
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val prefs: SharedPreferences,
    private val userStatusDao: UserStatusDao // <-- UserStatusDao'yu inject et
) {
    // SharedPreferences'te verileri saklamak için kullanacağımız anahtarlar
    companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_USER_EMAIL = "user_email" // E-posta için tek bir anahtar yeterli
        const val KEY_SETUP_COMPLETED = "setup_completed" // Kurulum durumu için anahtar
    }

    // --- TOKEN İŞLEMLERİ ---

    fun saveAuthTokens(tokens: AuthTokens) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, tokens.accessToken)
            .putString(KEY_REFRESH_TOKEN, tokens.refreshToken)
            .apply()
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    // --- KULLANICI BİLGİLERİ İŞLEMLERİ ---

    fun saveCurrentUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    // `getUserEmail` fonksiyonunu ekliyoruz. ProfileViewModel'deki hatayı bu çözecek.
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }


    // --- KURULUM DURUMU İŞLEMLERİ ---

    fun saveSetupCompleted(isCompleted: Boolean) {
        // Kurulum durumunu kullanıcının e-postasıyla ilişkilendirerek kaydediyoruz.
        // Böylece farklı kullanıcılar için durumlar karışmaz.
        val userSpecificKey = "${getUserEmail()}_${KEY_SETUP_COMPLETED}"
        prefs.edit().putBoolean(userSpecificKey, isCompleted).apply()
    }

    fun isSetupCompleted(): Boolean {
        // Mevcut kullanıcının e-postasına özel anahtarı oluşturuyoruz.
        val userSpecificKey = "${getUserEmail()}_${KEY_SETUP_COMPLETED}"
        // Eğer bu anahtarla bir kayıt varsa onun değerini, yoksa 'false' döndürüyoruz.
        return prefs.getBoolean(userSpecificKey, false)
    }


    // --- TEMİZLİK İŞLEMİ ---

    fun clearUserData() {
        // Çıkış yaparken tüm verileri temizle
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_EMAIL)
            // Not: Kurulum durumu (setup_completed) anahtarları burada temizlenmiyor
            // çünkü her kullanıcıya özel. Bu bir sorun teşkil etmez.
            .apply()
    }
}