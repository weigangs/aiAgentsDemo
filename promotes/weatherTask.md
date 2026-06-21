# Weather Lifestyle Assistant

## Goal

Implement a Weather Lifestyle Assistant inside the EXISTING Spring Boot project.

Tech Stack:

- Java 21
- Gradle
- Spring Boot
- Spring AI
- Ollama
- gpt-oss:20b

---

## Constraints

MUST:

- modify existing project only
- reuse existing root package
- reuse existing project structure

MUST NOT:

- create new module
- create new build.gradle
- modify settings.gradle
- create new root package
- create second Spring Boot application

---

## Architecture

Agent owns LLM.

Only Agent may use:

- ChatClient
- ChatModel
- ChatMemory

Skills are deterministic tools.

Skills must NOT call any LLM.

Workflow:

User
→ WeatherAgent
→ Spring AI Tool Calling
→ Skills
→ Response

Agent provides tools.

GPT-OSS decides which tools to invoke.

---

## Skills

### WeatherFetchSkill

Use Open-Meteo.

Workflow:

City
→ Geocoding
→ Lat/Lon
→ Forecast
→ WeatherData

### OutfitRecommendationSkill

Weather → clothing recommendations.

### ActivityRecommendationSkill

Weather → activity recommendations.

### WeatherAlertSkill

Weather → alerts.

---

## Memory

Use ChatMemory.

Remember city within the conversation.

Example:

How is weather in Toronto?

↓

Do I need an umbrella?

Toronto should be reused automatically.

---

## API

Controller
→ WeatherAgent

Controller must not call Skills directly.

---

## Tests

Add:

- unit tests
- integration tests

---

## Expected Queries

- What should I wear today?
- Will it rain today?
- Do I need an umbrella?
- Can I go jogging after work?
- What should I pack for tomorrow?

Use Spring AI Tool Calling for all weather-related decisions.