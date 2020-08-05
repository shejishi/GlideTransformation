plugins {
    id(GradlePluginId.AndroidApplication)
    id(GradlePluginId.KotlinAndroid)
    id(GradlePluginId.KotlinAndroidExtensions)
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)

    ndkVersion = "21.0.6113669"

    defaultConfig {
        applicationId = AndroidConfig.ReleaseId
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

        versionCode = AndroidConfig.VersionCode
        versionName = AndroidConfig.VersionName
        flavorDimensions("versionCode")
        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER
        multiDexEnabled = true
    }

    lintOptions {
        // By default lint does not check test sources, but setting this option means that lint will not even parse them
        isIgnoreTestSources = true
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            isZipAlignEnabled = BuildTypeRelease.isZipAlignEnable
            isShrinkResources = BuildTypeRelease.isShrinkResources
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }

        getByName(BuildType.DEBUG) {
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            isZipAlignEnabled = BuildTypeDebug.isZipAlignEnable
            isShrinkResources = BuildTypeDebug.isShrinkResources
            manifestPlaceholders = ManifestPlaceHolder.getDebugManifestPlaceHolder()
        }


        testOptions {
            unitTests.isReturnDefaultValues = TestOptions.IS_RETURN_DEFAULT_VALUES
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }


    externalNativeBuild {
        cmake {
            setVersion("3.10.2")
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }


    dataBinding { isEnabled = true }
}


dependencies {
    addTestDependencies()


    implementation(fileTree("libs"))
    // 基本依赖
    implementation(LibraryDependencies.AppCompat)
    implementation(LibraryDependencies.Material)
    implementation(LibraryDependencies.ExifInterface)
    implementation(LibraryDependencies.ConstraintLayout)
    implementation(LibraryDependencies.Multidex)
    implementation(LibraryDependencies.Annotation)
    annotationProcessor(LibraryDependencies.AnnotationProcessor)

    // kotlin
    implementation(LibraryDependencies.KotlinStd)
    implementation(LibraryDependencies.KotlinReflect)
    implementation(LibraryDependencies.KotlinCoroutines)
    implementation(LibraryDependencies.KotlinCoroutinesAndroid)
}