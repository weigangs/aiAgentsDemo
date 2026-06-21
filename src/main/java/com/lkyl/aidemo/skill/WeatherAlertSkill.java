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
