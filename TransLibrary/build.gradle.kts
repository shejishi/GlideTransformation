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

//dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
//    implementation 'androidx.core:core-ktx:1.3.1'
//    implementation 'androidx.appcompat:appcompat:1.1.0'
//    testImplementation 'junit:junit:4.12'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
//
//}

dependencies {
    addTestDependencies()


    // 基本依赖
    implementation(LibraryDependencies.AppCompat)
    implementation(LibraryDependencies.ConstraintLayout)
    implementation(LibraryDependencies.Multidex)

    // kotlin
    implementation(LibraryDependencies.KotlinStd)
    implementation(LibraryDependencies.KotlinReflect)


    api(LibraryDependencies.GlideIntegration) {
        exclude(group = "com.squareup.okhttp3")
    }
    kapt(LibraryDependencies.GlideCompiler)
}