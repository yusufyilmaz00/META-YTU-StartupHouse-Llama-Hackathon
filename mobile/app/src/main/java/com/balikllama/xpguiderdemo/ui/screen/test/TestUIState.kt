package com.balikllama.xpguiderdemo.ui.screen.test

import com.balikllama.xpguiderdemo.data.local.entity.AnswerType
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import com.balikllama.xpguiderdemo.domain.ScoreResult

/**
 * Test ekranının anlık durumunu temsil eden veri sınıfı.
 * ViewModel bu sınıfı güncelleyerek UI'a ne göstereceğini söyler.
 */
data class TestUIState(
    val credit: Int = 0,
    // Ekran yüklenirken veya bir işlem yapılırken true olur. (Örn: Sorular çekilirken)
    val isLoading: Boolean = true,

    // Test için veritabanından çekilen tüm soruların listesi.
    val questions: List<QuestionEntity> = emptyList(),

    // Kullanıcının o an ekranda gördüğü sorunun listedeki indeksi.
    val currentQuestionIndex: Int = 0,

    // Mevcut soruya daha önce verilmiş bir cevap varsa onu tutar. UI'da butonları işaretli göstermek için kullanılır.
    val currentAnswer: AnswerType? = null,

    // Test tamamlandığında true olur, sonuç ekranına yönlendirme için kullanılabilir.
    val isTestCompleted: Boolean = false,
    val results: List<ScoreResult> = emptyList()
) {
    /**
     * O an görüntülenen soruyu kolayca almak için bir yardımcı özellik.
     * Bu, listede olmayan bir indekse erişmeye çalışırken oluşabilecek çökmeleri engeller.
     */
    val currentQuestion: QuestionEntity?
        get() = questions.getOrNull(currentQuestionIndex)

    /**
     * İlerleme çubuğu (progress bar) için ilerleme metnini hesaplar. (Örn: "3 / 10")
     */
    val progressText: String
        get() = if (questions.isNotEmpty()) "${currentQuestionIndex + 1} / ${questions.size}" else ""
}
