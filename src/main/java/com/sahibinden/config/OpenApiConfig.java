package com.sahibinden.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Sahibinden Weather Service API",
        version = "1.0.0",
        description = """
            Bu API, gerçek zamanlı hava durumu bilgilerini sağlar. 
            Ücretsiz wttr.in servisi kullanılarak geliştirilmiştir.
            
            ## Özellikler
            - Güncel hava durumu bilgileri
            - 7 güne kadar hava durumu tahmini
            - Detaylı meteorolojik veriler
            - API key gerektirmez
            
            ## Kullanım Örnekleri
            - `/api/weather/current/Istanbul` - İstanbul'un güncel hava durumu
            - `/api/weather/forecast/Ankara?days=5` - Ankara için 5 günlük tahmin
            - `/api/weather/full/London` - Londra için detaylı bilgi
            """,
        contact = @Contact(
            name = "Sahibinden Weather Team",
            email = "weather@sahibinden.com",
            url = "https://sahibinden.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Geliştirme Sunucusu"),
        @Server(url = "https://weather-api.sahibinden.com", description = "Üretim Sunucusu")
    },
    tags = {
        @Tag(name = "Weather API", description = "Hava durumu işlemleri")
    }
)
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addResponses("BadRequest", new ApiResponse()
                    .description("Geçersiz istek parametresi")
                    .content(new Content()
                        .addMediaType("application/json", new MediaType()
                            .schema(new Schema<>()
                                .type("object")
                                .addProperty("error", new Schema<>()
                                    .type("string")
                                    .example("Geçersiz şehir adı")))
                            .addExamples("invalidCity", new Example()
                                .summary("Geçersiz şehir adı")
                                .value("{\"error\": \"Şehir adı boş olamaz\"}")))))
                .addResponses("NotFound", new ApiResponse()
                    .description("Şehir bulunamadı")
                    .content(new Content()
                        .addMediaType("application/json", new MediaType()
                            .schema(new Schema<>()
                                .type("object")
                                .addProperty("error", new Schema<>()
                                    .type("string")
                                    .example("Şehir bulunamadı")))
                            .addExamples("cityNotFound", new Example()
                                .summary("Şehir bulunamadı")
                                .value("{\"error\": \"Belirtilen şehir bulunamadı\"}")))))
                .addResponses("InternalServerError", new ApiResponse()
                    .description("Sunucu hatası")
                    .content(new Content()
                        .addMediaType("application/json", new MediaType()
                            .schema(new Schema<>()
                                .type("object")
                                .addProperty("error", new Schema<>()
                                    .type("string")
                                    .example("İç sunucu hatası")))
                            .addExamples("serverError", new Example()
                                .summary("Sunucu hatası")
                                .value("{\"error\": \"Hava durumu servisi geçici olarak kullanılamıyor\"}"))))))
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Sahibinden Weather Service API")
                .version("1.0.0")
                .description("""
                    # Sahibinden Hava Durumu API'si
                    
                    Bu API, dünya genelindeki şehirler için gerçek zamanlı hava durumu bilgilerini sağlar.
                    
                    ## API Özellikleri
                    
                    ### 🌡️ Güncel Hava Durumu
                    - Sıcaklık (Celsius ve Fahrenheit)
                    - Hissedilen sıcaklık
                    - Nem oranı
                    - Rüzgar hızı ve yönü
                    - Görüş mesafesi
                    - Atmosfer basıncı
                    - Bulut oranı
                    
                    ### 📅 Hava Durumu Tahmini  
                    - 1-7 gün arası tahmin
                    - Günlük maksimum/minimum sıcaklık
                    - Saatlik detaylar
                    - UV indeksi
                    - Güneşli saat sayısı
                    
                    ### 🌍 Konum Bilgileri
                    - Şehir adı ve ülke
                    - Enlem/boylam koordinatları
                    - Nüfus bilgisi
                    - En yakın meteoroloji istasyonu
                    
                    ## Veri Kaynağı
                    Bu API, [wttr.in](http://wttr.in) servisini kullanarak veri sağlar. 
                    wttr.in, ücretsiz ve açık kaynak bir hava durumu servisidir.
                    
                    ## Rate Limiting
                    Şu anda rate limiting uygulanmamaktadır, ancak makul kullanım beklenmektedir.
                    
                    ## Hata Kodları
                    - `400` - Geçersiz parametreler
                    - `404` - Şehir bulunamadı  
                    - `500` - Sunucu hatası veya dış servis problemi
                    
                    ## Örnek Kullanım
                    ```bash
                    # İstanbul'un güncel hava durumu
                    curl http://localhost:8080/api/weather/current/Istanbul
                    
                    # Ankara için 5 günlük tahmin
                    curl "http://localhost:8080/api/weather/forecast/Ankara?days=5"
                    
                    # New York için detaylı bilgi
                    curl http://localhost:8080/api/weather/full/New%20York
                    ```
                    """)
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .name("Sahibinden Weather Team") 
                    .email("weather@sahibinden.com")
                    .url("https://sahibinden.com"))
                .license(new io.swagger.v3.oas.models.info.License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
