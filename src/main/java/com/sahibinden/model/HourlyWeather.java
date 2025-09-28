package com.sahibinden.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeather {
    
    @JsonProperty("time")
    private String time;
    
    @JsonProperty("tempC")
    private String tempC;
    
    @JsonProperty("tempF")
    private String tempF;
    
    @JsonProperty("windspeedMiles")
    private String windspeedMiles;
    
    @JsonProperty("windspeedKmph")
    private String windspeedKmph;
    
    @JsonProperty("winddirDegree")
    private String winddirDegree;
    
    @JsonProperty("winddir16Point")
    private String winddir16Point;
    
    @JsonProperty("weatherCode")
    private String weatherCode;
    
    @JsonProperty("weatherDesc")
    private List<WeatherDescription> weatherDesc;
    
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
    
    @JsonProperty("HeatIndexC")
    private String heatIndexC;
    
    @JsonProperty("HeatIndexF")
    private String heatIndexF;
    
    @JsonProperty("DewPointC")
    private String dewPointC;
    
    @JsonProperty("DewPointF")
    private String dewPointF;
    
    @JsonProperty("WindChillC")
    private String windChillC;
    
    @JsonProperty("WindChillF")
    private String windChillF;
    
    @JsonProperty("WindGustMiles")
    private String windGustMiles;
    
    @JsonProperty("WindGustKmph")
    private String windGustKmph;
    
    @JsonProperty("FeelsLikeC")
    private String feelsLikeC;
    
    @JsonProperty("FeelsLikeF")
    private String feelsLikeF;
    
    @JsonProperty("uvIndex")
    private String uvIndex;
}