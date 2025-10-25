package com.balikllama.b1demo.ui.screen.dbtest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.hilt.navigation.compose.hiltViewModel


// Ekranın state yönetimi ve ViewModel bağlantısı
@Composable
fun DBTestScreen(
    modifier: Modifier = Modifier,
    viewModel: DBTestViewModel = hiltViewModel()
) {
    val interests by viewModel.interests.collectAsState()

    DBTestListView(
        modifier = modifier,
        interests = interests
    )
}


