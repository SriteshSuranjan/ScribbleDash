// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
        classpath("com.google.devtools.ksp:symbol-processing-api:2.0.21")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48") // Use your actual Hilt version here
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

