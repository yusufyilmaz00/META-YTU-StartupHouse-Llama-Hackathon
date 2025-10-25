package com.balikllama.xpguiderdemo.ui.screen.test

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.ui.designsystem.AppTheme
import com.balikllama.xpguiderdemo.ui.designsystem.Spacing
import com.balikllama.xpguiderdemo.ui.designsystem.customColors


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
                isBackButtonEnabled = uiState.currentQuestionIndex > 0
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
                .padding(innerPadding)
                .padding(horizontal = Spacing.M),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing.L),
                    elevation = CardDefaults.cardElevation(defaultElevation = Spacing.XS)
                ) {
                    AnimatedContent(
                        targetState = uiState.currentQuestion,
                        label = "questionTextAnimation"
                    ) { question ->
                        Text(
                            text = question?.qText ?: "Soru yükleniyor...",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(Spacing.M)
                        )
                    }
                }

                // Değişiklik 3: Spacer'a daha az ağırlık vererek butonların daha yukarıda konumlanmasını sağla.
                // 1f yerine 0.5f veya 1f gibi değerlerle oynayarak boşluğu ayarlayabilirsin.
                // Kart'a da bir ağırlık vererek daha orantılı bir dağılım yapabiliriz.
                Spacer(modifier = Modifier.weight(0.8f))

                AnswerButtons(
                    modifier = Modifier,
                    currentAnswer = uiState.currentAnswer,
                    onAnswerSelected = onAnswerSelected
                )

                Spacer(modifier = Modifier.weight(0.2f))
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
        title = { Text(text = "Test", style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            if (isBackButtonEnabled) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Önceki soruya dön"
                    )
                }
            }
        },
        actions = {
            Text(
                text = progressText,
                modifier = Modifier.padding(end = Spacing.M),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Composable
private fun AnswerButtons(
    modifier: Modifier = Modifier,
    currentAnswer: AnswerType?,
    onAnswerSelected: (AnswerType) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnswerButton(
            text = "Evet",
            isSelected = currentAnswer == AnswerType.YES,
            color = MaterialTheme.customColors.yesButton,
            contentColor = MaterialTheme.customColors.onYesButton,
            onClick = { onAnswerSelected(AnswerType.YES) }
        )

        Spacer(modifier = Modifier.width(Spacing.M))

        AnswerButton(
            text = "Kararsızım",
            isSelected = currentAnswer == AnswerType.NEUTRAL,
            color = MaterialTheme.customColors.neutralButton,
            contentColor = MaterialTheme.customColors.onNeutralButton,
            onClick = { onAnswerSelected(AnswerType.NEUTRAL) }
        )

        Spacer(modifier = Modifier.width(Spacing.M))

        AnswerButton(
            text = "Hayır",
            isSelected = currentAnswer == AnswerType.NO,
            color = MaterialTheme.customColors.noButton,
            contentColor = MaterialTheme.customColors.onNoButton,
            onClick = { onAnswerSelected(AnswerType.NO) }
        )
    }
}

@Composable
private fun AnswerButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    if (isSelected) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = contentColor
            )
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(color))
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}


@Preview(showBackground = true, name = "Light Mode Preview")
@Composable
fun TestViewLightPreview() {
    AppTheme(darkTheme = false) {
        TestView(
            uiState = TestUIState(
                isLoading = false,
                questions = listOf(
                    QuestionEntity(
                        qId = "Q1_PREVIEW",
                        qText = "Güne enerjik ve pozitif bir başlangıç yapmak senin için önemli midir ve bunu sık sık başarır mısın?",
                        primaryId = "A", s1Id = "G", s1w = 0.2f, s2Id = "", s2w = 0f, s3Id = "", s3w = 0f, active = true
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

@Preview(showBackground = true, name = "Dark Mode Preview")
@Composable
fun TestViewDarkPreview() {
    AppTheme(darkTheme = true) {
        TestView(
            uiState = TestUIState(
                isLoading = false,
                questions = listOf(
                    QuestionEntity(
                        qId = "Q1_PREVIEW",
                        qText = "Güne enerjik ve pozitif bir başlangıç yapmak senin için önemli midir ve bunu sık sık başarır mısın?",
                        primaryId = "A", s1Id = "G", s1w = 0.2f, s2Id = "", s2w = 0f, s3Id = "", s3w = 0f, active = true
                    )
                ),
                currentQuestionIndex = 0,
                currentAnswer = AnswerType.NEUTRAL
            ),
            onAnswerSelected = {},
            onBackPressed = {}
        )
    }
}