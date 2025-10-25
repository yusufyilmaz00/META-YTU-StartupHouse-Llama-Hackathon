package com.balikllama.xpguiderdemo.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.ui.components.AppTopBar
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing

@Composable
fun ProfileView(
    modifier: Modifier = Modifier,
    uiState: ProfileUIState // Artık UI durumunu dışarıdan alıyor
) {
    AppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = { AppTopBar(title = "Profile", creditInfo = uiState.credit.toString()) }        ) { innerPadding ->
            if (uiState.isLoading) {
                // Yükleniyor durumunda ortada bir progress indicator göster
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Veri yüklendiğinde içeriği göster
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = Spacing.M),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(Spacing.L))
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil İkonu",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(Spacing.M),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Spacing.L))
                    }

                    item {
                        UserInfoCard(userDetails = uiState.userDetails)
                        Spacer(modifier = Modifier.height(Spacing.L))
                    }

                    item {
                        Text(
                            text = "Geçmiş Aktiviteler",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Spacing.M)
                        )
                    }

                    items(uiState.pastActivities, key = { it.id }) { activity ->
                        PastActivityItem(activity = activity)
                        Spacer(modifier = Modifier.height(Spacing.S))
                    }
                }
            }
        }
    }
}

// UserInfoCard ve PastActivityItem fonksiyonları aynı kalabilir.
@Composable
private fun UserInfoCard(userDetails: List<UserInfo>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.XS),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(Spacing.M)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(Spacing.S)
        ) {
            userDetails.forEach { info ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${info.label}:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = info.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PastActivityItem(activity: ActivityItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(Spacing.M),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Geçmiş Aktivite İkonu",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(Spacing.M))
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true, name = "Profile Screen Preview")
@Composable
fun ProfileViewPreview() {
    // Önizleme için artık sahte bir UIState oluşturuyoruz.
    ProfileView(
        uiState = ProfileUIState(
            isLoading = false,
            userDetails = listOf(
                UserInfo("Username", "Test User"),
                UserInfo("E-main", "tester99@gmail.com")
            ),
            pastActivities = listOf(
                ActivityItem("1", "Kişilik Testini Tamamladın")
            )
        )
    )
}
