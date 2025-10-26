package com.balikllama.xpguiderdemo.ui.screen.chatbot

import com.balikllama.xpguiderdemo.model.chat.Message

data class ChatUIState(
    val credit: Int = 0,
    // Ekranda gösterilecek tüm mesajların listesi
    val messages: List<Message> = emptyList(),

    // Kullanıcının yazı yazdığı input alanının anlık durumu
    val currentInput: String = "",

    // AI cevap verirken veya bir işlem yapılırken true olur (örneğin, yükleme animasyonu göstermek için)
    val isAiTyping: Boolean = false
)