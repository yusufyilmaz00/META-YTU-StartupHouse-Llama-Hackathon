# app/main.py
from fastapi import FastAPI, Body
from fastapi.middleware.cors import CORSMiddleware
from fastapi import Depends, HTTPException
from pydantic import BaseModel, EmailStr, Field
from typing import Optional, Dict, Any
from supabase import Client
from .supa import get_supabase
from .deps import auth_required
from groq import Groq
import os
from .decision_agent import find_best_job, generate_mentor_suggestion
from .rag_manager import all_rag

GROQ_API_KEY = os.getenv("GROQ_API_KEY", "")


app = FastAPI(title="Hackathon Backend", version="0.1.0")
client = Groq(api_key=GROQ_API_KEY)



# CORS (mobil geliştirme için şimdilik açık; ileride kısıtlarız)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],      # PROD'da domain bazlı kısıtlayacağız
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
def warmup_rag():
    global _rag_loaded
    if not _rag_loaded:
        try:
            res = all_rag()
            # İstersen loglamak istersen:
            print("🔄 RAG warmup:", res)
            _rag_loaded = True
        except Exception as e:
            # Burada hata alırsan endpoint'ler yine de çalışır;
            # sadece RAG context’i boş olabilir.
            print(f"❌ RAG warmup hata: {e}")

@app.get("/")
def health():
    return {"ok": True, "service": "hackathon-backend", "version": "0.1.0"}



class RegisterIn(BaseModel):
    email: EmailStr
    password: str = Field(min_length=6)
    metadata: Optional[Dict[str, Any]] = None  # (opsiyonel) Supabase user metadata

class RegisterOut(BaseModel):
    user_id: str
    email: EmailStr
    requires_email_verification: bool

@app.post("/auth/register", response_model=RegisterOut, tags=["auth"])
def register(payload: RegisterIn, sb: Client = Depends(get_supabase)):
    """
    Supabase Auth ile email/password kayıt.
    Eğer Supabase'de 'Confirm email' açıksa, kullanıcı mail onayı yapana kadar session oluşmayabilir.
    """
    try:
        options = {}
        if payload.metadata:
            options["data"] = payload.metadata

        result = sb.auth.sign_up({
            "email": str(payload.email),
            "password": payload.password,
            "options": options or None
        })

        user = result.user
        session = result.session  # confirm email açıksa çoğu zaman None

        if not user:
            raise HTTPException(status_code=400, detail="Kayıt başarısız")

        return RegisterOut(
            user_id=user.id,
            email=user.email,
            requires_email_verification=(session is None),
        )
    except Exception as e:
        # Supabase kütüphanesi hata mesajını taşıyalım (ör: 'User already registered')
        raise HTTPException(status_code=400, detail=f"Register error: {e}")

# app/main.py (devamına ekle)


class LoginIn(BaseModel):
    email: EmailStr
    password: str

class LoginOut(BaseModel):
    user_id: str
    email: EmailStr
    access_token: str
    refresh_token: str
    provider_token: Optional[str] = None  # (Google vs. kullanırsan gelir)

@app.post("/auth/login", response_model=LoginOut, tags=["auth"])
def login(payload: LoginIn, sb: Client = Depends(get_supabase)):
    """
    Supabase Auth ile email/password login.
    Dönüş: access_token (JWT), refresh_token
    """
    try:
        result = sb.auth.sign_in_with_password({
            "email": str(payload.email),
            "password": payload.password
        })

        session = result.session
        user = result.user

        if not session or not user:
            raise HTTPException(status_code=401, detail="Geçersiz kimlik bilgileri")

        return LoginOut(
            user_id=user.id,
            email=user.email,
            access_token=session.access_token,
            refresh_token=session.refresh_token,
            provider_token=getattr(session, "provider_token", None)
        )
    except Exception as e:
        # Örn: AuthApiError('Invalid login credentials')
        raise HTTPException(status_code=401, detail=f"Login error: {e}")

@app.get("/me", tags=["auth"])
def me(auth=Depends(auth_required)):
    """
    Korumalı endpoint. Authorization: Bearer <access_token> ile erişilir.
    JWT claims'ten user bilgisini döner.
    """
    claims = auth["claims"]
    
    # JWT claims'te genellikle şu bilgiler bulunur:
    # - sub: user_id
    # - email: kullanıcı emaili
    # - user_metadata: kayıt sırasında eklenen metadata
    
    return {
        "user_id": claims.get("sub"),
        "email": claims.get("email"),
        "role": claims.get("role"),
        "aud": claims.get("aud"),
        "exp": claims.get("exp"),
        "iat": claims.get("iat"),
        "user_metadata": claims.get("user_metadata", {}),
        "app_metadata": claims.get("app_metadata", {}),
    }




