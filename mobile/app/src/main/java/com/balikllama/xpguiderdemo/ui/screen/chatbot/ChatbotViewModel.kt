package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balikllama.xpguiderdemo.model.chat.Message
import com.balikllama.xpguiderdemo.model.chat.MessageAuthor
import com.balikllama.xpguiderdemo.repository.CreditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val creditRepository: CreditRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUIState())
    val uiState: StateFlow<ChatUIState> = _uiState.asStateFlow()

    init {
        // Ekran açıldığında AI'dan bir karşılama mesajı ekleyelim
        addMessage("Merhaba! Ben senin kariyer asistanınım. Sana nasıl yardımcı olabilirim?", MessageAuthor.AI)
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
}