package com.balikllama.xpguiderdemo.ui.screen.testresult

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.domain.ScoreResult
import com.balikllama.xpguiderdemo.ui.components.AppTopBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing

@Composable
fun TestResultView(
    modifier: Modifier = Modifier,
    results: List<ScoreResult>,
    onNavigateHome: () -> Unit
) {
    AppTheme {
        Scaffold(
            modifier = modifier,
            topBar = { AppTopBar(title = "Test Sonuçların", creditInfo = "") }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = Spacing.M),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.L))

                Text(
                    text = "Karakteristik Özellik Puanların",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "En yüksek puana sahip özelliklerin, potansiyelini en çok yansıtanlardır.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = Spacing.L)
                )

                // Sonuçların listelendiği alan
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = Spacing.S),
                    verticalArrangement = Arrangement.spacedBy(Spacing.S)
                ) {
                    itemsIndexed(results) { index, result ->
                        ScoreItem(rank = index + 1, result = result)
                    }
                }

                // Ana Sayfaya Dön Butonu
                Button(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.M)
                ) {
                    Text("ANA SAYFAYA DÖN")
                }
            }
        }
    }
}

@Composable
private fun ScoreItem(rank: Int, result: ScoreResult) {
    // Animasyonun başlaması için bir tetikleyici state
    var triggerAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        triggerAnimation = true
    }

    // Skoru 0-1 arasına normalize ederek progress bar için uygun hale getirelim (geçici bir varsayım)
    // En yüksek skorun 50 olduğunu varsayalım.
    val maxScore = 50f
    val normalizedScore = (result.score / maxScore).coerceIn(0f, 1f)

    // Değeri animasyonlu olarak değiştir
    val animatedProgress by animateFloatAsState(
        targetValue = if (triggerAnimation) normalizedScore else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 100 * rank),
        label = "progressAnimation"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Spacing.XS)
    ) {
        Column(modifier = Modifier.padding(Spacing.M)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$rank. ${result.traitName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "%.2f".format(result.score),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(Spacing.S))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TestResultsViewPreview() {
    TestResultView(
        results = listOf(
            ScoreResult("A", "Analitik Zeka", 45.5f),
            ScoreResult("M", "Girişimcilik Zekası", 38.2f),
            ScoreResult("J", "Fiziksel Zeka", 25.0f),
            ScoreResult("C", "Duygusal Zeka", 15.8f),
            ScoreResult("L", "Stratejik Zeka", -5.1f)
        ),
        onNavigateHome = {}
    )
}
