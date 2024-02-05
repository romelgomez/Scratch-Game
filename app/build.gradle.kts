plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" 

    application

    id("org.gradle.version-catalog")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation(libs.jacksonDatabind)
    testImplementation(libs.junit)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnit()
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
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
