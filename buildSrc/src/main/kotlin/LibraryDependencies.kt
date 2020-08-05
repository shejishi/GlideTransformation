/**
 * @author ellison
 * @date 2020年06月03日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */

object LibraryDependencies {
    // 项目基本使用
    const val AppCompat =  "androidx.appcompat:appcompat:${LibraryVersion.AppCompatVersion}"
    const val Material = "com.google.android.material:material:${LibraryVersion.AndroidSupportVersion}"
    const val ExifInterface = "androidx.exifinterface:exifinterface:${LibraryVersion.AndroidSupportVersion}"
    const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${LibraryVersion.ConstraintVersion}"
    const val Multidex = "androidx.multidex:multidex:${LibraryVersion.MultidexVersion}"
    const val Annotation = "com.android.support:support-annotations:${LibraryVersion.SupportVersion}"
    const val AnnotationProcessor =  "com.android.support:support-annotations:${LibraryVersion.SupportVersion}"

    const val KotlinStd = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${CoreVersion.KotlinVersion}"
    const val KotlinReflect= "org.jetbrains.kotlin:kotlin-reflect:${CoreVersion.KotlinVersion}"
    const val KotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${CoreVersion.CoroutinesAndroid}"
    const val KotlinCoroutinesAndroid= "org.jetbrains.kotlinx:kotlinx-coroutines-android:${CoreVersion.CoroutinesAndroid}"


}


object LibraryVersion {

    const val AppCompatVersion = "1.0.0"
    const val AndroidSupportVersion = "1.0.0"
    const val ConstraintVersion = "2.0.0-alpha1"
    const val MultidexVersion = "2.0.1"
    const val SupportVersion = "28.0.0"

    const val GlideCompilerVersion = "4.9.0"
}

















