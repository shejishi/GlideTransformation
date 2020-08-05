package com.ellison.glide.translibrary.listener

/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
interface IResultListener<T> {
    fun onSuccessListener(result: T)

    fun onFailedListener();
}