import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.pierreschwang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
}

dependencies {
    /* Runtime */
    listOf("api", "text-minimessage").forEach {
        implementation("net.kyori:adventure-$it:4.11.0")
    }
    implementation("net.kyori:adventure-platform-bukkit:4.1.1")
    implementation("io.papermc:paperlib:1.0.7")
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-core:7.2.9")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.9")

    /* Testing */
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("io.papermc.lib", "de.pierreschwang.masterbuilders.lib.paperlib")
    relocate("net.kyori.adventure", "de.pierreschwang.masterbuilders.lib.adventure")
}