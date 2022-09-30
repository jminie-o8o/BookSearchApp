// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.1")
    }
}

plugins {
    id(Plugins.ANDROID_APPLICATION) version Versions.AGP apply false
    id(Plugins.ANDROID_LIBRARY) version Versions.AGP apply false
    id(Plugins.KOTLIN_ANDROID) version Versions.KOTLIN apply false
    id(Plugins.SAFEARGS) version Versions.NAVIGATION apply false
    id(Plugins.KOTLIN_SERIALIZATION) version Versions.KOTLIN apply false
    id(Plugins.DAGGER_HILT) version Versions.HILT apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
