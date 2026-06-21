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
