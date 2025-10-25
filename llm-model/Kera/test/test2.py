# main.py veya ayrı bir test dosyası içinde kullanılabilir.

import time
import json
from typing import Any, List, Dict, Tuple

# rag_manager.py ve config.py'den gerekli fonksiyonlar ve objeler
from rag_manager import sync_data_to_rag, MOCK_MENTORS, MOCK_OCCUPATIONS
from config import COLLECTION_MENTORS, COLLECTION_MARKET # RAG Koleksiyon objeleri


# --- GÜNCELLENMİŞ RAG SORGULAMA İŞLEVİ (YAZAR ADI İÇİN) ---

def retrieve_context_with_author(collection_obj: Any, query_text: str, n_results: int = 3) -> List[Dict]:
    """
    Belirtilen koleksiyon objesinden sorguya en uygun metinleri çeker ve 
    HER BİR SONUCU yazar (mentor/meslek) adı ile birlikte döndürür.
    """
    try:
        # LangChain'in similarity_search metodunu kullanıyoruz
        results = collection_obj.similarity_search(
            query=query_text,
            k=n_results
        )
        
        extracted_data = []
        
        if results:
            for doc in results:
                # --- DÜZELTME BURADA ---
                # Metadata'dan mentorun/mesleğin adını veya başlığını çekiyoruz.
                # Örn: 'name' (Dr. Ayşe Yılmaz), 'title' (Büyük Veri Direktörü)
                source_name = doc.metadata.get('name', doc.metadata.get('title', 'Bilinmeyen Kaynak'))
                
                extracted_data.append({
                    "source_name": source_name, # Yeni anahtar: KAYNAK ADI için
                    "content": doc.page_content
                })
            
            print(f"🔍 RAG: {collection_obj._collection.name} koleksiyonundan {len(results)} parça çekildi.")
            return extracted_data
        
    except Exception as e:
        print(f"❌ RAG Retrieval Hatası: {e}")
        
    return []


# --- ANA TEST FONKSİYONU ---

def run_rag_sync_and_test():
    """
    RAG veritabanı senkronizasyonunu ve akıllı sorgulama testini çalıştırır.
    """
    
    # MOCK verileri zaten rag_manager.py'de yükleniyor, sadece buradan çekelim.
    MENTORS_DATA = MOCK_MENTORS
    JOBS_DATA = MOCK_OCCUPATIONS
    
    # Bu formatı kullanacağız: sync_data_to_rag(COLLECTION_MENTORS, MOCK_MENTORS, "id", "bio")
    
    print("\n" + "#"*70)
    print("           💾 RAG VERİTABANI SENKRONİZASYON VE SORGULAMA TESTİ 💾")
    print("#"*70)

    # --- 1. Mentor Verilerini Yükle (sync_data_to_rag'ın düzgün imzasıyla çağırıyoruz) ---
    print("1. Mentor Verileri Yükleniyor...")
    sync_data_to_rag(
        COLLECTION_MENTORS,
        MENTORS_DATA,
        "id", 
        "bio"
    )

    # --- 2. Piyasa Verilerini Yükle ---
    print("\n2. Piyasa Verileri Yükleniyor...")
    sync_data_to_rag(
        COLLECTION_MARKET,
        JOBS_DATA,
        "id",
        "details"
    )
    
    time.sleep(1) # Yüklemenin tamamlanması için bekleme

    # --- 3. Akıllı RAG Sorgulama Testi ---
    print("\n" + "="*70)
    print("               🔍 RAG Yazar/Kaynak Adı ile Bağlam Çekme Testi")
    print("="*70)
    
    test_query_market = "En yüksek Analitik Zeka gerektiren meslekler nelerdir, maaşları ne kadar?"
    test_query_mentor = "Girişimci olmak ve risk almak isteyenlere kimler mentorluk yapabilir?"
    
    # 3.1. Piyasa verilerinden bağlam çek
    context_market_list = retrieve_context_with_author(COLLECTION_MARKET, test_query_market, n_results=1)
    
    # 3.2. Mentor verilerinden bağlam çek
    context_mentor_list = retrieve_context_with_author(COLLECTION_MENTORS, test_query_mentor, n_results=1)
    
    print("\n" + "*"*30 + " SONUÇLAR " + "*"*30)
    
    # Çıktı kısmını, yeni anahtar ismini kullanacak şekilde güncelleyelim:
    print(f"\nSorgu: '{test_query_market}'")
    print(f"--- Piyasa Bağlamı (Meslek/Maaş) ---")
    if context_market_list:
        data = context_market_list[0]
        # KAYNAK ID yerine KAYNAK ADI'nı yazdırıyoruz
        print(f"KAYNAK ADI: {data['source_name']}") 
        print(f"İÇERİK: {data['content']}")
    
    print(f"\nSorgu: '{test_query_mentor}'")
    print(f"--- Mentor Bağlamı (Tavsiye/Yazar) ---")
    if context_mentor_list:
        data = context_mentor_list[0]
        # MENTOR ID yerine MENTOR ADI'nı yazdırıyoruz
        print(f"MENTOR ADI: {data['source_name']}") 
        print(f"TAVSİYE: {data['content']}")
        
    print("\n" + "#"*70)
    print("🎉 RAG Yöneticisi ve Yazar/Kaynak Çekme Başarılı!")
    print("#"*70)


if __name__ == "__main__":
    # time modülünü import ediyoruz
    import time
    
    # Bu test fonksiyonunu çalıştırmak için 'config.py', 'rag_manager.py' ve JSON dosyalarının 
    # doğru yerde olduğundan emin olun.
    run_rag_sync_and_test()