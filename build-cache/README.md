# Gradle Build Cache Node

Bu proje, Gradle build cache node server'ı kurmak için gerekli Podman konfigürasyonunu içerir.

## Kurulum ve Çalıştırma

### Önkoşullar
- Podman yüklü olmalı
- Make utility (isteğe bağlı, komutları kolaylaştırmak için)

### Hızlı Başlangıç

1. **Build cache node'u derle ve çalıştır:**
   ```bash
   make up
   ```

2. **Alternatif olarak manuel çalıştırma:**
   ```bash
   # Image'ı derle
   make build
   
   # Container'ı çalıştır
   make run
   ```

### Kullanılabilir Komutlar

- `make help` - Tüm kullanılabilir komutları listele
- `make build` - Podman image'ını derle
- `make run` - Build cache node container'ını çalıştır
- `make stop` - Container'ı durdur
- `make restart` - Container'ı yeniden başlat
- `make logs` - Container loglarını göster
- `make status` - Container durumunu göster
- `make clean` - Container ve image'ı temizle
- `make shell` - Çalışan container'a shell ile bağlan

### Erişim

Build cache node başarıyla çalıştırıldığında:

- **Web UI**: http://localhost:5071
- **API Endpoint**: http://localhost:5071/cache/
- **Health Check**: http://localhost:5071/status

### Kimlik Doğrulama

Config.yaml dosyasında tanımlı kullanıcı:
- **Kullanıcı adı**: `jenkins.ci.cache`
- **Yetki**: read/write
- **UI Erişimi**: `jenkins.ci` kullanıcısı ile

### Gradle Projelerinde Kullanım

Build cache'i Gradle projelerinde kullanmak için `settings.gradle.kts` dosyasına:

```kotlin
buildCache {
    remote<HttpBuildCache> {
        url = uri("http://localhost:5071/cache/")
        credentials {
            username = "jenkins.ci.cache"
            password = "your-password"
        }
    }
}
```

### Konfigürasyon

- **Port**: 5071 (değiştirmek için Makefile'daki CACHE_PORT değişkenini düzenleyin)
- **Cache boyutu**: 10GB (config.yaml'da düzenlenebilir)
- **Max cache yaşı**: 168 saat (7 gün)
- **Persistent storage**: `./cache` dizinine mount edilir

### Podman Özel Özellikler

Bu kurulum Podman'in rootless mode'unu destekler. Podman ile çalıştırırken:
- Root ayrıcalıklarına ihtiyaç yoktur
- Container otomatik olarak güvenlik sınırları içinde çalışır
- Systemd ile entegrasyon mümkündür

### Sorun Giderme

Container loglarını kontrol edin:
```bash
make logs
```

Container durumunu kontrol edin:
```bash
make status
```

Cache istatistiklerini göster:
```bash
make cache-stats
```
