# /main/main.py

import json
from typing import Dict
from rag_manager import sync_data_to_rag, MOCK_MENTORS, MOCK_OCCUPATIONS
from decision_agent import find_best_job, generate_mentor_suggestion
from config import COLLECTION_MENTORS, COLLECTION_MARKET 

# --- KULLANILACAK TEST METRİKLERİ ---
# Bu metrikler, Agent'ın karar vermesi için bir test senaryosudur.
# Yüksek skorlar: Manevi, Girişimci, Duygusal, Pratik Zeka.
# Düşük skorlar: Geleneksel, Teknik, İletişim Zekası.
TEST_METRICS: Dict[str, int] = {
  "Analytical Intelligence": 100,
  "Creative Intelligence": 80,
  "Emotional Intelligence": 85,
  "Social Intelligence": 51,
  "Technical Intelligence": 54,
  "Aesthetic Intelligence": 68,
  "Practical Intelligence": 51,
  "Natural Intelligence": 59,
  "Scientific Intelligence": 75,
  "Conventional Intelligence": 51,
  "Spiritual Intelligence": 50,
  "Strategic Intelligence": 79,
  "Entrepreneurial Intelligence": 88,
  "Linguistic Intelligence": 65,
  "Digital Intelligence": 50,
  "Communication Intelligence": 56
}


def run_full_decision_pipeline(metrics: Dict[str, int]):
    """
    Tüm RAG senkronizasyonu ve Karar Agent'ı süreçlerini test eden ana fonksiyon.
    """
    
    # --- 1. RAG Veritabanı Senkronizasyonu (Veri Yükleme) ---
    print("\n" + "#"*70)
    print("                1. 💾 RAG VERİ YÜKLEME VE SENKRONİZASYON 💾")
    print("#"*70)
    
    # Mentorları yükle (Koleksiyon objesi, Ham veri, ID anahtarı, İçerik anahtarı)
    sync_data_to_rag(COLLECTION_MENTORS, MOCK_MENTORS, "id", "bio")
    
    # İşleri yükle
    sync_data_to_rag(COLLECTION_MARKET, MOCK_OCCUPATIONS, "id", "details")
    print("\n✅ Ollama, RAG Koleksiyonları ve Veri Yüklemesi Tamamlandı.")

    
    # --- 2. Agent: En Uygun Meslek Tespiti ---
    print("\n" + "="*70)
    print("                2. 🎯 AGENT: MESLEK ÖNERİSİ BAŞLIYOR")
    print(f"Metrikler:\n{json.dumps(metrics, indent=2)}")
    print("="*70)
    
    # RAG'dan destek alarak meslek önerir
    suggested_job = find_best_job(metrics)
    
    print(f"\n✨ TESPİT EDİLEN MESLEK (Agent Kararı): {suggested_job}")
    print("="*70)
    
    
    # --- 3. Agent: Mentor Eşleştirme ve İlham Sentezi ---
    print("\n" + "#"*70)
    print("                3. 🌟 AGENT: MENTOR EŞLEŞTİRME VE SENTEZ")
    print("#"*70)
    
    # Meslek ve metrikleri kullanarak RAG'dan mentor tavsiyesi çeker ve sentezler.
    mentor_message = generate_mentor_suggestion(suggested_job, metrics)
    
    print("\n--- 📝 NİHAİ MENTOR MESAJI VE İLHAM SENTEZİ ---")
    print(mentor_message)
    print("--------------------------------------------------")


if __name__ == "__main__":
    # Bu script, tüm diğer modüllerin (config, rag_manager, decision_agent) 
    # doğru çalışıp çalışmadığını gösterir.
    run_full_decision_pipeline(TEST_METRICS)