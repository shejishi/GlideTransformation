package com.ellison.glide.transformation

import android.app.Application
import com.ellison.glide.translibrary.GlideLoader
import com.ellison.glide.translibrary.ImageUtils

/**
 * @author ellison
 * @date 2020年08月05日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // first init
        ImageUtils.init(this, GlideLoader())
    }

}