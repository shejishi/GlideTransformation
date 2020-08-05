package com.ellison.glide.transformation.bean

import androidx.annotation.ColorInt

/**
 * @author ellison
 * @date 2020年08月05日
 * @desc 用一句话描述这个类的作用
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
class RecyclerBean(
    var pic: String,
    var picWidth: Int? = 0,
    var picHeight: Int? = 0,
    var isRound: Boolean = false,
    var leftTopRound: Boolean = false,
    var leftBottomRound: Boolean = false,
    var rightTopRound: Boolean = false,
    var rightBottomRound: Boolean = false,
    var roundCorners: Int = 0,
    var borderWidth: Int = 0,
    @ColorInt var borderColor: Int = 0
)