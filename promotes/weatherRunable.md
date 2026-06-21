Here is the complete implementation for the **AI Weather Lifestyle Assistant** mapped exactly to your root package `com.lkyl.aidemo`. All files use **Java 21** features, follow the explicit layering rules, and prevent any submodule generation.

---

## Configuration Layer

### `src/main/resources/application.yml`

Append this to your existing configurations:

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: gpt-oss:20b

```

### `src/main/java/com/lkyl/aidemo/config/AiConfiguration.java`

```java
package com.lkyl.aidemo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}

```

---

## Data Model Layer

### `src/main/java/com/lkyl/aidemo/model/WeatherData.java`

Using a Java 21 record for a clean, immutable data transfer object.

```java
package com.lkyl.aidemo.model;

public record WeatherData(
    String city,
    double temperature,
    String condition,
    boolean isRaining,
    double windSpeed
) {}

```

---

## Deterministic Skills Layer (Spring AI Tools)

Every tool class operates strictly with rule-based logic or direct integrations. **None** of them interact with the LLM or `ChatClient`.

### `src/main/java/com/lkyl/aidemo/skill/WeatherFetchSkill.java`

```java
package com.lkyl.aidemo.skill;

import com.lkyl.aidemo.model.WeatherData;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class WeatherFetchSkill {

    // Simple routing dictionary to mimic dynamic geolocation lookups safely
    private static final Map<String, WeatherData> MOCK_DATABASE = Map.of(
        "toronto", new WeatherData("Toronto", 14.0, "Rainy", true, 15.5),
        "singapore", new WeatherData("Singapore", 31.0, "Sunny", false, 5.0),
        "london", new WeatherData("London", 8.0, "Cloudy", false, 22.0)
    );

    @Tool(description = "Get current weather metrics (temperature, condition, rain status, and wind speed) by city name.")
    public WeatherData getWeather(String city) {
        if (city == null || city.isBlank()) {
            return new WeatherData("Unknown", 20.0, "Clear", false, 0.0);
        }
        
        String key = city.trim().toLowerCase();
        // Fallback option if city is not in mock map
        return MOCK_DATABASE.getOrDefault(key, new WeatherData(city, 18.5, "Clear", false, 8.0));
    }
}

```

### `src/main/java/com/lkyl/aidemo/skill/OutfitRecommendationSkill.java`

```java
package com.lkyl.aidemo.skill;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class OutfitRecommendationSkill {

    @Tool(description = "Generate concrete clothing recommendations based on a given temperature Celsius and rain status.")
    public List<String> recommendOutfit(double temperature, boolean isRaining) {
        List<String> suggestions = new ArrayList<>();

        if (temperature < 5) {
            suggestions.addAll(List.of("Winter coat", "Gloves", "Scarf"));
        } else if (temperature >= 10 && temperature <= 20) {
            suggestions.add("Light jacket");
        } else if (temperature > 25) {
            suggestions.addAll(List.of("T-shirt", "Shorts"));
        } else {
            suggestions.add("Casual sweater or hoodie");
        }

        if (isRaining) {
            suggestions.addAll(List.of("Umbrella", "Waterproof shoes"));
        }

        return suggestions;
    }
}

```

### `src/main/java/com/lkyl/aidemo/skill/ActivityRecommendationSkill.java`

```java
package com.lkyl.aidemo.skill;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ActivityRecommendationSkill {

    @Tool(description = "Recommend appropriate lifestyle activities based on general weather conditions (e.g., Rainy, Sunny).")
    public List<String> recommendActivities(String condition) {
        if (condition != null && condition.equalsIgnoreCase("Rainy")) {
            return List.of("Gym execution", "Cinema", "Indoor museum visits");
        }
        return List.of("Walking outside", "Hiking trail exploration", "Cycling across town");
    }
}

```

### `src/main/java/com/lkyl/aidemo/skill/WeatherAlertSkill.java`

```java
package com.lkyl.aidemo.skill;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherAlertSkill {

    @Tool(description = "Evaluate weather metrics to see if any warning alerts (Rain, Storm, Strong Wind) need to be thrown.")
    public List<String> evaluateAlerts(boolean isRaining, double windSpeed, double temperature) {
        List<String> alerts = new ArrayList<>();

        if (isRaining) {
            alerts.add("Rain Alert: Precipitation detected.");
        }
        if (windSpeed > 20.0) {
            alerts.add("Strong Wind Alert: High gusts occurring.");
        }
        if (temperature > 35.0) {
            alerts.add("Extreme Heat/UV Alert: Take precautions.");
        }

        return alerts;
    }
}

```

---

## AI Orchestration Layer (Agent)

### `src/main/java/com/lkyl/aidemo/agent/WeatherAgent.java`

The **only component** allowed to directly communicate with your local model through Spring AI.

```java
package com.lkyl.aidemo.agent;

import com.lkyl.aidemo.skill.ActivityRecommendationSkill;
import com.lkyl.aidemo.skill.OutfitRecommendationSkill;
import com.lkyl.aidemo.skill.WeatherAlertSkill;
import com.lkyl.aidemo.skill.WeatherFetchSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherAgent {

    private final ChatClient chatClient;
    private final WeatherFetchSkill weatherFetchSkill;
    private final OutfitRecommendationSkill outfitRecommendationSkill;
    private final ActivityRecommendationSkill activityRecommendationSkill;
    private final WeatherAlertSkill weatherAlertSkill;

    public String chat(String userPrompt) {
        return chatClient.prompt()
                .system("""
                    You are a highly analytical Weather Lifestyle Assistant.
                    
                    Your responsibilities:
                    1. When a user asks about weather, clothing, or outdoor plans, execute your tools.
                    2. Never invent weather numbers or guess metrics. Rely strictly on data returned from tools.
                    3. Combine results from weather metrics, outfit pickers, alerts, and activity checks seamlessly.
                    4. Present the information clearly, ending with a localized lifestyle tip based on the data.
                """)
                .user(userPrompt)
                .tools(
                    weatherFetchSkill,
                    outfitRecommendationSkill,
                    activityRecommendationSkill,
                    weatherAlertSkill
                )
                .call()
                .content();
    }
}

```

---

## Web Presentation Layer

### `src/main/java/com/lkyl/aidemo/controller/WeatherAssistantController.java`

```java
package com.lkyl.aidemo.controller;

import com.lkyl.aidemo.agent.WeatherAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather-assistant")
@RequiredArgsConstructor
public class WeatherAssistantController {

    private final WeatherAgent weatherAgent;

    @GetMapping("/chat")
    public String askAssistant(@RequestParam("prompt") String prompt) {
        return weatherAgent.chat(prompt);
    }
}

```