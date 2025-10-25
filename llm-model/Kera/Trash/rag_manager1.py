# rag_manager1.py (düzeltilmiş)

import os
import json
from typing import List, Dict, Any, Optional, Tuple

from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_chroma import Chroma
from langchain_core.documents import Document
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser

from config import (
    TEXT_SPLITTER,
    EMBEDDINGS,
    COLLECTION_MARKET,
    COLLECTION_MENTORS,
    LLM_TEMP_HIGH,   # mentor/job üretimi için
)

# -----------------
# PROMPT TANIMLARI
# -----------------
MENTOR_PROMPT = ChatPromptTemplate.from_template("""
You are a mentor matchmaker.
CONTEXT:
{mentor_context}

SUGGESTED JOB: {suggested_job}

TASK:
Write a very concise recommendation in Turkish.
Rules:
- Exactly 4 short lines:
  1) Mentor: <İsim> - <Ünvan>
  2) Neden eşleşme: <1 cümle>
  3) İpucu: <1 cümle>
  4) Sonraki adım: <1 cümle>
- Toplamda en fazla 60 kelime.
- Başka hiçbir şey yazma.
""")

JOB_PROMPT = ChatPromptTemplate.from_template("""
You are a strategic career planner.
CONTEXT:
{market_context}

METRICS:
{metrics}

TASK:
Return ONLY the single best job title for this user.
Rules:
- One line.
- Max 3 words.
- No extra text, no punctuation.
""")

# -----------------------------------
# DÜZELTİLEN DOSYA YOLLARI & YÜKLEME
# -----------------------------------
INITIAL_JOBS_PATH = os.path.join("dataset", "jobs.json")
INITIAL_MENTORS_PATH = os.path.join("dataset", "mentors.json")

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
MOCK_OCCUPATIONS = load_json_data(INITIAL_JOBS_PATH)

# -----------------------
# SİNK & RETRIEVE FONKS.
# -----------------------
def sync_data_to_rag(
    collection: Chroma, 
    data: List[Dict[str, Any]], 
    id_key: str, 
    content_key: str
) -> None:
    print(f"\n--- RAG Senkronizasyonu Başlıyor: {collection._collection.name} ---")

    documents: List[Document] = []
    for item in data:
        content = item.get(content_key, "") or ""
        if not content.strip():
            continue
        metadata = item.copy()
        documents.append(Document(page_content=content, metadata=metadata))

    chunks = TEXT_SPLITTER.split_documents(documents)
    print(f"   -> {len(data)} öğe yüklendi. {len(chunks)} parçaya ayrıldı (Chunking).")

    try:
        current_ids = collection.get().get('ids', [])
        if current_ids:
            collection.delete(ids=current_ids)
            print(f"   -> Eski {len(current_ids)} kayıt temizlendi.")
    except Exception:
        pass

    if chunks:
        collection.add_documents(chunks)   # embedding_function koleksiyona verildi
        print("   ✅ Veriler ChromaDB'ye Başarıyla Yüklendi ve Vektörleştirildi.")
    else:
        print("   ❌ UYARI: Yüklenecek geçerli bir metin bulunamadı.")

def retrieve_context(collection: Chroma, query: str, n_results: int = 1, max_chars: int = 400) -> str:
    retriever = collection.as_retriever(search_kwargs={"k": n_results})
    docs = retriever.invoke(query)
    if not docs:
        return ""

    chunks = []
    for doc in docs:
        name = doc.metadata.get('name', doc.metadata.get('title', 'Bilinmeyen Kaynak'))
        content = (doc.page_content or "").strip()
        if len(content) > max_chars:
            content = content[:max_chars] + "..."
        chunks.append(f"KAYNAK: {name}\nÖZET: {content}")

    return "\n---\n".join(chunks)

# -------------------------------
# METRİK YÜKLEME & ÖNERİ FONKS.
# -------------------------------
def _load_metrics_dict(metrics_path: str) -> Dict[str, int]:
    """dataset/metrics.json dosyasını sözlük olarak yükler ve tüm değerleri int'e çevirir."""
    if not os.path.exists(metrics_path):
        print(f"❌ HATA: Metrik dosyası bulunamadı: {metrics_path}")
        return {}
    try:
        with open(metrics_path, "r", encoding="utf-8") as f:
            data = json.load(f)
        if isinstance(data, dict):
            return {str(k): int(v) for k, v in data.items()}
        print(f"❌ HATA: {metrics_path} beklenen dict formatında değil.")
        return {}
    except Exception as e:
        print(f"❌ HATA: Metrik dosyası okunamadı: {metrics_path} -> {e}")
        return {}

def recommend_job_and_mentor_from_dataset(
    metrics_path: str = "dataset/metrics.json",
    job_ctx_k: int = 1,
    mentor_ctx_k: int = 1
) -> Dict[str, str]:
    metrics = _load_metrics_dict(metrics_path)
    if not metrics:
        return {"suggested_job": "Genel Uzman", "mentor_message": "Metrikler okunamadı.", "debug": {}}

    # Top-3 metrik
    top_3 = dict(sorted(metrics.items(), key=lambda kv: kv[1], reverse=True)[:3])

    # JOB
    job_query = f"Strongest traits: {top_3}. Best-fit job roles?"
    job_context = retrieve_context(COLLECTION_MARKET, job_query, n_results=job_ctx_k, max_chars=400)
    suggested_job = (JOB_PROMPT | LLM_TEMP_HIGH | StrOutputParser()).invoke({
        "market_context": job_context,
        "metrics": json.dumps(metrics, ensure_ascii=False)
    }).strip()
    if not suggested_job:
        suggested_job = "Software Architect"  # güvenli varsayılan

    # MENTOR
    mentor_query = f"Best single mentor for '{suggested_job}' considering scores: {metrics}"
    mentor_context = retrieve_context(COLLECTION_MENTORS, mentor_query, n_results=mentor_ctx_k, max_chars=400)
    mentor_message = (MENTOR_PROMPT | LLM_TEMP_HIGH | StrOutputParser()).invoke({
        "mentor_context": mentor_context,
        "suggested_job": suggested_job
    }).strip() or "Mentor bulunamadı."

    return {
        "suggested_job": suggested_job,
        "mentor_message": mentor_message,
        "debug": {
            "job_context": job_context,
            "mentor_context": mentor_context
        }
    }

# -----------------
# DOĞRUDAN ÇALIŞTIR
# -----------------
if __name__ == "__main__":
    # Koleksiyonların daha önce sync_data_to_rag ile doldurulmuş olması gerekir.
    # Gerekirse:
    # sync_data_to_rag(COLLECTION_MARKET, MOCK_OCCUPATIONS, "id", "details")
    # sync_data_to_rag(COLLECTION_MENTORS, MOCK_MENTORS, "id", "bio")

    result = recommend_job_and_mentor_from_dataset("dataset/metrics.json")
    print("\n=== ÖNERİ SONUÇ ===")
    print("Meslek:", result["suggested_job"])
    print("\nMentor Mesajı:\n", result["mentor_message"])
    print("\n--- DEBUG ---")
    print("Job Context:\n", result["debug"]["job_context"][:500], "...\n")
    print("Mentor Context:\n", result["debug"]["mentor_context"][:500], "...\n")
