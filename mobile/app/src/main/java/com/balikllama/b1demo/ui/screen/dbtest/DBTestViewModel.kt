package com.balikllama.b1demo.ui.screen.dbtest


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.b1demo.data.local.entity.InterestEntity
import com.balikllama.b1demo.data.local.entity.TraitEntity
import com.balikllama.b1demo.repository.InterestRepository
import com.balikllama.b1demo.repository.TraitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DBTestViewModel @Inject constructor(
    private val interestRepository: InterestRepository,
    private val traitRepository: TraitRepository
) : ViewModel() {

    // Veritabanından gelen ilgi alanlarını bir StateFlow olarak tut
    val interests: StateFlow<List<InterestEntity>> = interestRepository.getAllInterests()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Karakter Özellikleri için YENİ StateFlow
    val traits: StateFlow<List<TraitEntity>> = traitRepository.getAllTraits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    init {
        // ViewModel ilk oluşturulduğunda, veritabanı boşsa başlangıç verilerini ekle
        viewModelScope.launch {
            interestRepository.insertInitialInterests()
            traitRepository.insertInitialTraits()
        }
    }
}
