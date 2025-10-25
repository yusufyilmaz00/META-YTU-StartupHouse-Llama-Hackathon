package com.balikllama.b1demo.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.balikllama.b1demo.Greeting
import com.balikllama.b1demo.ui.components.AppTopBar
import com.balikllama.b1demo.ui.theme.B1demoTheme

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    creditInfo: String,
    onRefreshCredits: () -> Unit = {},
) {

    B1demoTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(title = "Home",
                    creditInfo = creditInfo)
            }
        ) { innerPadding ->
            Greeting(
                name = "HOME PAGE",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
