PROJE README — Özet ve Ürün Akışı

1) AMAÇ
Kullanıcının güçlü yönlerini ilgi alanları ve zeka tipleri üzerinden ölçüp doğrulamak; kısa bir sohbet-botu kontrolünden sonra 3 meslek ve her meslek için 1 mentör önermek.

2) KULLANICI AKIŞI
1. İlgi Alanı Seçimi
   a. 45 ilgi alanı listelenir.
   b. Kullanıcı 5–8 tanesini seçer.
   c. Bu seçimler sonraki değerlendirmede düşük ağırlıklı bir sinyal olarak kullanılır (ör. +w).

2. Zeka Tipi Anketi
   a. Toplam 48 soru vardır.
   b. Her zeka tipi için soru sayısı ekip tarafından belirlenir ve 2–4 aralığındadır; dağılımın toplamı 48’e eşittir.
   c. Her soru üç seçenekle puanlanır: Hayır = -0.4, Kararsız = 0.2, Eminim = 1.0.
   d. Her sorunun ağırlığı w ∈ {2, 3, 4} olarak tanımlanır (zeka tipine/soruya göre).
   e. Her zeka tipi skoru [0–100] ölçeğine normalize edilir (bkz. “Puanlama ve Birleştirme”).
   f. İlgi alanı eşleşmeleri ilgili zekaya küçük destek puanı ekler.

3. Form Linki
   a. Google Form: https://forms.gle/QXkBRRL1irwyzU5B8
   b. Ham formlar kullanıcıya görünmez; uygulama cevapları arka planda otomatik işler ve yalnızca anonim/özet sonuçları gösterir.

4. Analitik ve Dashboard
   a. Toplam kaç kişinin doldurduğu otomatik sayılır.
   b. Zeka tipi dağılımı (ortalama skorlar, “Top-3’e girme oranı” vb.) otomatik görselleştirilir.
   c. Zaman serisi (gün/hafta) ve basit trendler otomatik hesaplanır.
   d. Ham veriler kullanıcıya açılmaz; yalnızca anonim ve toplu grafikler görünür.

5. Sohbet Botu — Llama 3.2 3B
   a. Anketten çıkan baskın 2–3 zeka için kısa, hedefli doğrulama soruları sorar.
   b. Çelişki varsa kullanıcı açıklamasına göre küçük skor düzeltmesi (+/−) uygulanır.

6. Öneriler — Meslek ve Mentör
   a. Uygulama 3 meslek önerir.
   b. Her meslek için 1 mentör önerir.
   c. Öneriler, zeka profili + ilgi alanı uyumu + chatbot doğrulamasına dayalı kısa gerekçeyle sunulur.

3) 16 ZEKA TİPİ — KISA TANIMLAR
1. Analytical Intelligence — Veriye ve mantığa dayalı çıkarım yapar; karmaşık problemleri parçalara ayırarak çözer.
2. Creative Intelligence — Yeni fikirler üretir; farklı bağlantılar kurarak yenilikçi çözümler geliştirir.
3. Technical Intelligence — Sistem kurar; donanım-yazılım etkileşimlerini anlayıp uygular ve hata ayıklar.
4. Digital Intelligence — Dijital araçları, yazılımları ve çevrim içi ekosistemleri etkin kullanır.
5. Scientific Intelligence — Hipotez kurar; deney tasarlar ve kanıt temelli sonuçlara ulaşır.
6. Strategic Intelligence — Uzun vadeli hedef koyar; yol haritası planlar ve doğru önceliklendirir.
7. Linguistic Intelligence — Dil ve anlatımda güçlüdür; net ve ikna edici iletişim kurar.
8. Logical-Mathematical Intelligence — Sembolik akıl yürütme ve sayısal ilişkilerde üstündür.
9. Spatial-Visual Intelligence — 2B/3B zihinsel canlandırma, tasarım ve görsel kompozisyonda iyidir.
10. Interpersonal (Social) Intelligence — İnsanlarla çalışır; empati ve takım dinamiklerini iyi yönetir.
11. Intrapersonal Intelligence — Kendi motivasyonlarını tanır; öz-farkındalık ve öz-düzenleme becerisi yüksektir.
12. Emotional Intelligence — Duyguları okur ve yönetir; çatışma çözümü ve ilişki yönetiminde etkilidir.
13. Leadership Intelligence — Yön verir; ilham olur; karar alır ve sorumluluğu üstlenir.
14. Entrepreneurial Intelligence — Fırsatları görür; riskleri yönetir; sürdürülebilir değer üretir.
15. Pedagogical (Teaching) Intelligence — Bilgiyi yapılandırır; başkalarına açık ve anlaşılır aktarır.
16. Practical (Hands-on) Intelligence — El becerileriyle üretir; sahada pratik ve uygulanabilir çözümler geliştirir.

4) PUANLAMA VE BİRLEŞTİRME
a. Cevap Değerleri
   - Her soru üç seçeneklidir: Hayır = -0.4, Kararsız = 0.2, Eminim = 1.0.
   - Her soru için ağırlık w ∈ {2, 3, 4} (zeka tipine ve soruya göre tanımlanır).
