plugins {
    id(GradlePluginId.AndroidLibrary)
    id(GradlePluginId.KotlinAndroid)
    id(GradlePluginId.KotlinGradle)
    id(GradlePluginId.Maven)
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)

    ndkVersion = "21.0.6113669"

    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)

        versionCode = AndroidConfig.VersionCode
        versionName = AndroidConfig.VersionName
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
    }


    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    addTestDependencies()


    // 基本依赖
    implementation(LibraryDependencies.AppCompat)
    implementation(LibraryDependencies.ConstraintLayout)
    implementation(LibraryDependencies.Multidex)

    // kotlin
    implementation(LibraryDependencies.KotlinStd)
    implementation(LibraryDependencies.KotlinReflect)


    api(LibraryDependencies.GlideIntegration)
    kapt(LibraryDependencies.GlideCompiler)
}