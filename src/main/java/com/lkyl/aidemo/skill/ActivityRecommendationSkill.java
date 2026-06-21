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
