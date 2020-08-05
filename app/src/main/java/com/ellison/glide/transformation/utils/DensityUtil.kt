package com.ellison.glide.transformation.utils

import android.content.res.Resources

/**
 * @author ellison
 * @date 2020年08月05日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
class DensityUtil {
    var density: Float

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(dpValue: Float): Int {
        return (0.5f + dpValue * density).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(pxValue: Float): Float {
        return pxValue / density
    }

    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dp2px(dpValue: Float): Int {
            return (0.5f + dpValue * Resources.getSystem()
                .displayMetrics.density).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dp(pxValue: Float): Float {
            return pxValue / Resources.getSystem().displayMetrics.density
        }
    }

    init {
        density = Resources.getSystem().displayMetrics.density
    }
}