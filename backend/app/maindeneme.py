from fastapi import FastAPI, Body
import requests, json

# --- Ollama ayarları ---
OLLAMA_URL = "http://34.140.58.17:11434/api/generate"
MODEL_NAME = "llama3"

app = FastAPI(title="Career Counselor MCP")

# --- Model bağlamı (tek sistem prompt) ---
SYSTEM_PROMPT = (
    "You are a career counselor. "
    "You help users explore career paths, understand job skills, and plan professional growth. "
    "If someone asks about topics unrelated to careers, answer: "
    "'I wasn't given any information on this topic. I can only answer career counseling questions.' "
    "When intelligence percentages are provided, analyze them and ask questions to test their accuracy. "
    "You may adjust the percentages up or down based on the user's answers, explaining your reasoning clearly. "
    "After evaluating the intelligence profile, identify the user's strengths and weaknesses, "
    "and suggest suitable career paths and job roles that align with their intelligence profile. "
    "Provide clear, actionable guidance for professional growth based on their unique combination of intelligences."
)

# --- Modelin geçmişi global olarak saklanıyor ---
history = [{"user": "SYSTEM", "assistant": SYSTEM_PROMPT}]


def ask_llama(prompt: str, max_tokens: int = 300, temperature: float = 0.7):
    """
    Ollama modeline sistem prompt + geçmişle birlikte istek atar.
    max_tokens ve temperature parametreleri ile cevabın uzunluğunu ve yaratıcılığını ayarlayabilirsin.
    """
    conversation = ""
    for turn in history:
        conversation += f"User: {turn['user']}\nAssistant: {turn['assistant']}\n"

    data = {
        "model": MODEL_NAME,
        "prompt": f"{conversation}\nUser: {prompt}\nAssistant:",
        "max_tokens": max_tokens,
        "temperature": temperature,
    }

    try:
        response = requests.post(OLLAMA_URL, json=data, timeout=60)
        response.raise_for_status()
        result = response.json()
        return result.get("text", "").strip()

    except requests.exceptions.RequestException as e:
        return f"Bağlantı hatası: {e}"


@app.post("/chat")
def chat_endpoint(user_input: str = Body(..., embed=True)):
    """
    Kullanıcıdan gelen normal sohbet mesajlarını işler.
    """
    answer = ask_llama(user_input)
    history.append({"user": user_input, "assistant": answer})
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
        f"Here are the intelligence ratios for a person: {ratios_str}. "
        "Evaluate if these percentages seem accurate by asking diagnostic questions. "
        "You can increase or decrease them based on the answers. "
        "After analyzing the responses and adjusting the ratios, "
        "identify the person's strengths and weaknesses, "
        "and suggest suitable career paths and specific job roles that align with their intelligence profile. "
        "Provide clear, actionable guidance for professional growth based on their unique combination of intelligences."
    )
    answer = ask_llama(prompt)
    history.append({"user": prompt, "assistant": answer})
    return {"response": answer}
