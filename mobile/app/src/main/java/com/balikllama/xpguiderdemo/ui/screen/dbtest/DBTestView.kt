package com.balikllama.xpguiderdemo.ui.screen.dbtest

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.data.local.entity.CalculationFactorEntity
import com.balikllama.xpguiderdemo.data.local.entity.InterestEntity
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.data.local.entity.TraitEntity
import com.balikllama.xpguiderdemo.ui.components.AppTopBar

// Sadece UI'ı gösteren, önizlenebilir Composable
@Composable
fun DBTestView(
    modifier: Modifier = Modifier,
    interests: List<InterestEntity>,
    traits: List<TraitEntity>,
    questions: List<QuestionEntity>,
    factors: List<CalculationFactorEntity>
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
            // ilgi alanları listesi
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
            // Karakter Özellikleri Listesi
            item {
                Text("Karakter Özellikleri (trait_list)", style = MaterialTheme.typography.titleLarge)
            }
            if (traits.isEmpty()) {
                item { Text("`trait_list` tablosunda veri bulunamadı.") }
            } else {
                items(traits, key = { it.traitId }) { trait ->
                    TraitItem(trait = trait) // Yeni composable
                }
            }

            // Sorular Listesi
            item {
                Text("Sorular (question_list)", style = MaterialTheme.typography.titleLarge)
            }
            if (questions.isEmpty()) {
                item { Text("`question_list` tablosunda veri bulunamadı.") }
            } else {
                items(questions, key = { it.qId }) { question ->
                    QuestionItem(question = question) // Yeni composable
                }
            }

            // Hesaplama Faktörleri Listesi
            item {
                Text("Hesaplama Faktörleri (calculation_factors)", style = MaterialTheme.typography.titleLarge)
            }
            if (factors.isEmpty()) {
                item { Text("`calculation_factors` tablosunda veri bulunamadı.") }
            } else {
                items(factors, key = { it.key }) { factor ->
                    CalculationFactorItem(factor = factor) // Yeni composable
                }
            }
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

@Composable
fun TraitItem(trait: TraitEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = trait.traitName, fontWeight = FontWeight.Bold)
            Text(text = "ID: ${trait.traitId}", fontWeight = FontWeight.Light)
        }
    }
}

@Composable
fun QuestionItem(question: QuestionEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Soru: ${question.qText}", fontWeight = FontWeight.Bold)
            Text(text = "ID: ${question.qId} | Aktif: ${question.active}")
            Text(text = "Primary: ${question.primaryId}")
            Text(text = "Ağırlıklar: S1(${question.s1Id} - ${question.s1w}), S2(${question.s2Id} - ${question.s2w}), S3(${question.s3Id} - ${question.s3w})")
        }
    }
}

@Composable
fun CalculationFactorItem(factor: CalculationFactorEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Key: ${factor.key}", fontWeight = FontWeight.Bold)
            Text(text = "Value: ${factor.value}", fontWeight = FontWeight.Light)
        }
    }
}