def system_prompt():
    return (
        "You are a professional career counselor and cognitive analyst. "
        "Your role is to help users explore career paths, understand their job-related skills, and plan long-term professional growth. "
        "If a user asks about topics unrelated to careers, politely respond with: "
        "'I wasn't given any information on this topic. I can only answer career counseling questions.' "
        "When the user provides intelligence percentages, analyze them carefully and ask clarifying questions "
        "to test the accuracy and consistency of those values. You may adjust the percentages up or down "
        "based on the user's answers, but always explain your reasoning clearly and logically. "
        "Limit yourself to asking a maximum of five questions during this analysis process. "
        "After evaluating the intelligence profile, summarize your findings by identifying the user's cognitive strengths and weaknesses, "
        "and suggest the most suitable career paths and job roles that align with their intelligence profile. "
        "Finally, provide clear, actionable recommendations for professional growth tailored to their unique combination of intelligences. "
    )



# --- Global history ---

history = [{"role": "system", "content": system_prompt()}]
def ask_groq(prompt: str, max_tokens: int = 1000, temperature: float = 0.7):
    """
    Groq modeline sistem prompt + geçmişle birlikte istek atar.
    """
    # Geçmiş + yeni mesaj
    messages = history + [{"role": "user", "content": prompt}]
    
    try:
        completion = client.chat.completions.create(
            model="llama-3.1-8b-instant",
            messages=messages,
            temperature=temperature,
            max_completion_tokens=max_tokens,
            top_p=1,
            stream=False  # Tek seferde JSON cevabı
        )
        answer = completion.choices[0].message.content.strip()
        # History'e ekle
        history.append({"role": "user", "content": prompt})
        history.append({"role": "assistant", "content": answer})
        return answer
    except Exception as e:
        return f"Model hatası: {e}"



@app.post("/chat")
def chat_endpoint(user_input: str = Body(..., embed=True)):
    """
    Kullanıcıdan gelen normal sohbet mesajlarını işler.
    """
    answer = ask_groq(user_input)
    return {"response": answer}

@app.post("/intelligence")
def intelligence_endpoint(ratios: dict = Body(...)):
    """
    Zeka oranlarını modele gönderir.
    Örnek istek:
    {
      "analytical": 40,
      "emotional": 35,
      "creative": 25
    }
    """
    ratios_str = ", ".join([f"{k}: {v}%" for k, v in ratios.items()])
    prompt = (
        f"Here are the intelligence ratios for an individual: {ratios_str}. "
        "Carefully analyze whether these percentages appear realistic and balanced. "
        "Ask insightful diagnostic questions (no more than five) to verify their accuracy. "
        "Based on the user's responses, you may adjust the ratios upward or downward, "
        "explaining your reasoning clearly at each step. "
        "Once the analysis is complete, summarize the final intelligence profile, "
        "highlighting the individual’s cognitive strengths and areas for improvement. "
        "Finally, suggest the most suitable career paths and specific job roles that align with their intelligence profile, "
        "and provide clear, practical recommendations for their professional growth and development."
    )

    answer = ask_groq(prompt)
    return {"response": answer}


def job_recommendation_prompt(user_input: str, last_chat: str = "") -> str:
    return (
        "You are a professional career counselor. "
        "Based on the user's input and the previous conversation, suggest the most suitable job roles or career paths. "
        "Consider their skills, preferences, and potential. "
        "Limit your recommendations to the **top five most relevant professions**, "
        "and give a short explanation for each suggestion. "
        f"Previous conversation: {last_chat}\n"
        f"User input for job recommendation: {user_input}"
    )


@app.post("/jobrecommendation")
def job_recommendation_endpoint(ratios: dict = Body(...)):
    """
    Kullanıcıdan gelen iş önerisi isteklerini işler.
    Önceki sohbeti prompta ekleyerek daha bağlamsal öneri üretir.
    """
    # Son assistant cevabını al
    answer = find_best_job(metrics=ratios)
    return {"response": answer}


@app.post("/findmentor")
def find_mentor_endpoint(ratios: dict = Body(...)):
    """
    Kullanıcıdan gelen mentor önerisi isteklerini işler.
    """
    # Son assistant cevabını al
    answer = generate_mentor_suggestion(suggested_job="", metrics=ratios)
    return {"response": answer}
