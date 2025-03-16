İftar Sayacı
============

Açıklama
--------

Bu proje, **kullanıcı seçimine** göre **İftar** zamanını gösteren bir **geri sayım sayacı**dır. **CollectAPI** kullanarak namaz vakitlerini alır ve dinamik olarak geri sayımı günceller.

GUI, **Tkinter** ile oluşturulmuş ve modern bir görünüm için **Lato** fontu kullanılmıştır. Kullanıcılar, şehirlerini girerek İftar'a kalan süreyi öğrenebilirler.

![Screenshot 2025-03-03 110409](https://github.com/user-attachments/assets/f93df036-7223-4405-b6b6-c5b4079302d0)


Özellikler
----------

-   **Şehir Girişi**: Kullanıcılar, şehir girerek İftar saatini öğrenebilir.
-   **Geri Sayım**: İftar'a kalan süre sayılır.
-   **Responsive GUI**: Dinamik ve estetik bir arayüz.

Gereksinimler
-------------

-   Python 3.x
-   Tkinter
-   Pillow (görsel işleme için)
-   Requests (API çağrıları için)

Kurulum
-------

1.  Depoyu klonlayın ya da dosyayı indirin.

2.  Gerekli kütüphaneleri yükleyin:

    bash

    CopyEdit

    `pip install requests Pillow`

3.  `get_iftar_time` fonksiyonundaki `your_token` kısmını kendi **CollectAPI** token'ınız ile değiştirin.

Kullanım
--------

-   Python dosyasını çalıştırarak uygulamayı başlatın.
-   Şehir girin veya seçin, ardından İftar zamanını öğrenin.
-   İftar'a kalan süre ekranda gösterilecektir.
  
