# /main/prompts.py
from langchain_core.prompts import ChatPromptTemplate

# =========================
# 🎯 SORU ÜRETİM PROMPTU
# =========================
QUESTION_PROMPT = ChatPromptTemplate.from_template("""
    # ROLE: Expert Career Evaluator
    # GOAL: Verify the accuracy of the user's current skill scores.

    # CONTEXT:
    Current Metrics (Scores 0-100): {metrics}
    RAG Mentor Context (for inspiration): {rag_context}

    # INSTRUCTIONS:
    1. Select ONE metric from the list that would be most revealing to verify (e.g., the highest, lowest, or a key skill).
    2. Formulate ONE open-ended, engaging, and behavior-based question to thoroughly test that chosen metric.
    3. Your response MUST adhere to the REQUIRED FORMAT.

    # REQUIRED FORMAT (Return ONLY this text block):
    Metric Tested: [The exact full name of the metric you chose, e.g., 'Strategic Intelligence']
    Question: [Your behavioral question text]
    """)


# =========================
# 🧠 CEVAP ANALİZ PROMPTU
# =========================
ANALYSIS_PROMPT = ChatPromptTemplate.from_template("""
# ROLE: Metric Analyst
# INSTRUCTIONS:
1. Analyze the 'User's Answer' in relation to the 'Your Question'.
2. Set 'metric_to_update' to the EXACT full name of the metric being verified (must match a key in Current Metrics).
3. Set 'direction' to 'increase', 'neutral' or 'decrease'.
4. 'amount' must be an integer between 5 and 10.

# CONTEXT:
Current Metrics: {metrics}
Your Question: "{question}"
User's Answer: "{user_answer}"

# REQUIRED JSON FORMAT (Strictly adhere to this structure):
{{
    "metric_to_update": "[English name of the metric, e.g., 'Analytical Intelligence']",
    "direction": "[increase/neutral/decrease]",
    "amount": [5-10],
    "analysis_summary": "A brief summary in English justifying the change."
}}
""")


# =========================
# 💼 MESLEK ÖNERİ PROMPTU
# =========================
JOB_PROMPT = ChatPromptTemplate.from_template("""
# ROLE: Strategic Career Planner
# CONTEXT:
Job market data and user metrics are provided below.

# DATA:
{market_context}

# USER METRICS:
{metrics}

# TASK:
Based on the above, suggest ONE best-fitting career title (e.g., 'Data Scientist').
Return ONLY the job title, without explanation.
""")


# =========================
# 🧭 MENTOR ÖNERİ PROMPTU
# =========================
MENTOR_PROMPT = ChatPromptTemplate.from_template("""
# ROLE: Inspirational Mentor Matchmaker
# CONTEXT:
{mentor_context}

# TASK:
Based on the mentor context and the suggested job '{suggested_job}',
introduce one mentor who fits the user's needs.

Return in exactly 4 lines:
1. Mentor: <Name> - <Title>
2. Why match: <1 sentence>
3. Key advice: <1 sentence>
4. Next step: <1 sentence>
""")
