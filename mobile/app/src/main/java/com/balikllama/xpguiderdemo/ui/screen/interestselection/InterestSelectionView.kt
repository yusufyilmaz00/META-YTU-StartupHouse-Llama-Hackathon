package com.balikllama.xpguiderdemo.ui.screen.interestselection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestSelectionView(
    modifier: Modifier = Modifier,
    uiState: InterestSelectionUIState,
    onInterestClicked: (String) -> Unit,
    onContinueClicked: () -> Unit
) {
    // Scaffold'u kaldırıp yerine Column kullandık.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.M), // Yanlardan boşluk
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Başlık ve açıklama
        Text(
            text = "İlgi Alanlarını Keşfet",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = Spacing.L)
        )
        Text(
            text = "Başlamak için sana en uygun olan en az 5, en fazla 8 alanı seç.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.S, bottom = Spacing.L)
        )

        // Kaydırılabilir Çip Listesi
        // weight(1f) sayesinde bu alan, buton ve başlıklar dışındaki tüm boşluğu doldurur.
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.S),
            horizontalArrangement = Arrangement.spacedBy(Spacing.S),
            // En alta geldiğinde bile alttaki butona boşluk bırakmak için.
            contentPadding = PaddingValues(bottom = Spacing.M)
        ) {
            items(uiState.allInterests, key = { it.id }) { interest ->
                InterestChip(
                    text = interest.areaOfInterest,
                    isSelected = uiState.selectedInterestIds.contains(interest.id),
                    onClick = { onInterestClicked(interest.id) }
                )
            }
        }

        BottomContinueBar(
            uiState = uiState,
            onContinueClicked = onContinueClicked
        )

        Spacer(modifier = Modifier.height(160.dp))
    }
}

@Composable
private fun BottomContinueBar(
    uiState: InterestSelectionUIState,
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Butonun sadece seçim yapıldığında görünür olmasını sağlar
    AnimatedVisibility(
        visible = uiState.isContinueButtonEnabled,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.S), // Üstten ve alttan hafif boşluk
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.S)
        ) {
            // Sayaç
            Text(
                text = "${uiState.selectionCount}/${InterestSelectionUIState.MAX_SELECTION_COUNT} seçildi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // İleri Butonu
            Button(
                onClick = onContinueClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isContinueButtonEnabled
            ) {
                Text("Devam Et")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterestChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        enabled = true,
        shape = MaterialTheme.shapes.large,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp,
            selectedBorderWidth = 0.dp,
            enabled = true,
            selected = isSelected
        )
    )
}
