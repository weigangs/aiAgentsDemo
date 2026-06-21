package com.lkyl.aidemo.skill;

import com.lkyl.aidemo.model.*;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherFetchSkill {

    private final RestClient restClient = RestClient.create();

    @Tool(description = "Get current live weather information (temperature, general condition text, rain presence status, and wind speed) by providing a city name string.")
    public WeatherData getWeather(String city) {
        if (city == null || city.isBlank()) {
            return new WeatherData("Unknown", 20.0, "Clear", false, 0.0);
        }

        try {
            // Step 1: Convert City Name into GPS Coordinates using Geocoding API
            String geocodeUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + city.trim() + "&count=1&language=en&format=json";
            GeocodingResponse geoResponse = restClient.get()
                    .uri(geocodeUrl)
                    .retrieve()
                    .body(GeocodingResponse.class);

            if (geoResponse == null || geoResponse.results() == null || geoResponse.results().isEmpty()) {
                return new WeatherData(city, 15.0, "Unknown Location Context", false, 0.0);
            }

            GeocodingResult location = geoResponse.results().get(0);

            // Step 2: Query Live Weather using coordinates
            String weatherUrl = String.format(
                    "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,weather_code,wind_speed_10m",
                    location.latitude(), location.longitude()
            );

            OpenMeteoResponse weatherResponse = restClient.get()
                    .uri(weatherUrl)
                    .retrieve()
                    .body(OpenMeteoResponse.class);

            if (weatherResponse == null || weatherResponse.current() == null) {
                return new WeatherData(location.name(), 15.0, "Data Fetch Error", false, 0.0);
            }

            CurrentData current = weatherResponse.current();

            // Map World Meteorological Organization (WMO) codes to string conditions
            String condition = mapWmoCodeToCondition(current.weatherCode());
            boolean isRaining = current.weatherCode() >= 51 && current.weatherCode() <= 67 ||
                    current.weatherCode() >= 80 && current.weatherCode() <= 82;

            return new WeatherData(
                    location.name(),
                    current.temperature2m(),
                    condition,
                    isRaining,
                    current.windSpeed10m()
            );

        } catch (Exception e) {
            // Fail safely so your local LLM receives contextual fallback text instead of breaking the chat loop
            return new WeatherData(city, 16.0, "Service Temporarily Offline (" + e.getMessage() + ")", false, 5.0);
        }
    }

    /**
     * Translates standard WMO Weather Codes to readable conversational descriptions
     */
    private String mapWmoCodeToCondition(int code) {
        return switch (code) {
            case 0 -> "Sunny/Clear Sky";
            case 1, 2, 3 -> "Partly Cloudy";
            case 45, 48 -> "Foggy conditions";
            case 51, 53, 55 -> "Drizzle";
            case 61, 63, 65 -> "Rainy";
            case 71, 73, 75, 77 -> "Snowing";
            case 80, 81, 82 -> "Rain Showers";
            case 95, 96, 99 -> "Thunderstorm warning";
            default -> "Overcast/Cloudy";
        };
    }
}