package com.balikllama.b1demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.b1demo.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Global Credit ViewModel - Tüm ekranlarda kullanılacak
 * Singleton pattern ile tek instance'ı kullanacağız
 */
@HiltViewModel
class CreditViewModel @Inject constructor(
    private val creditRepository: CreditRepository // Inject the repository
) : ViewModel() {

    private val _userCredits = MutableStateFlow(0)
    val userCredits: StateFlow<Int> = _userCredits.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUserCredits()
    }

    /**
     * Kullanıcının kredi bilgisini API'den yükler
     */
    fun loadUserCredits() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // TODO: Backend API çağrısı
                // val response = apiService.getUserCredits(userId)
                // _userCredits.value = response.credits

                // Şimdilik mock data
                _userCredits.value = 150

            } catch (e: Exception) {
                _error.value = "Kredi bilgisi yüklenemedi: ${e.message}"
                _userCredits.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Kredi harcama (test başlatma, özellik kullanma vb.)
     * @param amount Harcanan kredi miktarı
     * @param onSuccess Başarılı olursa çağrılır
     * @param onInsufficientCredits Yetersiz kredi varsa çağrılır
     */
    fun spendCredits(
        amount: Int,
        onSuccess: (() -> Unit)? = null,
        onInsufficientCredits: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val current = _userCredits.value

            if (current >= amount) {
                // TODO: Backend'e kredi harcama isteği gönder
                // apiService.spendCredits(userId, amount)

                _userCredits.value = current - amount
                onSuccess?.invoke()
            } else {
                _error.value = "Yetersiz kredi! Mevcut: $current, Gerekli: $amount"
                onInsufficientCredits?.invoke()
            }
        }
    }

    /**
     * Kredi ekleme (satın alma, bonus vb.)
     * @param amount Eklenecek kredi miktarı
     */
    fun addCredits(amount: Int, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                // TODO: Backend'e kredi ekleme isteği gönder
                // apiService.addCredits(userId, amount)

                _userCredits.value = _userCredits.value + amount
                onSuccess?.invoke()

            } catch (e: Exception) {
                _error.value = "Kredi eklenemedi: ${e.message}"
            }
        }
    }

    /**
     * Manuel kredi güncelleme (backend senkronizasyonu için)
     * @param newCredits Yeni kredi değeri
     */
    fun updateCredits(newCredits: Int) {
        _userCredits.value = newCredits
    }

    /**
     * Hata mesajını temizle
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Kredi yeterli mi kontrolü
     */
    fun hasEnoughCredits(amount: Int): Boolean {
        return _userCredits.value >= amount
    }

    /**
     * Backend ile senkronizasyon (periyodik çağrılabilir)
     */
    fun syncWithBackend() {
        viewModelScope.launch {
            try {
                // TODO: Backend'den güncel kredi bilgisini çek
                // val response = apiService.getUserCredits(userId)
                // _userCredits.value = response.credits

            } catch (e: Exception) {
                // Sessizce hata yönet, kullanıcıyı rahatsız etme
            }
        }
    }
}