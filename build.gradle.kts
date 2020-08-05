allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath(GradleOldWayPlugins.AndroidGradle)
        classpath(GradleOldWayPlugins.KotlinGradle)
    }
}