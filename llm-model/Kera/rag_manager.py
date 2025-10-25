import os
import json
from typing import List, Dict, Any
from langchain_chroma import Chroma
from langchain_core.documents import Document
from config import TEXT_SPLITTER, COLLECTION_JOBS, COLLECTION_MENTORS

INITIAL_JOBS_PATH = os.path.join( "dataset/jobs.json")
INITIAL_MENTORS_PATH = os.path.join("dataset/mentors.json")

# Verileri Json Okuma #

def load_json_data(file_path: str) -> List[Dict]:
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"❌ HATA: RAG veri dosyası bulunamadı: {file_path}")
        return []
    except json.JSONDecodeError:
        print(f"❌ HATA: '{file_path}' geçerli bir JSON formatında değil.")
        return []

MOCK_MENTORS = load_json_data(INITIAL_MENTORS_PATH)
MOCK_JOBS = load_json_data(INITIAL_JOBS_PATH)

# RAG IŞLEMI (TEKLI) #

def main_rag(
    collection: Chroma, 
    data: List[Dict[str, Any]], 
    id_key: str, 
    content_key: str
) -> None:
    """
    Verilen koleksiyona, ham veriyi temizleyip LangChain Document formatında ekler.
    Bu fonksiyon, parçalama, metadata ekleme ve temizleme adımlarını içerir.
    """
    
    print(f"\n--- RAG Senkronizasyonu Başlıyor: {collection._collection.name} ---")
    
    # 1. Ham Veriyi LangChain Document formatına dönüştür ve Parçala
    documents = []
    
    for item in data:
        content = item.get(content_key, "")
        metadata = item.copy() 
        doc_id = str(item.get(id_key))
        
        if not content:
            continue
            
        # Parçalama öncesi Document oluştur
        doc = Document(
            page_content=content,
            metadata=metadata
        )
        documents.append(doc)
    
    # 2. Metni Parçalara Ayır (Chunking)
    # Eğer documents listesi boşsa (yani veri yüklenemediyse), chunks boş kalacaktır.
    chunks = TEXT_SPLITTER.split_documents(documents)
    print(f"   -> {len(data)} öğe yüklendi. {len(chunks)} parçaya ayrıldı (Chunking).")
    
    # 3. Eski verileri temizle (Önceki çalıştırmalardan kalanları siler)
    try:
        current_ids = collection.get()['ids']
        if current_ids:
            collection.delete(ids=current_ids)
            print(f"   -> Eski {len(current_ids)} kayıt temizlendi.")
    except Exception as e:
        # Boş koleksiyonda hata vermesini yoksayalım
        pass 
    
    # 4. Veriyi Vektör Mağazasına Ekle
    if chunks: # <-- Artık 'chunks' değişkeni yukarıda tanımlı
        collection.add_documents(chunks) 
        print(f"   ✅ Veriler ChromaDB'ye Başarıyla Yüklendi ve Vektörleştirildi.")
    else:
        print("   ❌ UYARI: Yüklenecek geçerli bir metin bulunamadı.")

# RAG ISLEMI (HEPSI) #

def all_rag():
    main_rag(COLLECTION_MENTORS, MOCK_MENTORS, "id", "bio")
    main_rag(COLLECTION_JOBS, MOCK_JOBS, "id", "details")
    print("\n✅ RAG Koleksiyonları Aktif ve Veriler Güncellendi.")

# RAG REQUEST #

def retrieve_context(collection: Chroma, query: str, n_results: int = 3) -> str:
    
    #Verilen sorgu için ilgili bağlamı RAG veritabanından çeker.
    #LLM'e sadece metin değil, MENTOR/MESLEK adını içeren bir formatta döndürür.
    
    
    retriever = collection.as_retriever(search_kwargs={"k": n_results})
    relevant_docs = retriever.invoke(query)
    
    if not relevant_docs:
        return "Hata: İlgili bağlam bulunamadı."
        
    context_list = []
    for doc in relevant_docs:
        # Metadata'dan adı veya başlığı çek (metadata'nın doğru olduğunu varsayıyoruz)
        name = doc.metadata.get('name', doc.metadata.get('title', 'Bilinmeyen Kaynak'))
        content_summary = doc.page_content # Parçalanmış metnin kendisi
        
        # LLM'in okuması için mentor adını içeren bir format oluşturuyoruz
        context_list.append(f"KAYNAK ADI: {name}\nİÇERİK: {content_summary}")

    context_text = "\n---\n".join(context_list)
    
    return context_text
