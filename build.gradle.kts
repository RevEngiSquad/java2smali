plugins {
    java
}

repositories {
    mavenCentral()
    maven("https://maven.google.com")
}

dependencies {
    implementation(libs.baksmali)
    implementation(libs.dexlib2)
    implementation(libs.r8)
    implementation(libs.commons.io)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "java2smali.Main"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}