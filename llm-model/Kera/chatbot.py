import json
import copy
from typing import Dict, Tuple, Optional, Any, List
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser, StrOutputParser
from config import LLM_TEMP_HIGH, LLM_TEMP_LOW, COLLECTION_MENTORS # LLM ve RAG objelerini config'den al
from rag_manager import retrieve_context # RAG sorgulama fonksiyonu
from prompt import QUESTION_PROMPT

# --- YARDIMCI FOKNSİYONLAR ---

def _topk_separation_ok(
    metrics: Dict[str, int],
    k: int = 5,
    sep_gap: int = 12
) -> Tuple[bool, List[Tuple[str, int]]]:
    """
    Skorları azalan sırala ve ilk k metrik için (k-1) ardışık gap >= sep_gap mi kontrol et.
    Dönüş: (True/False, top_k_list)
    """
    if not metrics or len(metrics) < k:
        return (False, [])
    top = sorted(metrics.items(), key=lambda kv: kv[1], reverse=True)[:k]
    
    # Ardışık boşlukların hepsi eşikten büyük/eşit mi?
    gaps_ok = all(top[i][1] - top[i + 1][1] >= sep_gap for i in range(k - 1))
    
    return (gaps_ok, top)

def retrieve_mentor_context(metrics: Dict[str, int]) -> str:
    """RAG'dan mentor bağlamını çeken yardımcı fonksiyon (soru üretimine ilham vermesi için)."""
    # En yüksek ve en düşük metrikleri alarak LLM'i odakla
    sorted_metrics = sorted(metrics.items(), key=lambda item: item[1], reverse=True)
    top_and_bottom_metrics = dict(sorted_metrics[:3] + sorted_metrics[-3:])
    rag_query = f"Hangi yetenekler test edilmeli? Güçlü/Zayıf Metrikler: {top_and_bottom_metrics}"
    
    return retrieve_context(COLLECTION_MENTORS, rag_query, n_results=2)


# --- SORGULAMA VE ANALİZ FONKSİYONLARI (LLM İLE İLETİŞİM) ---

def get_next_question(metrics: Dict[str, int]) -> Tuple[Optional[str], Optional[str]]:
    try:
        rag_context = retrieve_mentor_context(metrics)

        QUESTION_PROMPT = ChatPromptTemplate.from_template("""
            # ROLE: Expert Career Evaluator
            # GOAL: Verify the accuracy of the user's current skill scores.

            # CONTEXT:
            Current Metrics (Scores 0-100): {metrics}
            RAG Mentor Context (for inspiration): {rag_context}

            # INSTRUCTIONS:
            1. Select ONE metric from the list that would be most revealing to verify (e.g., the highest, lowest, or a key skill).
            2. Formulate ONE open-ended, engaging, and behavior-based question to thoroughly test that chosen metric.
            3. Your response MUST adhere to the REQUIRED FORMAT.

            # REQUIRED FORMAT (Return ONLY this text block):
            Metric Tested: [The exact full name of the metric you chose, e.g., 'Strategic Intelligence']
            Question: [Your behavioral question text]
        """)

        chain = QUESTION_PROMPT | LLM_TEMP_HIGH | StrOutputParser()
        raw = chain.invoke({
            "metrics": json.dumps(metrics, indent=2, ensure_ascii=False),
            "rag_context": rag_context
        })

        if not raw:
            return (None, None)

        # Basit ama sağlam ayrıştırma
        text = raw.strip()
        # Önce beklenen etiketlere göre böl
        if "Metric Tested:" in text and "Question:" in text:
            try:
                metric_tested = text.split("Metric Tested:", 1)[1].split("\n", 1)[0].strip()
                question_text = text.split("Question:", 1)[1].strip()
                # Güvenlik: boşsa None
                if not metric_tested or not question_text:
                    return (None, None)
                return (question_text, metric_tested)
            except Exception:
                return (None, None)

        # Beklenen formatta değilse, fallback: tek satır soru gibi davranma
        return (None, None)

    except Exception as e:
        print(f"❌ get_next_question hata: {e}")
        return (None, None)


def get_update_instruction(metrics: Dict[str, int], question: str, user_answer: str) -> Optional[Dict]:
    """Analiz LLM'i: Cevabı analiz eder ve metrik güncelleme talimatı verir (JSON)."""
    
    # (Kod aynı kalır)
    # ...
    pass # Önceki cevapta mutabık kaldığımız get_update_instruction içeriği buraya gelecek


# --- METRİK GÜNCELLEME VE DURDURMA FONKSİYONLARI ---

