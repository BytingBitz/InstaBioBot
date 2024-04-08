plugins {
    kotlin("jvm") version "1.9.23"
}

group = "au.jamal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.19.1")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}