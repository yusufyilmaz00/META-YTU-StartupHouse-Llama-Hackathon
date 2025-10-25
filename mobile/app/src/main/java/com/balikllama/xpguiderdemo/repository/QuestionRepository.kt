package com.balikllama.xpguiderdemo.repository

import com.balikllama.xpguiderdemo.data.local.dao.QuestionDao
import com.balikllama.xpguiderdemo.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val questionDao: QuestionDao
) {
    fun getAllQuestions(): Flow<List<QuestionEntity>> {
        return questionDao.getAllQuestions()
    }


    suspend fun clearAllQuestions() {
        questionDao.clearAll()
    }

    suspend fun insertInitialQuestions() {
        // Bu tablonun boş olup olmadığını kontrol et
        if (questionDao.getAllQuestions().first().isEmpty()) {
            val initialList = listOf(
                QuestionEntity("Q1", "Kitaplar benim için çok önemlidir.", "A", "G", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q2", "Zihnimde kolayca hesap yapabilirim.", "B", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q3", "Gözlerimi kapadığımda sıklıkla açık ve net imgeler görürüm.", "K", "B", 0.2f, "D", 0.1f, "", 0.0f, true),
                QuestionEntity("Q4", "Düzenli olarak yaptığım en az bir spor/fiziksel aktivite vardır.", "E", "F", 0.2f, "H", 0.1f, "", 0.0f, true),
                QuestionEntity("Q5", "Çevremdeki insanların danışmak için başvurduğu biriyimdir.", "N", "I", 0.2f, "B", 0.1f, "", 0.0f, true),
                QuestionEntity("Q6", "Hayat hakkındaki önemli sorular üzerine kafa yorarım.", "G", "H", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q7", "Doğa ile başbaşa olmayı severim.", "H", "O", 0.2f, "E", 0.1f, "", 0.0f, true),
                QuestionEntity("Q8", "Kelimeleri okumadan, yazmadan veya söylemeden önce beynimde işitirim.", "I", "D", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q9", "Matematik ve/veya fen dersleri okulda en çok sevdiğim dersler arasındadır.", "B", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q10", "Renklere karşı duyarlıyımdır.", "C", "D", 0.2f, "P", 0.1f, "", 0.0f, true),
                QuestionEntity("Q11", "Bir yerde uzun süre hiç kımıldamadan oturmaktan sıkılırım.", "E", "F", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q12", "Yürüyüş yapma, koşma, yüzme gibi bireysel sporlar yerine futbol, basketbol ve voleybol gibi takım sporlarını tercih ederim.", "F", "E", 0.2f, "I", 0.1f, "M", 0.1f, true),
                QuestionEntity("Q13", "Bazı insanların çevre ve doğal hayat hakkındaki duyarsızlıkları beni üzmektedir.", "P", "F", 0.2f, "G", 0.1f, "", 0.0f, true),
                QuestionEntity("Q14", "Mantıksal düşünmeyi ve beyin jimnastiği gerektiren oyunları severim.", "J", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q15", "Fotoğraf çekmeyi severim.", "C", "H", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q16", "Müzik dinlemeyi severim.", "L", "C", 0.2f, "E", 0.1f, "", 0.0f, true),
                QuestionEntity("Q17", "Ağaç işleri, dikiş, maket yapma gibi el becerisi gerektiren işleri severim.", "M", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q18", "Bir sorunum olduğunda tek başıma çözmeye çalışmak yerine yardımına başvurabileceğim birini ararım.", "F", "O", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q19", "Başarısız olduğum durumlarda kendime karşı esnek davranabilirim.", "O", "F", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q20", "Hayvanların etrafında dolaşmaktan, onlarla oynamaktan hoşlanırım.", "H", "F", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q21", "Kelime-işlem oyunlarını severim.", "A", "J", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q22", "Eğer... ise ne olur' türünde deneysel şeyler yapmayı severim.", "J", "H", 0.2f, "E", 0.1f, "K", 0.1f, true),
                QuestionEntity("Q23", "Yap-boz gibi görsel bulmaca oyunlarını severim.", "K", "J", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q24", "Çok iyi çaldığım bir müzik aleti vardır.", "D", "E", 0.2f, "J", 0.1f, "C", 0.1f, true),
                QuestionEntity("Q25", "En iyi fikirlerin içime doğduğu anlar yürüyüş, koşu gibi fiziksel etkinlikte bulunduğum zamanlardır.", "E", "G", 0.2f, "B", 0.1f, "", 0.0f, true),
                QuestionEntity("Q26", "En az üç yakın arkadaşım vardır.", "N", "I", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q27", "Kuş beslemek, akvaryum sahibi olmak gibi beni doğa ile irtibatlandıran en az bir hobim vardır.", "H", "M", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q28", "Dil sürçmeleri, tekerlemeler veya kafiyeli sözcüklerle eğlenmeyi severim.", "I", "D", 0.2f, "C", 0.1f, "", 0.0f, true),
                QuestionEntity("Q29", "Zihnim sürekli eşya ile ilgili kalıp, kural ve mantıksal silsileleri araştırmakla meşguldür.", "J", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q30", "Rüyalarım gerçek gibidir.", "C", "O", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q31", "Müzik olmasaydı hayatım daha kısır olurdu.", "L", "C", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q32", "Boş zamanlarımı genellikle dışarıda geçirmek isterim.", "M", "H", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q33", "Kendi başıma eğlenmekten çok bir grup arkadaşla eğlenmeyi tercih ederim.", "F", "E", 0.2f, "I", 0.1f, "", 0.0f, true),
                QuestionEntity("Q34", "Hayatla ilgili sürekli zihnimi meşgul eden bazı konular vardır.", "G", "P", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q35", "Kafam mevsimler, iklimler gibi doğal olayların oluşumu ile ilgili sorularla meşguldür.", "P", "B", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q36", "Konuşurken (veya yazdığımda) insanlar bazen kullandığım kelimelerin ne anlama geldiğini sorarlar.", "I", "J", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q37", "Bilimsel alandaki gelişmeler ilgimi çeker.", "B", "P", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q38", "Bilmediğim yerlerde yön tayin etmede ve gideceğim yeri bulmada rahatımdır.", "K", "H", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q39", "Sokakta yürürken bazen kendimi bir melodiyi mırıldanırken bulurum.", "D", "E", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q40", "Konuşurken sıklıkla el kol hareketi yapar veya diğer çeşit beden dillerini kullanırım.", "M", "F", 0.2f, "I", 0.1f, "", 0.0f, true),
                QuestionEntity("Q41", "Bildiğim şeyleri başkalarına öğretmeyi severim.", "N", "I", 0.2f, "J", 0.1f, "", 0.0f, true),
                QuestionEntity("Q42", "Zayıf ve kuvvetli yönlerim hakkında gerçekçi bir bakış açısına sahibim.", "O", "B", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q43", "Ağaç, kuş ve benzeri bitki ve hayvan türlerini kolaylıkla ayırt ederim.", "P", "K", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q44", "Her şeyin mutlaka mantıklı bir açıklaması olduğuna inanırım.", "J", "P", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q45", "Resim yapmayı ve çizmeyi severim.", "C", "D", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q46", "Kendimi bir lider olarak görürüm / İnsanlar bir lider olduğumu söyler.", "N", "I", 0.2f, "O", 0.1f, "", 0.0f, true),
                QuestionEntity("Q47", "Geometri gibi şekillerle ilgili konuları cebir gibi işlemsel konulardan daha kolay bulurum.", "K", "J", 0.2f, "", 0.0f, "", 0.0f, true),
                QuestionEntity("Q48", "Kalabalık ortamlarda rahat davranırım.", "F", "O", 0.2f, "", 0.0f, "", 0.0f, true),
            )
            questionDao.insertAll(initialList)
        }
    }
}