def _update_metrics(current_metrics: Dict[str, int], instruction: Dict) -> Tuple[Dict[str, int], Dict[str, int]]:
    """
    Metrikleri güvenli bir şekilde günceller ve güncellenen miktarları içeren bir 'delta' sözlüğü döndürür.
    """
    metric_name = instruction.get('metric_to_update') 
    direction = instruction.get('direction')
    amount = instruction.get('amount', 0)
    deltas = {}

    # Metrikte bir değişiklik yapılacaksa
    if metric_name in current_metrics and direction in ['increase', 'decrease'] and amount > 0:
        
        before_value = current_metrics[metric_name]
        
        if direction == 'increase':
            delta = amount
        else: # 'decrease'
            delta = -amount
        
        current_metrics[metric_name] += delta
        current_metrics[metric_name] = max(0, min(100, current_metrics[metric_name])) # Sınırla
        
        # Değişim miktarını delta sözlüğüne kaydet (döngüde kullanılacak)
        deltas[metric_name] = current_metrics[metric_name] - before_value
        
        # Konsol çıktısı
        print("="*60)
        print(f"✅ Analiz Özeti: {instruction.get('analysis_summary', 'N/A')}")
        print(f"Güncellenen Metrik: '{metric_name}'")
        print(f"Önceki Değer: {before_value} -> Yeni Değer: {current_metrics[metric_name]} ({direction} {amount})")
        print("="*60)
    elif instruction.get('direction', '').lower() == 'neutral':
        print("\n" + "="*60)
        print(f"ℹ️ Analiz Özeti: {instruction.get('analysis_summary', 'N/A')}")
        print("Metrik güncellenmedi (Nötr Karar).")
        print("="*60)
        
    return current_metrics, deltas


# --- 4. ANA DÖNGÜ YÖNETİCİSİ (main_chatbot) ---

def main_chatbot(
    initial_metrics: Dict[str, int],
    max_rounds: int = 10,
    sep_target_k: int = 5,
    sep_gap: int = 12,
    sep_patience: int = 2,
    neutral_patience: int = 2,
    min_net_change_threshold: int = 2
) -> Dict[str, int]:
    
    current = copy.deepcopy(initial_metrics)

    # Streak sayaçları
    separation_streak = 0
    neutral_streak = 0
    small_change_streak = 0

    print("\n=== Doğrulama Döngüsü Başladı ===")
    print(json.dumps(current, indent=2, ensure_ascii=False))

    for r in range(1, max_rounds + 1):
        print(f"\n--- TUR {r}/{max_rounds} ---")

        # --- DURDURMA KRİTERİ KONTROLÜ (Başlangıç) ---
        separated, topk_list = _topk_separation_ok(current, k=sep_target_k, sep_gap=sep_gap)
        if separated and separation_streak >= sep_patience:
             topk_str = ", ".join([f"{n}:{s}" for n, s in topk_list])
             print(f"✅ DUR: İlk {sep_target_k} metrik net biçimde ayrıştı (gap≥{sep_gap}) ve {sep_patience} tur korundu. {topk_str}")
             break

        q_text, tested_metric = get_next_question(current)
        if not q_text:
            print("Model soru üretemedi, döngü bitiyor.")
            break

        user_answer = input(f"Soru: {q_text}\nCevabınız: ")
        if user_answer.strip().lower() in {"quit", "exit", "stop"}:
            print("Kullanıcı durdurdu.")
            break

        instr = get_update_instruction(current, q_text, user_answer)
        if not instr:
            print("Geçerli talimat alınamadı (JSON). Tur atlandı.")
            continue

        # 🔧 KRİTİK DÜZELTME: Metrik güncelleme ve delta hesaplama
        old_metrics = copy.deepcopy(current)
        current, deltas = _update_metrics(current, instr) 
        
        # Total değişim miktarını hesapla
        total_abs_delta = sum(abs(v) for v in deltas.values())

        # --- GÜNCELLEME VE STREAK YÖNETİMİ ---

        # 1. Neutral Streak Kontrolü
        if instr.get('direction', '').lower() == 'neutral' or total_abs_delta == 0:
            neutral_streak += 1
            print("🔹 Değişim yok (neutral veya uygulanamadı).")
        else:
            neutral_streak = 0
            
        # 2. Small Change Streak Kontrolü
        if total_abs_delta < min_net_change_threshold and total_abs_delta > 0:
            small_change_streak += 1
        else:
            small_change_streak = 0

        # 3. Ayrışma Streak Kontrolü
        if separated:
            separation_streak += 1
            topk_str = ", ".join([f"{n}:{s}" for n, s in topk_list])
            print(f"🏁 Ayrışma sağlandı (gap≥{sep_gap}) [streak={separation_streak}] → {topk_str}")
        else:
            separation_streak = 0
        
        # --- İKİNCİL DURDURMA ÖLÇÜTLERİ ---
        if neutral_streak >= neutral_patience:
            print(f"✅ DUR: Arka arkaya {neutral_streak} tur 'neutral' — stabil.")
            break
        if small_change_streak >= 2:
            print(f"✅ DUR: İki tur üst üste küçük değişim (<{min_net_change_threshold}).")
            break
        
        # Tur özeti
        print("Mevcut metrikler (Sıralı):")
        sorted_output = sorted(current.items(), key=lambda kv: kv[1], reverse=True)
        print(json.dumps(dict(sorted_output), indent=2, ensure_ascii=False))

    print("\n=== Doğrulama Döngüsü Bitti ===")
    print(json.dumps(current, indent=2, ensure_ascii=False))
    return current