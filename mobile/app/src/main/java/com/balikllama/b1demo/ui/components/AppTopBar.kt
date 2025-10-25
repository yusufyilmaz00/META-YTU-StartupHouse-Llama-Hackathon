package com.balikllama.b1demo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.balikllama.b1demo.ui.designsystem.AppTheme
import com.balikllama.b1demo.ui.designsystem.Radius
import com.balikllama.b1demo.ui.designsystem.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    creditInfo: String
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            CreditChip(creditInfo = creditInfo)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun CreditChip(
    creditInfo: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(end = Spacing.M)
            .clip(RoundedCornerShape(Radius.M))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = Spacing.M, vertical = Spacing.S),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "💳",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(end = Spacing.XS)
        )
        Text(
            text = creditInfo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreview() {
    AppTheme {
        AppTopBar(
            title = "Ana Sayfa",
            creditInfo = "150"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarDarkPreview() {
    AppTheme(darkTheme = true) {
        AppTopBar(
            title = "Test Ekranı",
            creditInfo = "42"
        )
    }
}