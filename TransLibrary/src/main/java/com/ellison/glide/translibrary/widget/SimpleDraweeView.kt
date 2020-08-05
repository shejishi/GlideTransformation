package com.ellison.glide.translibrary.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.ellison.glide.translibrary.R

/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
class SimpleDraweeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : ImageView(context, attrs, defStyleAttr) {
    // 占位图  占位图的伸展方式
    var placeHolderImage: Drawable
    var placeHolderImageScaleType: Int

    // 圆角、 圆图
    var isRoundAsCircle: Boolean

    // 圆角大小
    var roundedCornerRadius: Int
    var isRoundTopLeft: Boolean
    var isRoundTopRight: Boolean
    var isRoundBottomRight: Boolean
    var isRoundBottomLeft: Boolean

    // 不同圆角
    var roundTopLeftRadius: Int
    var roundTopRightRadius: Int
    var roundBottomRightRadius: Int
    var roundBottomLeftRadius: Int

    // 边框颜色、宽度
    var borderWidth: Int

    @get:ColorInt
    @ColorInt
    var borderColor: Int

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleDraweeView)
        placeHolderImage = ta.getDrawable(R.styleable.SimpleDraweeView_placeholderImage)
        placeHolderImageScaleType = ta.getInt(
            R.styleable.SimpleDraweeView_placeholderImageScaleType,
            DraweeImageScaleType.none
        )
        isRoundAsCircle = ta.getBoolean(R.styleable.SimpleDraweeView_roundAsCircle, false)

        // 圆角
        roundedCornerRadius =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundedCornerRadius, 0)
        isRoundTopLeft = ta.getBoolean(R.styleable.SimpleDraweeView_roundTopLeft, false)
        isRoundTopRight = ta.getBoolean(R.styleable.SimpleDraweeView_roundTopRight, false)
        isRoundBottomRight = ta.getBoolean(R.styleable.SimpleDraweeView_roundBottomRight, false)
        isRoundBottomLeft = ta.getBoolean(R.styleable.SimpleDraweeView_roundBottomLeft, false)
        // 不同圆角
        roundTopLeftRadius =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundTopLeftRadius, 0)
        roundTopRightRadius =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundTopRightRadius, 0)
        roundBottomRightRadius =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundBottomRightRadius, 0)
        roundBottomLeftRadius =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundBottomLeftRadius, 0)
        borderWidth =
            ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundingBorderWidth, 0)
        borderColor = ta.getColor(R.styleable.SimpleDraweeView_roundingBorderColor, -0x1)
        ta.recycle()
    }
}