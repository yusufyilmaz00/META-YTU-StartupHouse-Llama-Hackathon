from decision_agent import main_agent, load_initial_metrics
from rag_manager import all_rag , MOCK_JOBS, MOCK_MENTORS
from chatbot import main_chatbot

if __name__ == "__main__":
    all_rag()
    metrics = load_initial_metrics()

    if not metrics or not MOCK_MENTORS or not MOCK_JOBS:
        print("❌ UYGULAMA DURDURULDU: Kritik veri dosyaları yüklenemedi.")
        exit()
    print(metrics)
    print("\n✅ KULLANICI METRIKLERI YUKLENDI \n")
    updated_metrics = main_chatbot(
        initial_metrics=metrics,         # dataset/metrics.json'dan okunan başlangıç metrikleri
        max_rounds=10,                   # toplam en fazla 10 soru
        sep_target_k=5,                  # en az 5 metrik net ayrışana kadar sürdür
        sep_gap=12,                      # metrikler arası boşluk (gap) en az 12 puan olmalı
        sep_patience=2,                  # bu ayrışma durumu 2 tur üst üste görülürse dur
        neutral_patience=2,              # 2 tur üst üste nötr kalırsa dur
        min_net_change_threshold=2       # toplam değişim 2 puandan küçükse küçük değişim say
    )  
    main_agent(metrics=updated_metrics)
    