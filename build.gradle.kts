// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext.kotlin_version = "2.0.21" // Define the Kotlin version you're using
    ext.ksp_version = "2.0.21" // Define the KSP version you're using

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // Kotlin plugin for Gradle
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        // KSP plugin
        classpath("com.google.devtools.ksp:symbol-processing-api:$ksp_version")
        // Hilt plugin (optional, based on your app requirements)
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp.android) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

