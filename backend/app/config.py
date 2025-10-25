import os
from dotenv import load_dotenv
from langchain_ollama import ChatOllama, OllamaEmbeddings
from langchain_chroma import Chroma  # Düzeltilmiş import
from langchain_text_splitters import RecursiveCharacterTextSplitter
from typing import Any

# --- .env dosyasını yükle (API anahtarları için) ---
load_dotenv() 

# --- LLM VE OLLAMA KONFİGÜRASYONU ---
OLLAMA_URL = os.getenv("OLLAMA_URL", "http://35.195.226.151:11434/api/generate")
LLM_MODEL = os.getenv("LLM_MODEL", "llama3.2:3b")
OLLAMA_EMBEDDING_MODEL = os.getenv("EMBED_MODEL", "http://35.195.226.151:11434/api/embeddings")

# LLM Nesneleri (Sıcaklık ayarları ile farklı roller için)
LLM_TEMP_LOW = ChatOllama(model=LLM_MODEL, temperature=0.1, format="json", base_url=OLLAMA_URL) # JSON/Analiz/Router için
LLM_TEMP_HIGH = ChatOllama(model=LLM_MODEL, temperature=0.7, base_url=OLLAMA_URL) # Yaratıcılık/Sorgu için

# --- RAG KONFİGÜRASYONU ---
DB_PATH = "chromadb_data" # RAG verilerinin saklandığı dizin

# RAG Bileşenleri
EMBEDDINGS = OllamaEmbeddings(model=OLLAMA_EMBEDDING_MODEL, base_url=OLLAMA_URL)
TEXT_SPLITTER = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=100)

def initialize_chroma_store(name: str) -> Chroma:
    """LangChain Chroma sınıfını kullanarak Vektör Mağazasını başlatır."""
    print(f"INFO: Chroma Store '{name}' başlatılıyor/oluşturuluyor.")
    
    # LangChain Chroma sınıfını kullanarak kalıcı bir mağaza oluşturuyoruz.
    # Bu, get_or_create_collection işlevini görür.
    return Chroma(
        collection_name=name,
        embedding_function=EMBEDDINGS,
        persist_directory=DB_PATH
    )

# İki ayrı koleksiyonumuzun bağlantısını başlatıyoruz
# NOT: Program başladığında bu satırlar çalışır ve bağlantıları kurar.
COLLECTION_MENTORS: Any = initialize_chroma_store("mentors")
COLLECTION_JOBS: Any = initialize_chroma_store("jobs")