package com.balikllama.b1demo.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.b1demo.Greeting
import com.balikllama.b1demo.ui.components.AppTopBar
import com.balikllama.b1demo.ui.theme.B1demoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    uiState: HomeUIState, // ViewModel yerine UIState alıyor
    onAddCredit: () -> Unit, // Kredi ekleme eylemi
    onDecreaseCredit: () -> Unit, // Kredi azaltma eylemi
    onNavigateToDbTest: () -> Unit // YENİ PARAMETRE
) {
    B1demoTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                AppTopBar(
                    title = "Home",
                    creditInfo = uiState.credit.toString()
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding), // innerPadding'i Column'a uyguluyoruz
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting(
                    name = "HOME PAGE",
                )

                Text("Kredi Test Butonları")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onAddCredit) {
                    Text("5 Kredi Ekle")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = onDecreaseCredit) {
                    Text("10 Kredi Azalt")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onNavigateToDbTest) { // YENİ BUTON
                    Text("DB İçeriğini Göster")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView(
        uiState = HomeUIState(credit = 150),
        onAddCredit = {},
        onDecreaseCredit = {},
        windowSizeClass = WindowSizeClass.calculateFromSize(
            androidx.compose.ui.unit.DpSize(
                412.dp,
                891.dp
            )
        ),
        onNavigateToDbTest = {}
    )
}