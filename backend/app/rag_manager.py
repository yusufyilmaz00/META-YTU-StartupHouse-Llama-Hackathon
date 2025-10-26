# app/rag_manager.py
import os
import json
from typing import List, Dict, Any
from langchain_chroma import Chroma
from langchain_core.documents import Document
from .config import TEXT_SPLITTER, COLLECTION_JOBS, COLLECTION_MENTORS

# ---------------------------------------------------------------------
# JSON Yükleme
# ---------------------------------------------------------------------

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

# Dataset dosyalarının global path'i
JOBS_PATH = os.path.join(BASE_DIR, "dataset", "jobs.json")
MENTORS_PATH = os.path.join(BASE_DIR, "dataset", "mentors.json")

print("JOBS_PATH:", JOBS_PATH)
print("MENTORS_PATH:", MENTORS_PATH)
"""
JOBS_PATH = os.path.join("dataset", "jobs.json")
print("JOB PATH:", JOBS_PATH)
MENTORS_PATH = os.path.join("dataset", "mentors.json")
"""
def load_json_data(file_path: str):
    try:
        with open(file_path, "r", encoding="utf-8") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"❌ HATA: RAG veri dosyası bulunamadı: {file_path}")
        return []
    except json.JSONDecodeError:
        print(f"❌ HATA: '{file_path}' geçerli bir JSON formatında değil.")
        return []

MOCK_JOBS = load_json_data(JOBS_PATH) or []
MOCK_MENTORS = load_json_data(MENTORS_PATH) or []

# ---------------------------------------------------------------------
# İçerik Birleştiriciler (Jobs / Mentors)
# ---------------------------------------------------------------------
def build_job_content(item: Dict[str, Any]) -> str:
    name = item.get("name", "")
    details = item.get("details", "")
    intelligences = item.get("intelligences", []) or []
    intelligences_str = ", ".join(map(str, intelligences))
    return (
        f"JOB: {name}\n"
        f"DETAILS: {details}\n"
        f"RELEVANT INTELLIGENCES: {intelligences_str}"
    ).strip()

def build_mentor_content(item: Dict[str, Any]) -> str:
    name = item.get("name", "")
    field = item.get("field", "")
    biography = item.get("biography", "")
    knowledge_areas = item.get("knowledge_areas", []) or []
    dominant = item.get("dominant_intelligences", []) or []
    knowledge_str = ", ".join(map(str, knowledge_areas))
    dominant_str = ", ".join(map(str, dominant))
    return (
        f"MENTOR: {name} — {field}\n"
        f"BIO: {biography}\n"
        f"KNOWLEDGE AREAS: {knowledge_str}\n"
        f"DOMINANT INTELLIGENCES: {dominant_str}"
    ).strip()

# ---------------------------------------------------------------------
# Metadata temizliği
# ---------------------------------------------------------------------
def _sanitize_metadata(md: Dict[str, Any]) -> Dict[str, Any]:
    clean: Dict[str, Any] = {}
    for k, v in md.items():
        if isinstance(v, (str, int, float, bool)) or v is None:
            clean[k] = v
        elif isinstance(v, list):
            try:
                clean[k] = ", ".join(map(str, v))
            except Exception:
                clean[k] = json.dumps(v, ensure_ascii=False)
        elif isinstance(v, dict):
            clean[k] = json.dumps(v, ensure_ascii=False)
        else:
            clean[k] = str(v)
    return clean

# ---------------------------------------------------------------------
# Ortak Senkronizasyon (koleksiyon temizle + chunk + ekle)
# ---------------------------------------------------------------------
def sync_documents(collection: Chroma, items: List[Dict[str, Any]], kind: str) -> Dict[str, int]:
    docs: List[Document] = []
    for it in items:
        content = build_job_content(it) if kind == "job" else build_mentor_content(it)
        if not content:
            continue

        md = {**it, "_kind": kind}
        if "id" in it:
            md["_source_id"] = str(it["id"])
        md = _sanitize_metadata(md)

        docs.append(Document(page_content=content, metadata=md))

    chunks = TEXT_SPLITTER.split_documents(docs)

    removed = 0
    try:
        current_ids = collection.get().get("ids", [])
        if current_ids:
            collection.delete(ids=current_ids)
            removed = len(current_ids)
    except Exception:
        pass

    added = 0
    if chunks:
        collection.add_documents(chunks)
        added = len(chunks)

    return {"removed": removed, "added": added, "items": len(items)}

# ---------------------------------------------------------------------
# Hepsini yükle
# ---------------------------------------------------------------------
def all_rag() -> Dict[str, Any]:
    res_jobs = sync_documents(COLLECTION_JOBS, MOCK_JOBS, kind="job")
    res_ment = sync_documents(COLLECTION_MENTORS, MOCK_MENTORS, kind="mentor")
    return {
        "jobs": res_jobs,
        "mentors": res_ment,
        "warnings": {
            "jobs_json_empty": not bool(MOCK_JOBS),
            "mentors_json_empty": not bool(MOCK_MENTORS),
        }
    }

# ---------------------------------------------------------------------
# RAG Sorgu
# ---------------------------------------------------------------------
def retrieve_context(collection: Chroma, query: str, n_results: int = 3, max_chars: int = 600) -> str:
    retriever = collection.as_retriever(search_kwargs={"k": n_results})
    relevant_docs = retriever.invoke(query)

    if not relevant_docs:
        return "Hata: İlgili bağlam bulunamadı."

    ctx = []
    for doc in relevant_docs:
        name = (
            doc.metadata.get("name")
            or doc.metadata.get("title")
            or doc.metadata.get("_source_id")
            or "Bilinmeyen Kaynak"
        )
        content = (doc.page_content or "").strip()
        if max_chars and len(content) > max_chars:
            content = content[:max_chars] + "..."
        ctx.append(f"KAYNAK: {name}\nİÇERİK: {content}")
    return "\n---\n".join(ctx)