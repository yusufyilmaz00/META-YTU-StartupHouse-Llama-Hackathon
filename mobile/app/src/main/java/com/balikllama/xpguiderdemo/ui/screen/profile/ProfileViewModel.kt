package com.balikllama.xpguiderdemo.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val creditRepository: CreditRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUIState())

    // Kredi ve profil durumunu birleştirerek tek bir UIState oluştur
    val uiState: StateFlow<ProfileUIState> = combine(
        _uiState,
        creditRepository.userCredits
    ) { profileState, credit ->
        profileState.copy(credit = credit)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUIState()
    )
    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Sahte bir ağ gecikmesini simüle et
            delay(1000)
            // Sahte verileri oluştur
            val userDetails = listOf(
                UserInfo("Username", "Jhon Dere"),
                UserInfo("E-mail", "jhond@gmail.com"),
                UserInfo("Education", "High School"),
                UserInfo("Age", "16"),
                UserInfo("Nationality", "American")
            )
            val pastActivities = listOf(
                ActivityItem("1", "You completed the Personality Test"),
                ActivityItem("2", "You talked with the AI about 'Software Engineering'"),
                ActivityItem("3", "You completed the Personality Test"),
                ActivityItem("4", "You talked with the AI about 'Data Science'")
            )

            // UI durumunu yeni verilerle güncelle
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userDetails = userDetails,
                    pastActivities = pastActivities
                )
            }
        }
    }
}
