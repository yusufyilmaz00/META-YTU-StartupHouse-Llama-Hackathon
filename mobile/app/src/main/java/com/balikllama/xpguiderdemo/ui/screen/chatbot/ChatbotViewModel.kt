package com.balikllama.xpguiderdemo.ui.screen.chatbot

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.chat.Message
import com.balikllama.xpguiderdemo.model.chat.MessageAuthor
import com.balikllama.xpguiderdemo.repository.ApiResult
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
        // --- DEĞİŞEN KISIM: İLK MESAJI DİNAMİK OLARAK YÜKLE ---

        // 1. Repository'den API'den gelen analiz mesajını iste.
        val analysisMessage = chatbotRepository.getInitialAnalysisMessage()

        // 2. Mesaj var mı diye kontrol et.
        if (analysisMessage != null) {
            // 2a. Eğer mesaj varsa, onu ilk mesaj olarak listeye ekle.
            addMessage(analysisMessage, MessageAuthor.AI)
            Log.d("ChatbotViewModel", "API'den gelen ilk analiz mesajı eklendi.")

            // 2b. Bu mesajı bir daha göstermemek için Repository'den temizle.
            chatbotRepository.clearInitialAnalysisMessage()
        } else {
            // 3. Eğer mesaj yoksa (kullanıcı testi çözmeden doğrudan chat'e geldiyse),
            // standart karşılama mesajını göster.
            addMessage("Merhaba! Ben senin kariyer asistanınım. Sana nasıl yardımcı olabilirim?", MessageAuthor.AI)
            Log.d("ChatbotViewModel", "Standart karşılama mesajı eklendi çünkü analiz mesajı bulunamadı.")
        }
    }

    // Kullanıcının yazdığı metin değiştiğinde çağrılır
    fun onInputChanged(newInput: String) {
        _uiState.update { it.copy(currentInput = newInput) }
    }

    // Kullanıcı gönder butonuna bastığında çağrılır
    fun onSendMessage() {
        val messageText = _uiState.value.currentInput.trim()
        if (messageText.isBlank() || _uiState.value.isAiTyping) return

        // 1. Kullanıcının mesajını listeye ekle ve input'u temizle
        addMessage(messageText, MessageAuthor.USER)
        _uiState.update { it.copy(currentInput = "", isAiTyping = true) } // AI yazıyor durumunu başlat

        // 2. API'ye gerçek istek at
        viewModelScope.launch {
            when (val result = chatbotRepository.sendMessage(messageText)) {
                is ApiResult.Success -> {
                    // 3a. Başarılı olursa, AI'nın cevabını listeye ekle
                    addMessage(result.data, MessageAuthor.AI)
                }
                is ApiResult.Error -> {
                    // 3b. Hata olursa, hata mesajını listeye ekle
                    val errorMessage = result.message ?: "Bilinmeyen bir hata oluştu."
                    addMessage(errorMessage, MessageAuthor.AI)
                    // Hata durumunda da AI'nın yazma işlemi biter.
                }
            }
            // 4. İşlem bitince "yazıyor" durumunu kapat
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

}