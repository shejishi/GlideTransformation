private object TestLibraryVersion {
    const val JUnit = "4.13"
    const val EspressoCore = "3.2.0"
    const val TestRunner = "1.2.0"
    const val ARCH = "2.1.0"
}

object TestLibraryDependency {
//    androidTestImplementation('androidx.test.espresso:espresso-core:3.2.0', {
//        exclude group: 'com.android.support', module: 'support-annotations'
//    })
    const val JUnit = "junit:junit:${TestLibraryVersion.JUnit}"
    const val EspressoCore = "androidx.test.espresso:espresso-core:${TestLibraryVersion.EspressoCore}"
    const val TEST_RUNNER = "androidx.test:runner:${TestLibraryVersion.TestRunner}"

    const val ANDROID_X_TEST = "androidx.arch.core:core-testing:${TestLibraryVersion.ARCH}"
}
