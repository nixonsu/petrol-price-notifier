import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.nixonsu"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.amazonaws:aws-lambda-java-core:1.2.2")
    implementation("com.amazonaws:aws-java-sdk-sns:1.12.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.7")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks {
    shadowJar {
        archiveBaseName.set("petrol-price-notifier")
        archiveClassifier.set("")
    }
}
