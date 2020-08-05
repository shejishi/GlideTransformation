import kotlin.reflect.full.memberProperties

/**
 * @author ellison
 * @date 2020年06月02日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */

@Suppress("unused")
object ModuleDependencies {

    const val App = ":app"
    const val TransformationLibrary = ":TransLibrary"

    fun getAllModules() = ModuleDependencies::class.memberProperties
        .filter { it.isConst }
        .map { it.getter.call().toString() }
        .toSet()
}