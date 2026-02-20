# Yük Dengeleyici Simülasyonu

Bu proje, farklı yük dengeleme algoritmalarını karşılaştıran bir Java simülasyonu sunar. Softmax (pekiştirmeli öğrenme tabanlı), Round Robin ve Random algoritmalarının gerçek dünya benzeri sunucu gecikmelerine karşı performansını ölçer.

---

## Proje Yapısı

```
com/example/loadbalancer/
├── LoadBalancer.java         # Yük dengeleyici arayüzü
├── Server.java               # Sunucu arayüzü
├── SimulatedServer.java      # Rastgele gecikme üreten simüle edilmiş sunucu
├── SimulationResult.java     # Simülasyon sonuçlarını tutan veri sınıfı
├── SoftmaxLoadBalancer.java  # Softmax tabanlı öğrenen yük dengeleyici
├── RoundRobinBalancer.java   # Sıralı yük dengeleyici
├── RandomBalancer.java       # Rastgele yük dengeleyici
└── Main.java                 # Giriş noktası ve simülasyon döngüsü
```

---

## Algoritmalar

### 🧠 Softmax Yük Dengeleyici
Pekiştirmeli öğrenme prensibini kullanan adaptif bir algoritma. Her sunucu için bir Q-değeri tutar ve bu değerleri gelen gecikme geri bildirimlerine göre günceller. Sunucu seçimi, sıcaklık parametresi `tau` ile kontrol edilen bir softmax dağılımına göre yapılır.

- **Avantaj:** Zamanla en düşük gecikmeli sunuculara yönelir; değişen koşullara adapte olur.
- **Parametreler:**
  - `tau` — Keşif sıcaklığı (düşük = açgözlü, yüksek = rastgele)
  - `alpha` — Öğrenme oranı

#### 📐 Softmax Formülü

Softmax yük dengeleme algoritmasında her sunucunun seçilme olasılığı aşağıdaki formül ile hesaplanır:

$$P(i) = \frac{e^{Q_i / \tau}}{\sum_{j=1}^{K} e^{Q_j / \tau}}$$

**📌 Açıklama**

| Sembol | Açıklama |
|--------|----------|
| $P(i)$ | i. sunucunun seçilme olasılığı |
| $Q_i$ | i. sunucunun tahmini performans değeri |
| $\tau$ | Temperature (sıcaklık) parametresi |
| $K$ | Toplam sunucu sayısı |

**🔥 Temperature Parametresinin Etkisi**

- **Küçük τ** → Daha greedy davranış (en iyi sunucu daha sık seçilir)
- **Büyük τ** → Daha fazla exploration (keşif artar)

### 🔄 Round Robin Yük Dengeleyici
Sunuculara sırayla istek dağıtır. Herhangi bir öğrenme ya da adaptasyon gerçekleştirmez.

- **Avantaj:** Basit ve öngörülebilir.
- **Dezavantaj:** Sunucular arasındaki gecikme farklarını göz ardı eder.

### 🎲 Random Yük Dengeleyici
Her istekte rastgele bir sunucu seçer. Temel kıyaslama noktası olarak kullanılır.

- **Avantaj:** Sıfır yük, sıfır öğrenme.
- **Dezavantaj:** Performans optimizasyonu yoktur.

---

## Simülasyon Detayları

- **Sunucu Sayısı (K):** 5
- **İstek Sayısı:** 10.000
- **Sunucu Gecikmeleri:** Her sunucunun bir temel gecikmesi vardır (20ms, 25ms, 30ms, 35ms, 40ms). Gecikmeler her istekte rastgele sapma (`drift`) ve gürültü (`noise`) ile değişir; bu sayede durağan olmayan bir ortam simüle edilir.

### Gecikme Modeli

```
yeni_gecikme = max(1.0, taban_gecikme + sapma + gürültü)
sapma ∈ [-1.0, +1.0]  (rastgele yürüyüş)
gürültü ∈ [0.0, 5.0]
```

---

## Çalıştırma

### Gereksinimler
- Java 11 veya üzeri

### Derleme ve Çalıştırma

```bash
# Derleme
javac -d out src/com/example/loadbalancer/*.java

# Çalıştırma
java -cp out com.example.loadbalancer.Main
```

### Örnek Çıktı

```
Softmax    -> Avg Latency: 23.145 ms
RoundRobin -> Avg Latency: 32.087 ms
Random     -> Avg Latency: 32.541 ms
```

> Softmax algoritması, öğrenme süreci sayesinde düşük gecikmeli sunuculara yönelerek diğer algoritmalardan daha iyi performans sergilemektedir.

---

## Tasarım Kararları

- **Log-Sum-Exp Trick:** Softmax hesaplamasında sayısal kararlılık sağlamak amacıyla en büyük Q-değeri çıkarılarak üstel taşma önlenir.
- **Ödül Fonksiyonu:** `reward = -latency` formülü kullanılarak gecikme minimizasyonu bir maksimizasyon problemine dönüştürülür.
- **Arayüz Tabanlı Tasarım:** `LoadBalancer` ve `Server` arayüzleri sayesinde yeni algoritmalar veya sunucu modelleri kolayca eklenebilir.

---

## Geliştirme Fikirleri

- Epsilon-Greedy veya UCB (Upper Confidence Bound) gibi ek algoritmalar eklenebilir.
- Grafiksel gecikme karşılaştırması için JFreeChart entegrasyonu yapılabilir.
- Gerçek HTTP sunucu simülasyonu için gecikme modeli genişletilebilir.
- Birim testleri ile algoritma davranışları doğrulanabilir.
