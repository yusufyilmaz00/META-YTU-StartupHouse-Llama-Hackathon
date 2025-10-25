package com.balikllama.xpguiderdemo.ui.screen.test

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.ui.theme.B1demoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestView(
    modifier: Modifier = Modifier,
    uiState: TestUIState,
    onAnswerSelected: (AnswerType) -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TestTopBar(
                progressText = uiState.progressText,
                onBackPressed = onBackPressed,
                // Sadece ilk soruda değilken geri butonunu göster
                isBackButtonEnabled = uiState.currentQuestionIndex > 0
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                AnimatedContent(
                    targetState = uiState.currentQuestion,
                    label = "questionTextAnimation"
                ) { question ->
                    Text(
                        text = question?.qText ?: "Loading question...",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Cevap butonları
                AnswerButtons(
                    currentAnswer = uiState.currentAnswer,
                    onAnswerSelected = onAnswerSelected
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestTopBar(
    progressText: String,
    onBackPressed: () -> Unit,
    isBackButtonEnabled: Boolean
) {
    TopAppBar(
        title = { Text(text = "Test") },
        navigationIcon = {
            if (isBackButtonEnabled) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to previous question"
                    )
                }
            }
        },
        actions = {
            Text(
                text = progressText,
                modifier = Modifier.padding(end = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Composable
private fun AnswerButtons(
    currentAnswer: AnswerType?,
    onAnswerSelected: (AnswerType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Evet Butonu
        AnswerButton(
            text = "Yes",
            answerType = AnswerType.YES,
            isSelected = currentAnswer == AnswerType.YES,
            onClick = { onAnswerSelected(AnswerType.YES) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Kararsızım Butonu
        AnswerButton(
            text = "Neutral",
            answerType = AnswerType.NEUTRAL,
            isSelected = currentAnswer == AnswerType.NEUTRAL,
            onClick = { onAnswerSelected(AnswerType.NEUTRAL) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Hayır Butonu
        AnswerButton(
            text = "No",
            answerType = AnswerType.NO,
            isSelected = currentAnswer == AnswerType.NO,
            onClick = { onAnswerSelected(AnswerType.NO) }
        )
    }
}

@Composable
private fun AnswerButton(
    text: String,
    answerType: AnswerType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Seçili butonu dolu, diğerlerini outlined (çerçeveli) göster
    if (isSelected) {
        Button(onClick = onClick) {
            Text(text)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(text)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TestViewPreview() {
    B1demoTheme {
        TestView(
            uiState = TestUIState(
                isLoading = false,
                questions = listOf(
                    QuestionEntity(
                        qId = "Q1_PREVIEW",
                        qText = "Are you a morning person?",
                        primaryId = "A",
                        s1Id = "G",
                        s1w = 0.2f,
                        s2Id = "",
                        s2w = 0f,
                        s3Id = "",
                        s3w = 0f,
                        active = true
                    )
                ),
                currentQuestionIndex = 0,
                currentAnswer = AnswerType.YES
            ),
            onAnswerSelected = {},
            onBackPressed = {}
        )
    }
}
