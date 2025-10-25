package com.balikllama.b1demo.ui.screen.dbtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.balikllama.b1demo.data.local.entity.InterestEntity
import com.balikllama.b1demo.ui.components.AppTopBar

// Sadece UI'ı gösteren, önizlenebilir Composable
@Composable
fun DBTestView(
    modifier: Modifier = Modifier,
    interests: List<InterestEntity>
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppTopBar( title = "DB Test: İlgi Alanları",creditInfo = "51") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("İlgi Alanları (interest_list)", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
            }
            if (interests.isEmpty()) {
                item {
                    Text("`interest_list` tablosunda veri bulunamadı.")
                }
            } else {
                items(interests) { interest ->
                    InterestItem(interest = interest)
                }
            }
            // Gelecekte diğer tabloların listesi buraya eklenebilir
        }
    }
}

@Composable
fun InterestItem(interest: InterestEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = interest.areaOfInterest, fontWeight = FontWeight.Bold)
            Text(text = "ID: ${interest.id}", fontWeight = FontWeight.Light)
        }
    }
}