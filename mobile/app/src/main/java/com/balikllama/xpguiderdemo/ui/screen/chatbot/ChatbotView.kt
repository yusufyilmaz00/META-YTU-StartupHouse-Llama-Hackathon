package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.balikllama.xpguiderdemo.Greeting
import com.balikllama.xpguiderdemo.ui.theme.B1demoTheme

@Composable
fun ChatbotView(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass
) {

    B1demoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "CHATBOT PAGE",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}