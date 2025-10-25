package com.balikllama.xpguiderdemo.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditRepository @Inject constructor() {
    private val _userCredits = MutableStateFlow(150) // Başlangıç değeri
    val userCredits: StateFlow<Int> = _userCredits.asStateFlow()

    fun addCredits(amount: Int) {
        _userCredits.value += amount
    }

    fun spendCredits(amount: Int) {
        if (_userCredits.value >= amount) {
            _userCredits.value -= amount
        }
    }
}