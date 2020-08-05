/**
 * @author ellison
 * @date 2020年06月02日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */

object GradlePluginVersion {
    const val AndroidGradle = "3.6.2"
    const val GradleUpdateVersion = "0.28.0"

    const val KotlinVersion = CoreVersion.KotlinVersion
}

object GradlePluginId {

    const val AndroidApplication = "com.android.application"
    const val AndroidLibrary = "com.android.library"

    const val KotlinJvm = "org.jetbrains.kotlin.jvm"
    const val KotlinAndroid = "kotlin-android"
    const val KotlinAndroidExtensions = "kotlin-android-extensions"
    const val GradleUpdateVersion = "com.github.ben-manes.versions"
    const val KotlinGradle = "kotlin-kapt"


    const val MavenGradle = "android-maven-gradle-plugin"
}

object GradleOldWayPlugins {
    const val AndroidGradle = "com.android.tools.build:gradle:${GradlePluginVersion.AndroidGradle}"

    //      kotlin-kapt
    const val KotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${GradlePluginVersion.KotlinVersion}"

}