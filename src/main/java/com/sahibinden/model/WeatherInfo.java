package com.sahibinden.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfo {
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("maxtempC")
    private String maxtempC;
    
    @JsonProperty("maxtempF")
    private String maxtempF;
    
    @JsonProperty("mintempC")
    private String mintempC;
    
    @JsonProperty("mintempF")
    private String mintempF;
    
    @JsonProperty("avgtempC")
    private String avgtempC;
    
    @JsonProperty("avgtemp F")
    private String avgtempF;
    
    @JsonProperty("totalSnow_cm")
    private String totalSnowCm;
    
    @JsonProperty("sunHour")
    private String sunHour;
    
    @JsonProperty("uvIndex")
    private String uvIndex;
    
    @JsonProperty("hourly")
    private List<HourlyWeather> hourly;
}