b. Zeka Tipi Ham Skoru — Ağırlıklı Ortalama
   - Bir zeka tipi için N ∈ {2, 3, 4} soru bulunur.
   - Ham skor = (Σ w_i * r_i) / (Σ w_i) , r_i ∈ {-0.4, 0.2, 1.0}.
   - Teorik aralık: [-0.4, 1.0].
c. Normalize Etme [0–100]
   - normalized = ((ham + 0.4) / 1.4) * 100
   - İlgi alanı uyumu bonusu (ör. +3..+5) eklenir; değer 0–100 aralığına kırpılır.
d. Top-3 Zeka Seçimi
   - En yüksek üç zeka tipi seçilir.
   - Eşitlikte sırasıyla: ilgi uyumu > toplam ağırlık (Σ w_i) > cevap çeşitliliği (varyans) “tie-breaker” olarak kullanılır.
e. Chatbot Düzeltmesi
   - Llama 3.2 doğrulama sorularından sonra küçük düzeltme (±) uygulanır.
   - Son skorlar 0–100 aralığına tekrar kırpılır.

5) ANALİTİK VE DASHBOARD
a. Toplam yanıtlayıcı sayısı otomatik hesaplanır.
b. Zeka tipi dağılımı otomatik görselleştirilir.
   - Ortalama skorlar, veya
   - “Top-3’e girme oranı” yüzdeleri.
c. Zaman serisi
   - Gün/hafta bazında doldurma sayıları,
   - Basit trendler.
d. Not
   - Ham verilere erişim yoktur; yalnızca anonim ve toplu göstergeler paylaşılır.

6) SOHBET BOTU — Llama 3.2 3B
a. Amaç
   - Top-3 zeka tipini kısa sorularla doğrulamak ve gri alanları netleştirmek.
b. İlke
   - Her baskın zeka tipi için 1–2 hedefli mini senaryo veya kapalı uçlu soru.
   - Cevaplara göre küçük skor düzeltmesi; kararsızlıkta önceki anket ağırlıkları korunur.

7) ÖNERİLER — Meslek ve Mentör
a. Meslek
   - Top-3 zeka + ilgi alanı uyumu + chatbot doğrulamasıyla 3 meslek önerilir.
b. Mentör
   - Her meslek için 1 mentör; alan tecrübesine göre eşleştirilir.
c. Çıktı Açıklaması
   - “Neden bu meslek/mentör?” kısa gerekçesi gösterilir.

8) TEKNİK ALTYAPI VE MİMARİ
a. Mobil Uygulama
   - Android uygulaması Kotlin ile geliştirilir.
b. Veritabanı
   - Kalıcı depolama PostgreSQL üzerinde tutulur (UTF-8, ACID; gerekirse JSONB alanlar).
c. API Katmanı
   - REST/JSON uç noktaları üzerinden istemci-sunucu haberleşmesi sağlanır.
d. Kimlik Doğrulama ve Yetkilendirme
   - Token tabanlı yaklaşım (ör. JWT) ve rol/izin modeli uygulanır.
e. Veri İşleme Hattı
   - Anket yanıtları ve ilgi alanı seçimleri sunucuya aktarılır, ağırlıklandırma ve normalize işlemleri arka planda yürütülür.
f. Analitik ve Görselleştirme
   - Zeka dağılımı ve toplam sayılar için sunucu tarafı toplulaştırma; istemci tarafında grafik bileşenleri.
g. Chatbot Entegrasyonu
   - Llama 3.2 3B için bir inference servisi üzerinden soru/cevap akışı; doğrulama çıktıları skor modeline küçük düzeltme olarak yansıtılır.
h. Günlükleme ve İzleme
   - Anonimleştirilmiş olay günlükleri, hata takip ve temel performans metrikleri toplanır.

9) VERİ MODELİ
a. User: user_id, created_at, demographics (opsiyonel)
b. SurveyResponse: response_id, user_id, interests_selected[], answers[48], scores{16}, top3[]
c. ChatValidation: response_id, adjustments{}, notes
d. Recommendations: response_id, jobs[3], mentors[3], rationale

10) GİZLİLİK VE İZİNLER
a. Veriler yalnızca anonim analiz ve kişisel öneri üretimi için kullanılır.
b. Ham veriler uygulamada otomatik işlenir; kullanıcı yalnızca anonim/özet sonuçları görür.
c. Ekip, toplanan anonim/özet verilerle sistemi doğrular; bu süreç kullanıcı akışının parçası değildir.

11) YOL HARİTASI
a. Dashboard metriklerinin genişletilmesi (segment, yaş/alan kırılımları — anonim)
b. Mentör havuzu metadata: uzmanlık, kıdem, müsaitlik
c. Öneri açıklamalarını zenginleştirme (kariyer yolu, öğrenme kaynakları)
d. Teknik mimari ve entegrasyon dokümantasyonu

12) HIZLI TEST
a. Uygulamada “Anketi Başlat” ile 48 soruyu doldur.
b. Uygulama cevapları arka planda otomatik işler, puanları hesaplar ve dashboard’u gösterir.
c. Sohbet botu otomatik kısa doğrulamayı yapar.
d. Uygulama 3 meslek + 3 mentör önerisini ve kısa gerekçelerini sunar.
- Not — ekip içi: Biz, kullanıcıdan toplanan anonim/özet verilerle sadece sistemi doğruladık; manuel işlem adımı kullanıcı akışında yoktur.
