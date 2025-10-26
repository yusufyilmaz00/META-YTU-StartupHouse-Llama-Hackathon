package com.balikllama.xpguiderdemo.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel

/**
 * Ortak Scaffold component - Tüm ekranlar için
 * Otomatik olarak kredi bilgisini gösterir
 */
@Composable
fun ScreenScaffold(
    title: String,
    creditViewModel: CreditViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val credits by creditViewModel.userCredits.collectAsState()
    val isLoading by creditViewModel.isLoading.collectAsState()

    AppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    title = title,
                    creditInfo = if (isLoading) "..." else credits.toString()
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}