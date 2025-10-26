package com.balikllama.xpguiderdemo.ui.screen.interestselection

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing

@Composable
fun InterestSelectionView(
    modifier: Modifier = Modifier,
    uiState: InterestSelectionUIState,
    onInterestClicked: (String) -> Unit,
    onContinueClicked: () -> Unit
) {
    AppTheme {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                // Alt kısma sabitlenmiş "Devam Et" butonu
                Surface(shadowElevation = Spacing.S) {
                    Button(
                        onClick = onContinueClicked,
                        enabled = uiState.isContinueButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.M)
                    ) {
                        Text("DEVAM ET (${uiState.selectionCount}/8)")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = Spacing.M),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.L))

                // Bilgilendirme Başlığı
                Text(
                    text = "İlgi Alanlarını Keşfet",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Başlamak için sana en uygun olan en az 5, en fazla 8 alanı seç.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Spacing.L))

                // Yükleniyor durumu
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    // İlgi Alanı Izgarası (Grid)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(bottom = Spacing.M),
                        verticalArrangement = Arrangement.spacedBy(Spacing.S),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.S)
                    ) {
                        items(uiState.allInterests, key = { it.id }) { interest ->
                            InterestChip(
                                text = interest.areaOfInterest,
                                isSelected = uiState.selectedInterestIds.contains(interest.id),
                                onClick = { onInterestClicked(interest.id) }
                            )
                        }
                    }
                }
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

@Preview(showBackground = true)
@Composable
private fun InterestSelectionViewPreview() {
    InterestSelectionView(
        uiState = InterestSelectionUIState(
            isLoading = false,
            allInterests = (1..15).map { InterestEntity(id = "I$it", areaOfInterest = "İlgi Alanı $it") },
            selectedInterestIds = setOf("I1", "I5", "I9")
        ),
        onInterestClicked = {},
        onContinueClicked = {}
    )
}
