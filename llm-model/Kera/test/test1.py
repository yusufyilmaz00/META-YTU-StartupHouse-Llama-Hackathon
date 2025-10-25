# /main/main.py (Test Dosyası)

from config import LLM_TEMP_HIGH, EMBEDDINGS, COLLECTION_MENTORS, COLLECTION_MARKET, OLLAMA_URL
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser

def run_config_test():
    """
    config.py'deki tüm bileşenlerin doğru çalışıp çalışmadığını kontrol eder.
    """
    print("\n" + "="*70)
    print("                🧪 Konfigürasyon Testi Başlatılıyor 🧪")
    print("="*70)
    
    # --- 1. Ollama LLM Bağlantı Testi ---
    print("1. LLM Bağlantısı Test Ediliyor...")
    try:
        prompt = ChatPromptTemplate.from_template("What is the capital of Turkey?")
        chain = prompt | LLM_TEMP_HIGH | StrOutputParser()
        result = chain.invoke({})
        print(f"   ✅ LLM Başarılı: Cevap '{result[:30]}...' (Model: llama3)")
    except Exception as e:
        print(f"   ❌ LLM BAĞLANTI HATASI! Ollama çalışmıyor veya adres hatalı: {OLLAMA_URL}")
        print(f"   Detay: {e}")
        return

    # --- 2. Embedding Fonksiyonu Testi ---
    print("2. Embedding Fonksiyonu Test Ediliyor...")
    try:
        # Basit bir metni vektörleştirmeye çalış
        vector = EMBEDDINGS.embed_query("Test metni")
        print(f"   ✅ Embedding Başarılı: Vektör boyutu: {len(vector)}")
    except Exception as e:
        print("   ❌ EMBEDDING HATASI! nomic-embed-text modeli yüklü değil veya Ollama hatası.")
        print(f"   Detay: {e}")
        return

    # --- 3. ChromaDB Koleksiyon Testi ---
    print("3. ChromaDB Koleksiyonları Test Ediliyor...")
    try:
        # Koleksiyonların var olup olmadığını ve sayısını kontrol et
        mentor_count = COLLECTION_MENTORS._collection.count()
        market_count = COLLECTION_MARKET._collection.count()
        
        print(f"   ✅ ChromaDB Başarılı: 'mentor_tavsiyeleri' ({mentor_count} öğe)")
        print(f"   ✅ ChromaDB Başarılı: 'piyasa_verileri' ({market_count} öğe)")
        
        if mentor_count == 0 and market_count == 0:
            print("   ⚠️ Uyarı: Veritabanları boş. Lütfen verileri yüklemek için sync_data_to_rag'i çalıştırın.")
            
    except Exception as e:
        print("   ❌ CHROMADB HATASI! Veritabanı dosyaları bozuk olabilir.")
        print(f"   Detay: {e}")
        return

    print("="*70)
    print("🎉 Tüm temel modüller ve bağlantılar başarıyla kuruldu ve test edildi!")
    print("="*70)

if __name__ == "__main__":
    run_config_test()