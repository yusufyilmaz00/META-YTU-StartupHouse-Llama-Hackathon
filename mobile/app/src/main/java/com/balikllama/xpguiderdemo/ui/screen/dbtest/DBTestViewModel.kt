package com.balikllama.xpguiderdemo.ui.screen.dbtest


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.data.local.entity.CalculationFactorEntity
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.TraitEntity
import com.balikllama.xpguiderdemo.repository.CalculationFactorRepository
import com.balikllama.xpguiderdemo.repository.InterestRepository
import com.balikllama.xpguiderdemo.repository.TraitRepository
import com.balikllama.xpguiderdemo.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DBTestViewModel @Inject constructor(
    private val interestRepository: InterestRepository,
    private val traitRepository: TraitRepository,
    private val questionRepository: QuestionRepository,
    private val calculationFactorRepository: CalculationFactorRepository
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
    // Sorular için YENİ StateFlow
    val questions: StateFlow<List<QuestionEntity>> = questionRepository.getAllQuestions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Hesaplama faktörleri için YENİ StateFlow
    val factors: StateFlow<List<CalculationFactorEntity>> = calculationFactorRepository.getAllFactors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            // Tüm tablolar için başlangıç verilerini ekle
            interestRepository.insertInitialInterests()
            traitRepository.insertInitialTraits()
            questionRepository.insertInitialQuestions()
            calculationFactorRepository.insertInitialFactors()
        }
    }

    /**
     * Tüm tabloları temizler.
     */
    fun resetDatabase() {
        viewModelScope.launch {
            // 1. Önce tüm tabloları temizle
            interestRepository.clearAllInterests()
            traitRepository.clearAllTraits()
            questionRepository.clearAllQuestions()
            calculationFactorRepository.clearAllFactors()
        }
    }
}
