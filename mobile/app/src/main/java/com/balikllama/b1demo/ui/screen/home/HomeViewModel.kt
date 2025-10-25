package com.balikllama.b1demo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _welcomeMessage = MutableStateFlow("Hoş geldiniz!")
    val welcomeMessage: StateFlow<String> = _welcomeMessage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // TODO: Home sayfası için gerekli verileri yükle
                // - Son aktiviteler
                // - Önerilen testler
                // - İstatistikler vb.

                _welcomeMessage.value = "Hoş geldiniz!"

            } catch (e: Exception) {
                _welcomeMessage.value = "Bir hata oluştu"
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /**
     * Home verilerini yenile
     */
    fun refreshHomeData() {
        loadHomeData()
    }
}