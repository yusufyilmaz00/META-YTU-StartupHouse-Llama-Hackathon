package com.balikllama.xpguiderdemo.ui.screen.interestselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.repository.InterestRepository
import com.balikllama.xpguiderdemo.repository.UserRepository
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
    private val interestRepository: InterestRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InterestSelectionUIState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllInterests()
    }

    private fun loadAllInterests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // interestRepository.getAllInterests() bir Flow döndürdüğü için,
            // ondan gelen ilk ve tek listeyi .first() ile alıyoruz.
            val interests = interestRepository.getAllInterests().first()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    allInterests = interests
                )
            }
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
                newSelectedIds.remove(interestId)
            } else {
                if (newSelectedIds.size < InterestSelectionUIState.MAX_SELECTION_COUNT) {
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
            // Seçilen ID'leri şimdilik bir yere kaydetmiyoruz (sunucuya gidecekti)
            // Sadece kurulumun tamamlandığı bilgisini DB'ye kaydediyoruz.
            userRepository.saveSetupCompleted(true)

            // UI'ı yönlendirme için bilgilendir
            _uiState.update { it.copy(isSelectionSaved = true) }
        }
    }
}