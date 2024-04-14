plugins {
    kotlin("jvm") version "1.9.23"
}

kotlin {
    jvmToolchain(21)
}

group = "au.jamal"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.19.1")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    implementation("io.github.cdimascio:dotenv-java:2.2.0")
    implementation("org.yaml:snakeyaml:2.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("instaBioBot")
    archiveExtension.set("jar")
    manifest {
        attributes["Main-Class"] = "au.jamal.instabiobot.MainKt"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}