package com.balikllama.b1demo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _userCredits = MutableStateFlow(0)
    val userCredits: StateFlow<Int> = _userCredits.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserCredits()
    }

    /**
     * Kullanıcının kredi bilgisini yükler
     */
    fun loadUserCredits() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: API'den kredi bilgisini çek
                // val credits = repository.getUserCredits()

                // Şimdilik mock data
                _userCredits.value = 150

            } catch (e: Exception) {
                // Hata durumunda 0 göster veya error state yönet
                _userCredits.value = 0
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Kredi güncelleme (test tamamlandığında veya satın alma sonrası çağrılabilir)
     */
    fun updateCredits(newCredits: Int) {
        _userCredits.value = newCredits
    }

    /**
     * Kredi harcama (test başlatıldığında)
     */
    fun spendCredits(amount: Int) {
        val current = _userCredits.value
        if (current >= amount) {
            _userCredits.value = current - amount
        }
    }

    /**
     * Kredi ekleme (satın alma sonrası)
     */
    fun addCredits(amount: Int) {
        _userCredits.value = _userCredits.value + amount
    }
}