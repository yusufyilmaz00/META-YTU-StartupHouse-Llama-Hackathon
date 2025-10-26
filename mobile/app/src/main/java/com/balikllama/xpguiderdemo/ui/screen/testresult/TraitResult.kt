package com.balikllama.xpguiderdemo.ui.screen.testresult

/**
 * Test sonuçları ekranında her bir kişilik özelliğini
 * (isim, skor ve açıklama) göstermek için kullanılan veri sınıfı.
 */
data class TraitResult(
    val name: String,
    val score: Float,
    val description: String
)
