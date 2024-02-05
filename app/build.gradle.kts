plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0" 

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Apply the java-library plugin if this project is a library
    // `java-library`

    // Apply the version catalog plugin to use the version catalog feature
    id("org.gradle.version-catalog")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Existing dependency
    implementation(libs.guava)

    // Added dependencies
    implementation(libs.jacksonDatabind) // For Jackson
    testImplementation(libs.junit) // For JUnit
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit test framework with version from libs.versions.toml
            useJUnit()
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    // Set the source and target compatibility versions
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.named<JavaExec>("run") {
    args("--config", "config.json", "--betting-amount", "100")
}

tasks.shadowJar {
    archiveBaseName.set("scratch-game")
    archiveVersion.set("0.0.1")
    archiveClassifier.set("")
}

application {
    mainClass.set("org.romelgomez.ScratchGame") 
}
