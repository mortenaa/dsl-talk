/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */
val ktor_version: String by project
val kotlin_version: String by project
val kotlin_css_version: String by project
val logback_version: String by project

this.plugins( {
    val plugin = this.application
    this.kotlin("jvm").version("1.7.10")
    this.id("io.ktor.plugin").version("2.1.1")
})

this.group = "com.example"
this.version = "0.0.1"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.7.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    // This dependency is used by the application.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-core:2.2.1-eap-565")
    implementation("io.ktor:ktor-server-netty:2.2.1-eap-565")
    implementation("io.ktor:ktor-server-html-builder:2.2.1-eap-565")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.454")
    //testImplementation("io.ktor:ktor-server-test-host:2.2.1-eap-565")
    //testImplementation("org.jetbrains.kotlin:kotlin-test")
    //implementation("com.google.guava:guava:31.1-jre")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest("1.7.10")

            dependencies {
                // Use newer version of JUnit Engine for Kotlin Test
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
            }
        }
    }
}

application {
    // Define the main class for the application.
    mainClass.set("dsl.talk.AppKt")
}