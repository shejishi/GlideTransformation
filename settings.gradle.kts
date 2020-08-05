pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    plugins {
        id(GradlePluginId.GradleUpdateVersion) //version GradlePluginVersion.GradleUpdateVersion

        id(GradlePluginId.KotlinJvm) //version GradlePluginVersion.KotlinVersion
        id(GradlePluginId.KotlinAndroid) //version GradlePluginVersion.KotlinVersion
        id(GradlePluginId.KotlinAndroidExtensions) //version GradlePluginVersion.KotlinVersion

        id(GradlePluginId.AndroidApplication) //version GradlePluginVersion.AndroidGradle
        id(GradlePluginId.AndroidLibrary) //version GradlePluginVersion.AndroidGradle
    }

}

rootProject.buildFileName = "build.gradle.kts"
include(projectPaths = *ModuleDependencies.getAllModules().toTypedArray())
