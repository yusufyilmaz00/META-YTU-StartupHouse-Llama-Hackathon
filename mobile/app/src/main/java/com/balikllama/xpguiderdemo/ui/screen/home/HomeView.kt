package com.balikllama.xpguiderdemo.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.Greeting
import com.balikllama.xpguiderdemo.ui.components.AppTopBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing
import com.balikllama.xpguiderdemo.ui.navigation.Routes
import com.balikllama.xpguiderdemo.ui.theme.B1demoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    onNavigateToTest: () -> Unit, // Teste gitmek için tek bir event alacak
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.M), // Sayfanın geneline bir boşluk verelim
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // İçeriği yukarıdan başlat
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth() // Genişliği tam ekran yap
                // Yüksekliği ekranın yaklaşık %20-25'i gibi ayarla
                .height(180.dp)
                // Kartın tıklanabilir olmasını sağla
                .clickable(onClick = onNavigateToTest),
            elevation = CardDefaults.cardElevation(defaultElevation = Spacing.S),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.L),
                // Kartın içindeki içeriği dikeyde ve yatayda ortala
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kariyer Yolculuğunu Keşfet",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Spacing.S))
                Text(
                    text = "Kişilik testini çözerek potansiyelini ortaya çıkar.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(Spacing.M))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Teste Git"
                )
            }
        }

        // ESKİ BUTONLAR KALDIRILDI
    }
}