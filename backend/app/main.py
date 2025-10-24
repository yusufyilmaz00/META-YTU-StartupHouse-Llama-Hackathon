# app/main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi import Depends, HTTPException
from pydantic import BaseModel, EmailStr, Field
from typing import Optional, Dict, Any
from supabase import Client
from .supa import get_supabase
from .deps import auth_required


app = FastAPI(title="Hackathon Backend", version="0.1.0")

# CORS (mobil geliştirme için şimdilik açık; ileride kısıtlarız)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],      # PROD'da domain bazlı kısıtlayacağız
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

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
