# Huawei Project
Bu proje, Android projelerinde ve Restful API hizmetlerinde Huawei servislerinin nasıl kullanılacağını göstermek amacıyla oluşturulmuştur. Projede, kullanıcı kimlik doğrulaması için Authentication Service, kişiselleştirilmiş harita gösterimi için Map Kit ve konum almak için Location Kit gibi Huawei servisleri kullanılmaktadır.

## Projeyi nasıl çalıştıracağız?
Projeyi çalıştırabilmek için ilk olarak Huawei HMS Core 'u entegre etmeliyiz
- Preparation steps of HMS Core Integration : https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html#0

HMS Core entegre işlemini gerçekleştirdikten sonra [Open Charge Map](https://openchargemap.org/site) sitesinde "My Profile " sekmesinden api key almalıyız.

## Proje geliştirilirken kullanılan teknolojiler
 -  [Retrofit](https://square.github.io/retrofit/)
 -  [Huawei Location Kit](https://developer.huawei.com/consumer/en/hms/huawei-locationkit/)
 -  [Huawei Map Kit](https://developer.huawei.com/consumer/en/hms/huawei-MapKit/)
 -  [Navigation](https://developer.android.com/guide/navigation)
 -  [Huawei Account Kit](https://developer.huawei.com/consumer/en/hms/huawei-accountkit/)
 
# SSS
- Eğer ki locatiion kit ile kullanıcı konumu alırken 10803 Permission_Denied hatası alırsanız uygulama için Konumu her zaman "Allow All time" olarak ayarlamanız önerilir. Bu, uygulamanız arka planda konum isteklerini başlattığında doğru verilerin sürekli olarak döndürülebilmesini sağlayabilir.Ancak "Allow All Time" için AndroidManifest.xml dosyasında ACCESS_BACKGROUND_LOCATION iznini bildirin.Android 11 (API düzeyi: 30) veya sonraki sürümlerde, yukarıdaki üç izni aynı anda alınmamaktadır. ACCESS_BACKGROUND_LOCATION izni için yalnızca ACCESS_COARSE_LOCATION ve ACCESS_FINE_LOCATION izinleri verildikten sonra başvurabilirsiniz.
