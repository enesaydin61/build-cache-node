package com.sahibinden.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentCondition {
    
    @JsonProperty("temp_C")
    private String tempC;
    
    @JsonProperty("temp_F")
    private String tempF;
    
    @JsonProperty("weatherCode")
    private String weatherCode;
    
    @JsonProperty("weatherDesc")
    private List<WeatherDescription> weatherDesc;
    
    @JsonProperty("windspeedMiles")
    private String windspeedMiles;
    
    @JsonProperty("windspeedKmph")
    private String windspeedKmph;
    
    @JsonProperty("winddirDegree")
    private String winddirDegree;
    
    @JsonProperty("winddir16Point")
    private String winddir16Point;
    
    @JsonProperty("precipMM")
    private String precipMM;
    
    @JsonProperty("humidity")
    private String humidity;
    
    @JsonProperty("visibility")
    private String visibility;
    
    @JsonProperty("pressure")
    private String pressure;
    
    @JsonProperty("cloudcover")
    private String cloudcover;
    
    @JsonProperty("FeelsLikeC")
    private String feelsLikeC;
    
    @JsonProperty("FeelsLikeF")
    private String feelsLikeF;
}