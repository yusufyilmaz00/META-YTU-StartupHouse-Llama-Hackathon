package com.balikllama.b1demo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.balikllama.b1demo.viewmodel.CreditViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val creditViewModel: CreditViewModel
) : ViewModel() {
    // CreditViewModel'den gelen Int akışını HomeUIState akışına dönüştürüyoruz.
    val uiState: StateFlow<HomeUIState> = creditViewModel.credit
        .map { currentCredit ->
            HomeUIState(credit = currentCredit)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUIState() // Başlangıç state'i
        )

    // UI event'lerini yönetmek için fonksiyonlar
    fun addCredit(amount: Int) {
        creditViewModel.addCredit(amount)
    }

    fun decreaseCredit(amount: Int) {
        creditViewModel.decreaseCredit(amount)
    }

}