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
