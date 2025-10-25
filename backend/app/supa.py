# app/supa.py
from supabase import create_client, Client
from functools import lru_cache
from dotenv import load_dotenv
import os

load_dotenv()

SUPABASE_URL = os.getenv("SUPABASE_URL", "")
ANON_KEY = os.getenv("SUPABASE_ANON_KEY", "")
SERVICE_ROLE_KEY = os.getenv("SUPABASE_SERVICE_ROLE_KEY", "")

if not SUPABASE_URL:
    raise RuntimeError("SUPABASE_URL .env içinde eksik")

@lru_cache
def get_supabase() -> Client:
    """
    Backend tarafında SERVICE_ROLE_KEY varsa onu kullanır, yoksa ANON_KEY ile çalışır.
    (Register akışı için ANON_KEY genelde yeterlidir.)
    """
    
    if not SERVICE_ROLE_KEY :
        raise RuntimeError("SERVICE_ROLE_KEY .env içinde eksik")
    return create_client(SUPABASE_URL, key)

