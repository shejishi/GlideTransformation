package com.ellison.glide.translibrary.base

import android.content.Context

/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
interface Loader {
    // 重启
    fun resume(context: Context)

    // 暂停
    fun pause(context: Context)

    // 清除磁盘缓存
    fun clearDiskCache(context: Context)

    // 清除内存缓存
    fun clearMemoryCache(context: Context)

    fun onTrimMemory(context: Context, level: Int)

    fun onLowMemory(context: Context)

    // 加载
    fun load(loadConfig: LoaderBuilder)
}