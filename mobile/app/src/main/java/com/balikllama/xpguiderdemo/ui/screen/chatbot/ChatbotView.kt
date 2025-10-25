package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.balikllama.xpguiderdemo.ui.designsystem.Radius
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing
import com.balikllama.xpguiderdemo.model.chat.Message
import com.balikllama.xpguiderdemo.model.chat.MessageAuthor
import com.balikllama.xpguiderdemo.ui.components.AppTopBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme

@Composable
fun ChatbotView(
    modifier: Modifier = Modifier,
    uiState: ChatUIState,
    onInputChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    val listState = rememberLazyListState()

    // Yeni bir mesaj eklendiğinde listenin en altına otomatik olarak kaydır
    LaunchedEffect(uiState.messages.size, uiState.isAiTyping) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }

    // B1demoTheme yerine projenin ana teması olan AppTheme'i kullanıyoruz.
    AppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { AppTopBar(title = "Carrier Assistan AI", creditInfo = uiState.credit.toString()) },            // Sistem çubuklarının (örn: klavye) içeriği sıkıştırmasını yönet
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Mesajların listelendiği alan
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = listState,
                    contentPadding = PaddingValues(Spacing.M),
                    verticalArrangement = Arrangement.spacedBy(Spacing.M)
                ) {
                    items(uiState.messages, key = { it.id }) { message ->
                        MessageBubble(message = message)
                    }
                    if (uiState.isAiTyping) {
                        item {
                            MessageBubble(message = Message("typing", "Yazıyor...", MessageAuthor.AI))
                        }
                    }
                }

                // Mesaj yazma ve gönderme alanı
                ChatInput(
                    value = uiState.currentInput,
                    onValueChange = onInputChanged,
                    onSendClick = onSendMessage
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    // Mesajın sahibine göre hizalamayı ayarla
    val contentAlignment = if (message.author == MessageAuthor.USER) Alignment.CenterEnd else Alignment.CenterStart

    // Mesajın sahibine göre renkleri ve şekli design system'dan alalım
    val backgroundColor = when (message.author) {
        MessageAuthor.USER -> MaterialTheme.colorScheme.surfaceVariant // Kullanıcı balonu rengi
        MessageAuthor.AI -> MaterialTheme.colorScheme.surface // AI balonu rengi
    }
    // Radius token'larını kullanarak farklı köşe yuvarlaklıkları verelim
    val shape = when (message.author) {
        MessageAuthor.USER -> RoundedCornerShape(topStart = Radius.XL, topEnd = Radius.S, bottomStart = Radius.XL, bottomEnd = Radius.XL)
        MessageAuthor.AI -> RoundedCornerShape(topStart = Radius.S, topEnd = Radius.XL, bottomStart = Radius.XL, bottomEnd = Radius.XL)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f) // Baloncuğun maksimum genişliği
                .clip(shape)
                .background(backgroundColor)
                .padding(Spacing.M) // Spacing token'ı
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge, // Typography token'ı
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(shadowElevation = Spacing.S) { // Input alanına hafif bir gölge ekler
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.M), // Spacing token'ı
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Bir mesaj yaz...") },
                shape = RoundedCornerShape(Radius.L), // Radius token'ı
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.width(Spacing.S)) // Spacing token'ı
            IconButton(
                onClick = onSendClick,
                enabled = value.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Gönder")
            }
        }
    }
}


@Preview(showBackground = true, name = "Chatbot Preview")
@Composable
fun ChatbotViewPreview() {
    AppTheme {
        ChatbotView(
            uiState = ChatUIState(
                messages = listOf(
                    Message("1", "Merhaba, sana nasıl yardımcı olabilirim?", MessageAuthor.AI),
                    Message("2", "Kariyer hedeflerim hakkında konuşmak istiyorum.", MessageAuthor.USER),
                    Message("3", "Harika! Hedeflerin nelerdir? Uzun ve detaylı bir şekilde anlatabilirsin. Ben de sana bu konuda en iyi şekilde yardımcı olmak için buradayım.", MessageAuthor.AI),
                ),
                isAiTyping = true,
                currentInput = "Aslında ben..."
            ),
            onInputChanged = {},
            onSendMessage = {}
        )
    }
}