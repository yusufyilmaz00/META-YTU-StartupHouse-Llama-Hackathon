package com.balikllama.xpguiderdemo.ui.screen.interestselection

import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity

data class InterestSelectionUIState(
    // Veritabanından gelen tüm ilgi alanları
    val allInterests: List<InterestEntity> = emptyList(),

    // Kullanıcının seçtiği ilgi alanlarının ID'lerini tutan set
    val selectedInterestIds: Set<String> = emptySet(),

    // Veri yüklenirken gösterilecek olan
    val isLoading: Boolean = true
) {
    // Seçilen ilgi alanı sayısı
    val selectionCount: Int get() = selectedInterestIds.size

    val isContinueButtonEnabled: Boolean get() = selectionCount in MIN_SELECTION_COUNT..MAX_SELECTION_COUNT

    companion object {
        const val MIN_SELECTION_COUNT = 5
        const val MAX_SELECTION_COUNT = 8
    }
}