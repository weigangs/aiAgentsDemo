# Weather Assistant & Outfit Planner

A smart assistant that provides personalized clothing recommendations based on real-time weather data and issues proactive notifications for conditions like rain.

## Project Overview
This project aims to simplify morning preparations by providing a complete "weather + outfit" analysis. It starts with fetching raw weather data from an external source, processes the information (temperature, precipitation), and suggests what you should wear while reminding you of essentials like umbrellas if necessary.

## Core Features
- **Live Weather Fetching**: Automatically retrieves current weather conditions from a web service/API.
- **Climate Analysis**: Analyzes temperature ranges to provide specific clothing advice (e.g., "Wear a light jacket," "Stay warm with a coat").
- **Smart Notifications**: Generates helpful reminders, such as "don't forget to take an umbrella", based on the probability of rain.
- **AI-Powered Interaction**: Uses Spring AI and LLM capabilities to provide more natural and intelligent interactions via tool-calling.

## Architecture & Implementation Details

The system is built using a modular architecture where specialized **Agents** handle high-level logic and **Skills** (exposed as **Spring AI Tools**) provide specific capabilities or integrations.

### Components:
1.  **Tools / Skills (Capabilities)**:
    *   `WeatherFetchSkill`: A tool that fetches raw weather data from external APIs. In the Spring AI framework, this is implemented as a `Function` bean, allowing LLMs to call it when they need current environmental information.
    *   `RecommendationLogic`: A utility or skill used to map temperature ranges to clothing types and generate localized advice.
2.  **Agents (Task Execution)**:
    *   `WeatherAgent`: Orchestrates the flow using Spring AI's `ChatClient` (or similar abstractions). It leverages **Function Calling**, where the LLM determines which tool (Skill) to call based on the user's intent, enabling a more dynamic and interactive experience compared to a hard-coded script.
3.  **Service Layer**:
    *   Handles the integration between the UI/Controller and the agents.

## Spring AI Integration Highlights:
- **Function Calling**: By utilizing Spring AI's tool/function support, our skills are directly accessible by the LLM. The agent doesn't just run a sequence; it "decides" when to call `WeatherFetchSkill` based on user prompts (e.g., "What should I wear today?").
- **Contextual Intelligence**: Spring AI helps in synthesizing the raw data from the tools into a polished, conversational response that sounds natural rather than like a standard output.

## System Workflow
1. **Input**: User interacts with the system via text or voice (via the `AgentController`).
2. **AI Reasoning**: The LLM processes the input and determines if it needs information from available Tools (e.g., "I need to know the temperature").
3. **Tool Execution**: Spring AI automatically executes the required Tool/Skill (e.g., `WeatherFetchSkill`) and provides the output back to the model.
4. **Response Generation**: The LLM combines its reasoning with the tool's data to generate a final recommendation, including "Smart Notifications" like umbrella reminders.

## Roadmap & Development Tasks

The implementation is divided into the following manageable tasks:

### Phase 1: Data Foundation
- [x] Define `WeatherData` model (Temperature, Condition, Precipitation).
- [ ] Implement `WeatherFetchSkill` as a Spring AI compatible function/tool.

### Phase 2: Intelligence & Tools Logic
- [ ] Create an analysis engine to map temperature ranges to clothing recommendations.
- [ ] Integrate Spring AI's Function Calling capabilities for the primary intelligence logic.

### Phase 3: Agent Integration & LLM Core
- [ ] Develop `WeatherAgent` using Spring AI's high-level abstractions (`ChatClient`, etc.).
- [ ] Map all Skills into standard Spring Bean functions to be consumed as "Tools" by the model.

### Phase 4: Testing & Refinement
- [ ] Unit tests for parsing logic and tool responses.
- [ ] Integration tests for the full "User Query -> LLM Selection -> Tool Execution -> Final Answer" pipeline.
