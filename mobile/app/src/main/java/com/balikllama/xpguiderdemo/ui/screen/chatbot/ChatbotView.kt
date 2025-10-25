package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.balikllama.xpguiderdemo.Greeting
import com.balikllama.xpguiderdemo.ui.designsystem.Radius
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing
import com.balikllama.xpguiderdemo.ui.theme.B1demoTheme

@Composable
fun ChatbotView(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass
) {

    B1demoTheme {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.M))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = Spacing.XL, vertical = Spacing.L),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = " Welcome to ChatBot Page",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}