# /main/main.py

import json
import copy
from typing import Dict
import time # Döngüde bekleme veya süre ölçümü için

# --- Gerekli Modüllerin Import Edilmesi ---
# decision_agent'tan tüm Agent mantığını başlatıyoruz
from decision_agent import start_full_pipeline 
# rag_manager'dan metrics verisini çekmek için (Test amacıyla)
from rag_manager import MOCK_INITIAL_METRICS 
# Chatbot'tan metrik güncelleme ve durma kriteri fonksiyonlarını alıyoruz (Aşama 0: Hazırlık)
from chat_bot import check_stopping_criteria, get_update_instruction, get_next_question


# --- TEST METRİKLERİ ---
# RAG yüklenmeden önce bu metrikleri kullanacağız.
# MOCK_INITIAL_METRICS, rag_manager tarafından JSON'dan okunmuştur.
TEST_METRICS: Dict[str, int] = MOCK_INITIAL_METRICS


def update_metrics(current_metrics: Dict[str, int], instruction: Dict) -> Dict[str, int]:
    """
    Kariyer botu analizinden gelen talimatlarla metrikleri güncelleyen yardımcı fonksiyon.
    Bu kod, production (üretim) ortamında da kullanılacak ana mantıktır.
    """
    
    metric_name = instruction.get('metric_to_update') 
    direction = instruction.get('direction')
    amount = instruction.get('amount', 0)
    
    if metric_name in current_metrics and direction in ['increase', 'decrease'] and isinstance(amount, int):
        
        before_value = current_metrics[metric_name]
        
        if direction == 'increase':
            current_metrics[metric_name] += amount
        elif direction == 'decrease':
            current_metrics[metric_name] -= amount
        
        # Değerleri 0-100 aralığında tut
        current_metrics[metric_name] = max(0, min(100, current_metrics[metric_name]))
        
        # Konsol çıktısı
        print("\n" + "="*60)
        print(f"✅ Model Analizi: {instruction.get('analysis_summary', 'N/A')}")
        print(f"Metrik Güncellendi: '{metric_name}' ({direction} {amount})")
        print(f"Önceki Değer: {before_value} -> Yeni Değer: {current_metrics[metric_name]}")
        print("="*60)
    elif direction == 'neutral':
        # Neutral kararı durumunda sadece bilgi ver
        print("\n" + "="*60)
        print(f"ℹ️ Model Analizi: {instruction.get('analysis_summary', 'N/A')}")
        print("Metrik güncellenmedi (Nötr Karar).")
        print("="*60)
    else:
        print(f"--- UYARI: Modelin belirttiği '{metric_name}' metriği bulunamadı veya talimat hatalı.")
        
    return current_metrics


def run_sorgulama_loop(initial_metrics: Dict[str, int]):
    """
    Durma koşulu gelene kadar LLM destekli sorgulama döngüsünü yönetir.
    """
    
    current_metrics = copy.deepcopy(initial_metrics)
    conversation_history = []
    turn_count = 0
    MAX_TURNS = 10 # Sonsuz döngüden kaçınmak için bir üst sınır
    
    print("\n\n" + "#"*70)
    print("                2. 🗣️ SORGULAMA VE DOĞRULAMA DÖNGÜSÜ BAŞLADI")
    print(f"Başlangıç Metrikleri: {json.dumps(current_metrics, indent=2)}")
    print("#"*70)
    
    while turn_count < MAX_TURNS: 
        
        # --- DURMA KOŞULU KONTROLÜ ---
        if check_stopping_criteria(current_metrics):
            print("\n🎉 KAPANMA KRİTERİ KARŞILANDI! Metrikler belirginleşti.")
            break
            
        turn_count += 1
        
        # 1. Soru üretimi (Chatbot)
        question_from_llm, metric_tested = get_next_question(current_metrics)
        
        if not question_from_llm:
            print("Model bir soru üretemedi.")
            break

        print(f"\n[TUR {turn_count}/{MAX_TURNS}]\nTest Edilen Metrik: {metric_tested}")
        user_answer = input(f"Soru: {question_from_llm}\nCevabınız: ")
        
        conversation_history.append(f"AI: {question_from_llm}")
        conversation_history.append(f"USER: {user_answer}")

        if user_answer.lower() in ["quit", "exit", "stop"]:
            break
        
        # 2. Cevap analizi ve güncelleme talimatı al
        instruction = get_update_instruction(current_metrics, question_from_llm, user_answer)
        
        # 3. Metrikleri güncelle
        if instruction:
            current_metrics = update_metrics(current_metrics, instruction)
            print("\n--- TÜM GÜNCEL METRİKLER ---")
            print(json.dumps(current_metrics, indent=2))
            
    
    print("\n" + "="*70)
    print("   ✅ SORGULAMA FAZI SONLANDI. KARAR AŞAMASINA GEÇİLİYOR.")
    print("="*70)
    
    # 4. Agent Karar Aşamasına Geçiş (Decision Agent'ı çağır)
    # Metrikleri, final öneri için decision_agent'a gönder.
    start_full_pipeline(current_metrics)


if __name__ == "__main__":
    
    # Tüm RAG altyapısını kuran ve ana döngüyü başlatan tek bir çağrı
    run_sorgulama_loop(TEST_METRICS)