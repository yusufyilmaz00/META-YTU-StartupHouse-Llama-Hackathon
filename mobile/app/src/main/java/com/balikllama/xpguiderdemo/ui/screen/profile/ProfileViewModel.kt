package com.balikllama.xpguiderdemo.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.repository.CreditRepository
import com.balikllama.xpguiderdemo.repository.UserRepository
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
    private val creditRepository: CreditRepository,
    // --- 2. YENİ REPOSITORY'Yİ ENJEKTE ET ---
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUIState())

    // Bu kısım (kredi birleştirme) doğru ve aynı kalabilir.
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
            delay(1000) // Sahte gecikme kalabilir.

            // --- 3. GERÇEK KULLANICI E-POSTASINI AL ---
            val userEmail = userRepository.getUserEmail() ?: "Bulunamadı"

            // Sahte verileri oluştururken gerçek e-postayı kullan
            val userDetails = listOf(
                UserInfo("Username", "Jhon Dere"), // Bu bilgi de ileride API'den gelebilir
                // "jhond@gmail.com" yerine SharedPreferences'ten gelen 'userEmail' değişkenini kullan.
                UserInfo("E-mail", userEmail),
                UserInfo("Education", "High School"),
                UserInfo("Age", "16"),
                UserInfo("Nationality", "American")
            )
            // Geçmiş aktiviteler sahte olarak kalabilir.
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
                    pastActivities = pastActivities,
                    // Bu satır artık doğrudan kullanılmıyor ama state'de kalmasında sakınca yok.
                    // E-posta `userDetails` listesi içinde yönetiliyor.
                    userEmail = userEmail
                )
            }
        }
    }
}