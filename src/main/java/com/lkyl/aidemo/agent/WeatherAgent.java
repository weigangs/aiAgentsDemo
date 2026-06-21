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
