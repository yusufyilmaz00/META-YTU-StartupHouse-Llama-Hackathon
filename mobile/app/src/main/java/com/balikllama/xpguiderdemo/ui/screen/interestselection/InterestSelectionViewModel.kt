package com.balikllama.xpguiderdemo.ui.screen.interestselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.repository.InterestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toMutableSet

@HiltViewModel
class InterestSelectionViewModel @Inject constructor(
    private val interestRepository: InterestRepository
    // TODO: Seçimleri kaydetmek için DataStore Repository'si buraya eklenecek
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestSelectionUIState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllInterests()
    }

    private fun loadAllInterests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val interests = interestRepository.getAllInterests().first()
            _uiState.update { it.copy(isLoading = false, allInterests = interests) }
        }
    }

    /**
     * Kullanıcı bir ilgi alanına tıkladığında çağrılır.
     * @param interestId Tıklanan ilgi alanının ID'si.
     */
    fun onInterestClicked(interestId: String) {
        _uiState.update { currentState ->
            val newSelectedIds = currentState.selectedInterestIds.toMutableSet()
            if (newSelectedIds.contains(interestId)) {
                // Eğer zaten seçiliyse, seçimden kaldır
                newSelectedIds.remove(interestId)
            } else {
                // Eğer seçili değilse ve seçim sayısı 8'den az ise ekle
                if (newSelectedIds.size < 8) {
                    newSelectedIds.add(interestId)
                }
            }
            currentState.copy(selectedInterestIds = newSelectedIds)
        }
    }

    /**
     * "Devam Et" butonuna basıldığında çağrılır.
     * Bu fonksiyon, seçilen ID'leri DataStore'a kaydedecek ve
     * "ilgi alanı seçme tamamlandı" flag'ini true yapacak.
     */
    fun onContinueClicked() {
        if (!_uiState.value.isContinueButtonEnabled) return

        viewModelScope.launch {
            val selectedIds = _uiState.value.selectedInterestIds
            // TODO: 'selectedIds' listesini ve 'isSelectionCompleted=true' bilgisini
            // DataStore kullanarak kaydet.
            println("Seçilen İlgi Alanları Kaydedildi: $selectedIds")
        }
    }
}