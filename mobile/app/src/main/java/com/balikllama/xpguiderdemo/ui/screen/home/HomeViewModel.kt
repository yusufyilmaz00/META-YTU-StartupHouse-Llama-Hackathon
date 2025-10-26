package com.balikllama.xpguiderdemo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val creditRepository: CreditRepository
) : ViewModel() {
    val uiState: StateFlow<HomeUIState> = creditRepository.userCredits
        .map { currentCredit ->
            HomeUIState(credit = currentCredit)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState()
        )

    fun addCredit(amount: Int) {
        creditRepository.addCredits(amount)
    }

    fun decreaseCredit(amount: Int) {
        creditRepository.spendCredits(amount)
    }
}