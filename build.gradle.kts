plugins {
    id("org.springframework.boot") version "4.1.0"
    java
}

group = "com.lkyl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Globally strip out the default Spring Boot Logback engine
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

dependencies {
    // 1. Define the Spring Boot BOM as a reusable variable
    val springBootBom = platform("org.springframework.boot:spring-boot-dependencies:4.1.0")

    // 2. Apply the BOM to all relevant dependency scopes
    implementation(springBootBom)
    compileOnly(springBootBom)
    annotationProcessor(springBootBom)
    testCompileOnly(springBootBom)
    testAnnotationProcessor(springBootBom)

    // Core Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")

    // Spring AI Platform BOM
    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.0"))
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

    // Project Lombok (Now successfully inherits versioning from the scopes above)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}