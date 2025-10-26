package com.balikllama.xpguiderdemo.ui.screen.chatbot

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.chat.Message
import com.balikllama.xpguiderdemo.model.chat.MessageAuthor
import com.balikllama.xpguiderdemo.repository.ChatbotRepository
import com.balikllama.xpguiderdemo.repository.CreditRepository
import com.balikllama.xpguiderdemo.repository.TestResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.associate

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val creditRepository: CreditRepository,
    private val chatbotRepository: ChatbotRepository,
    private val testResultRepository: TestResultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUIState())

    // Kredi ve chat durumunu birleştirerek tek bir UIState oluştur
    val uiState: StateFlow<ChatUIState> = combine(
        _uiState,
        creditRepository.userCredits
    ) { chatState, credit ->
        chatState.copy(credit = credit)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChatUIState()
    )

    init {
        Log.d("ChatbotViewModel", "ViewModel BAŞLATILDI!")
        addMessage("Merhaba! Ben senin kariyer asistanınım. Sana nasıl yardımcı olabilirim?", MessageAuthor.AI)
        sendAnalysisResults()
    }

    // Kullanıcının yazdığı metin değiştiğinde çağrılır
    fun onInputChanged(newInput: String) {
        _uiState.update { it.copy(currentInput = newInput) }
    }

    // Kullanıcı gönder butonuna bastığında çağrılır
    fun onSendMessage() {
        val messageText = _uiState.value.currentInput.trim()
        if (messageText.isBlank()) return

        // 1. Kullanıcının mesajını listeye ekle ve input'u temizle
        addMessage(messageText, MessageAuthor.USER)
        _uiState.update { it.copy(currentInput = "") }

        // 2. AI'nın cevap vermesi için sahte bir bekleme ve cevap süreci başlat
        viewModelScope.launch {
            // "Yazıyor..." animasyonu için durumu güncelle
            _uiState.update { it.copy(isAiTyping = true) }
            delay(timeMillis = 1500) // 1.5 saniye bekle

            // Sahte bir cevap oluştur ve listeye ekle
            val aiResponse = "Bu harika bir soru! '$messageText' konusunu senin için araştırıyorum."
            addMessage(aiResponse, MessageAuthor.AI)

            // "Yazıyor..." animasyonunu kaldır
            _uiState.update { it.copy(isAiTyping = false) }
        }
    }

    // Mesaj listesine yeni bir mesaj ekleyen yardımcı fonksiyon
    private fun addMessage(text: String, author: MessageAuthor) {
        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            text = text,
            author = author
        )
        _uiState.update {
            it.copy(messages = it.messages + newMessage)
        }
    }

    // intelligence
    private fun sendAnalysisResults() {
        viewModelScope.launch {
            Log.d("ChatbotViewModel", "sendAnalysisResultsToBackend fonksiyonu çalıştı.")
            // 1. Veritabanından test sonuçlarını al
            val testResults = testResultRepository.getTestResults()
            Log.d("ChatbotViewModel", "Veritabanından ${testResults.size} adet test sonucu bulundu.")
            // Eğer sonuç yoksa işlemi durdur
            if (testResults.isEmpty()) {
                Log.e("ChatbotViewModel", "Test sonucu bulunamadığı için API isteği İPTAL EDİLDİ.")
                // TODO: Kullanıcıya henüz test yapmadığına dair bir mesaj gösterilebilir.
                return@launch
            }

            // 2. Sonuçları Map<String, Double> formatına dönüştür
            val ratiosMap = testResults.associate { result ->
                result.name to result.percentage.toDouble()
            }
            Log.d("ChatbotViewModel", "API'ye gönderilecek veri: $ratiosMap")
            // 3. Repository aracılığıyla API'ye gönder
            chatbotRepository.sendRatiosToIntelligenceApi(ratiosMap)
            // Sonuç zaten repository içinde loglanıyor.
        }
    }
}