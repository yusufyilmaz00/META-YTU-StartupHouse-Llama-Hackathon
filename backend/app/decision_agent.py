import os
import json
from typing import Dict
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser, JsonOutputParser
from config import LLM_TEMP_HIGH, LLM_TEMP_LOW 
from rag_manager import retrieve_context, load_json_data
from .config import COLLECTION_JOBS, COLLECTION_MENTORS

METRICS_FILE_PATH = os.path.join("dataset/metrics.json")

# METRIKLERI YUKLEME

def load_initial_metrics() -> Dict[str, int]:
    print("\n✅ KULLANICI METRIKLERI YUKLENIYOR ")
    metrics_data = load_json_data(METRICS_FILE_PATH)
    # JSON yapısının sözlük (dictionary) olduğundan emin ol
    if isinstance(metrics_data, dict):
        # Tüm değerleri tam sayı (int) olarak döndür
        return {k: int(v) for k, v in metrics_data.items()}
    
    # Eğer dosya list[dict] veya boş geldiyse
    print(f"❌ UYARI: Metrics dosyası geçerli bir sözlük (Dict) formatında değil.")
    return {}

# KULLANICI SOURUSUNDAN SONUC URETME #

def get_router_intent(user_question: str) -> str:
    
    #ROUTER AGENT: Analyzes the user's question to determine the best RAG source.
    
    ROUTER_PROMPT = ChatPromptTemplate.from_template("""
        Analyze the user's question. Determine the best information source needed to answer.
        Return ONLY a JSON object with a single key: "source".
        
        Available sources: 
        1. 'RAG_MENTOR': For soft skills, values, motivation, and leadership questions.
        2. 'RAG_MARKET': For salary, required skills, and job market facts.
        3. 'NONE': If the question is purely conversational or doesn't require specific data.
        
        User Question: "{user_question}"
    """)
    
    chain = ROUTER_PROMPT | LLM_TEMP_LOW | JsonOutputParser()
    try:
        result = chain.invoke({"user_question": user_question})
        return result.get("source", "NONE").upper().strip() 
    except Exception:
        return "NONE"

# EN IYI MESLEK ONERISI #

def find_best_job(metrics: Dict[str, int]) -> str:
    """
    AGENT: Uses user metrics and Market RAG context to suggest the single best job role.
    """
    
    # Get top 3 highest scoring metrics
    sorted_metrics = sorted(metrics.items(), key=lambda item: item[1], reverse=True)
    top_3_metrics = dict(sorted_metrics[:3])
    
    # 1. RAG Query Preparation (Retrieval)
    job_query = (
        f"The user's strongest traits are: {top_3_metrics}. What are the most suitable job roles for this profile? "
    )
    
    market_context = retrieve_context(COLLECTION_JOBS, job_query, n_results=3)

    # 2. Job Suggestion LLM Prompt (English)
    JOB_PROMPT = ChatPromptTemplate.from_template("""
        # ROLE: Strategic Career Planner
        # CONTEXT (Job Market Data): {market_context}
        # USER METRICS (Scores 0-100): {metrics}
        
        # TASK: 
        Based ONLY on the CONTEXT and the user's METRICS, identify the ONE single best job role that maximizes the user's highest scores. 
        Your answer MUST be the job title only. Return ONLY the job title text.
        
        Example: Data Scientist
    """)
    
    chain = JOB_PROMPT | LLM_TEMP_HIGH | StrOutputParser()
    
    try:
        suggested_job = chain.invoke({"market_context": market_context, "metrics": json.dumps(metrics)})
        return suggested_job.strip()
    except Exception as e:
        print(f"❌ Job Suggestion Error: {e}")
        return "General Project Manager"

# MENTOR ONERI SISTEMI #

def generate_mentor_suggestion(suggested_job: str, metrics: Dict[str, int]) -> str:
    """
    INSPIRATION AGENT: Uses RAG to select and synthesize advice from one suitable mentor.
    """
    
    # 1. RAG Query Preparation (Retrieval)
    mentor_query = (
        f"Who is the single most suitable mentor to provide specific advice for a person aiming for the '{suggested_job}' role, considering their scores: '{metrics}'?"
    )
    
    # Retrieve only 1 mentor's information
    mentor_context = retrieve_context(COLLECTION_MENTORS, mentor_query, n_results=1)

    # 2. Mentor Synthesis Prompt (English)
    MENTOR_PROMPT = ChatPromptTemplate.from_template("""
        # ROLE: Inspirational Mentor Matchmaker
        # CONTEXT: Mentor Advice (from Internal RAG): {mentor_context}
        # Suggested Job: {suggested_job}
        
        # TASK: 
        Based ONLY on the Mentor Advice context, introduce the suggested mentor to the user. 
        1. State the mentor's name and title.
        2. Explain WHY this specific mentor is the best match for the user's journey to become a '{suggested_job}'.
        3. End with a quote or a key piece of advice found in the context.
        
        Your entire response MUST be motivating.
    """)
    
    chain = MENTOR_PROMPT | LLM_TEMP_HIGH | StrOutputParser()

    try:
        suggestion = chain.invoke({"mentor_context": mentor_context, "suggested_job": suggested_job})
        return suggestion.strip()
    except Exception as e:
        print(f"❌ Mentor Synthesis Error: {e}")
        return "Mentor tavsiyesi alınamadı."

# SON PROMPTU ATAN KOD #

def main_agent(metrics):
    print("\n" + "="*70)
    print("                2. 🎯 AGENT: MESLEK ÖNERİSİ BAŞLIYOR")
    print(f"Başlangıç Metrikleri:\n{json.dumps(metrics, indent=2)}")
    print("="*70)
    
    suggested_job = find_best_job(metrics)
    mentor_message = generate_mentor_suggestion(suggested_job, metrics)
    
    print(suggested_job)
    print("\n--- 📝 NİHAİ MENTOR MESAJI VE İLHAM SENTEZİ ---")
    print(mentor_message)
    print("--------------------------------------------------")
    print("\n🎉 Proje Test Akışı Başarıyla Tamamlandı.")