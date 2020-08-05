/**
 * @author ellison
 * @date 2020年06月02日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */

object AndroidConfig {

    const val COMPILE_SDK_VERSION = 28
    const val MIN_SDK_VERSION = 19
    const val TARGET_SDK_VERSION = 28
    const val BUILD_TOOLS_VERSION = "28.0.3"

    const val VersionCode = 1
    const val VersionName = "1.0.0"

    const val ReleaseId = "com.ellison.glide.transformation"
    const val TestId = "com.ellison.glide.transformation"
    const val TEST_INSTRUMENTATION_RUNNER = "android.support.test.runner.AndroidJUnitRunner"
}

object ManifestPlaceHolder {
    fun getManifestPlaceHolder(): Map<String, String> {
        val hashMap = HashMap<String, String>()
        hashMap["UMENG_CHANNEL_VALUE"] = "\"zhadanyu\""
        hashMap["UMENG_APPKEY"] = "\"5d68977e4ca357dab00007f0\""
        hashMap["APP_WX_ID"] = "\"wx6c83bcda136735ea\""
        hashMap["QQ_APPID"] = "\"1109719829\""
        return hashMap
    }

    fun getDebugManifestPlaceHolder(): Map<String, String> {
        val hashMap = HashMap<String, String>()
        hashMap["UMENG_CHANNEL_VALUE"] = "\"zhadanyu\""
        hashMap["UMENG_APPKEY"] = "\"5d68977e4ca357dab00007f0\""
        hashMap["QQ_APPID"] = "\"1105929012\""
        return hashMap
    }
}

//interface
interface BuildType {

    companion object {
        const val RELEASE = "release"
        const val DEBUG = "debug"
    }

    val isMinifyEnabled: Boolean
    val isZipAlignEnable: Boolean
    val isShrinkResources: Boolean
    val runApiPath: String
}

object BuildTypeDebug : BuildType {
    override val isMinifyEnabled = false
    override val isZipAlignEnable = false
    override val isShrinkResources = false
    override val runApiPath: String
        // 测试环境
        get() = "\"http://39.108.12.66:7080\""
    // 开发环境
//        get() = "\"http://120.78.84.15:7080\""
}

object BuildTypeRelease : BuildType {
    override val isMinifyEnabled = true
    override val isZipAlignEnable = true
    override val isShrinkResources = true
    override val runApiPath: String
        get() = "\"https://api.plpyp.com\""
}

object TestOptions {
    const val IS_RETURN_DEFAULT_VALUES = true
}
