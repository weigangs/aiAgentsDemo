1. update springboot to 4.1.0
2. include springai as below:
dependencies {
implementation platform("org.springframework.ai:spring-ai-bom:2.0.0")
// Replace the following with the specific module dependencies (e.g., spring-ai-openai) or starter modules (e.g., spring-ai-starter-model-openai) that you wish to use
implementation 'org.springframework.ai:spring-ai-openai'
}