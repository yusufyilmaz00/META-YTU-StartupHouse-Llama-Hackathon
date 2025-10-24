# app/deps.py
import os
from functools import lru_cache
from typing import Dict, Any

from dotenv import load_dotenv
load_dotenv()

import jwt
from jwt import PyJWKClient
from fastapi import Header, HTTPException

@lru_cache
def get_supabase_url() -> str:
    base = os.getenv("SUPABASE_URL", "").rstrip("/")
    if not base:
        raise RuntimeError("SUPABASE_URL .env içinde eksik")
    return base

@lru_cache
def get_jwks_url() -> str:
    return f"{get_supabase_url()}/auth/v1/jwks"

@lru_cache
def get_jwk_client() -> PyJWKClient:
    return PyJWKClient(get_jwks_url())

@lru_cache
def get_jwt_secret() -> str:
    secret = os.getenv("SUPABASE_JWT_SECRET", "")
    if not secret:
        raise RuntimeError("SUPABASE_JWT_SECRET .env içinde eksik (HS256 için)")
    return secret

def auth_required(authorization: str = Header(None)) -> Dict[str, Any]:
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail="Authorization header bekleniyor (Bearer <token>)")

    token = authorization.split(" ", 1)[1].strip()

    # Header/payload ön kontrol
    try:
        hdr = jwt.get_unverified_header(token)
        alg = hdr.get("alg", "")
        payload_preview = jwt.decode(token, options={"verify_signature": False})
        iss = payload_preview.get("iss", "")
        expected_iss = f"{get_supabase_url()}/auth/v1"
        if iss and iss != expected_iss:
            raise HTTPException(
                status_code=401,
                detail=f"Issuer mismatch (iss != SUPABASE_URL/auth/v1). iss={iss} expected={expected_iss}"
            )
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=401, detail=f"Invalid token format: {e}")

    # İmzayı doğrula (HS256/RS256)
    try:
        if alg == "HS256":
            claims = jwt.decode(
                token,
                get_jwt_secret(),
                algorithms=["HS256"],
                options={"verify_aud": False},
                leeway=30
            )
        elif alg == "RS256":
            signing_key = get_jwk_client().get_signing_key_from_jwt(token)
            claims = jwt.decode(
                token,
                signing_key.key,
                algorithms=["RS256"],
                options={"verify_aud": False},
                leeway=30
            )
        else:
            raise HTTPException(status_code=401, detail=f"Unsupported JWT alg: {alg}")

        return {"token": token, "claims": claims}

    except jwt.ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token expired")
    except Exception as e:
        raise HTTPException(status_code=401, detail=f"Signature verification failed: {e}")

