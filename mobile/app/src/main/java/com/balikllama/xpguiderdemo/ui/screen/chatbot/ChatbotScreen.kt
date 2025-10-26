package com.balikllama.xpguiderdemo.ui.screen.chatbot

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.balikllama.xpguiderdemo.ui.navigation.Routes
import com.balikllama.xpguiderdemo.viewmodel.CreditViewModel

@Composable
fun ChatbotScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ChatbotViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // UI katmanına (View) state'i ve event lambda'larını (fonksiyonları) iletiyoruz.
    ChatbotView(
        modifier = modifier,
        uiState = uiState,
        onInputChanged = viewModel::onInputChanged,
        onSendMessage = viewModel::onSendMessage,
        onNavigateToTestResults = {
            navController.navigate(Routes.TEST_RESULTS)
        }
    )
}