package com.ellison.glide.translibrary

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.ellison.glide.translibrary.base.Loader
import com.ellison.glide.translibrary.base.LoaderBuilder
import com.ellison.glide.translibrary.listener.IResultListener
import com.ellison.glide.translibrary.widget.SimpleDraweeView

/**
 * 说明：
 * 作者：逍遥子
 * 时间：2016/3/21 17:09
 */
class ImageUtils {
    companion object {
        private var instances: ImageUtils? = null
            get() {
                if (field == null) {
                    field = ImageUtils()
                }
                return field
            }

        // 判断是否初始化
        private lateinit var loader: Loader
        private fun isInitLoader(): Boolean {
            return this::loader.isInitialized
        }

        lateinit var applicationContext: Application
        private fun isApplicationContextInit(): Boolean {
            return this::applicationContext.isInitialized
        }

        @JvmStatic
        fun getLoader(): Loader {
            if (!isInitLoader()) {
                throw RuntimeException("Must call ImageLoader.init() first!")
            }
            return loader
        }

        @JvmStatic
        fun init(applicationContext: Application, loader: Loader) {
            Companion.loader = loader
        }

        @JvmStatic
        fun getInstance(): ImageUtils {
            return instances!!
        }

    }


    fun load(url: String, width: Int = 0, height: Int = 0, iResult: IResultListener<Bitmap>?) {
        val builder = LoaderBuilder()
            .load(url)
            .width(width)
            .height(height)
            .listener(iResult)
        getLoader().load(builder)
    }

    /**
     * 对外设置
     * @see ImageView  使用ImageView加载时，可以动态配置
     */
    fun bind(view: ImageView?, loaderBuilder: LoaderBuilder) {
        loaderBuilder.into(view)
    }

    /**
     * kotlin 方法
     * @see SimpleDraweeView  [ 使用该控件为了兼容前面版本，可以使用它加载矩形圆角、矩形圆角边框、
     *                          圆形图片、圆形边框图片等]
     * @param view 加载的图片控件，使用 {@link com.facebook.drawee.view.SimpleDraweeView}
     */
    fun bind(
        view: ImageView?,
        url: Any?,
        width: Int? = 0,
        height: Int? = 0,
        isNeedBlur: Boolean? = false,
        defaultImage: Int?,
        isUseDp: Boolean = true
    ) {
        if (view == null) {
            return
        }

        val builder = LoaderBuilder().load(url)
        if (isUseDp) {
            if (width != null && height != null) {
                if (width > 0 && height > 0) {
                    builder.width = dip2px(width.toFloat())
                    builder.height = dip2px(height.toFloat())
                }
            }
        } else {
            if (width != null && height != null) {
                if (width > 0 && height > 0) {
                    builder.width = width
                    builder.height = height
                }
            }
        }
        builder.scaleType = view.scaleType
        if (view is SimpleDraweeView) {
            val placeHolderImage = view.placeHolderImage
            if (placeHolderImage != null) {
                builder.placeholder(placeHolderImage)
            } else {
                if (defaultImage != null) {
                    builder.placeholder(defaultImage)
                }
            }
            val roundAsCircle = view.isRoundAsCircle
            if (roundAsCircle) {
                builder.circle(true)
            } else {
                val roundedCornerRadius = view.roundedCornerRadius
                if (roundedCornerRadius > 0) {
                    if (!view.isRoundTopLeft && !view.isRoundTopRight && !view.isRoundBottomRight && !view.isRoundBottomLeft) {
                        // 兼容旧版
                        builder.round(
                            floatArrayOf(
                                roundedCornerRadius.toFloat(),
                                roundedCornerRadius.toFloat(),
                                roundedCornerRadius.toFloat(),
                                roundedCornerRadius.toFloat()
                            )
                        )
                    } else {
                        builder.round(
                            floatArrayOf(
                                if (view.isRoundTopLeft) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundTopRight) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundBottomRight) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundBottomLeft) roundedCornerRadius.toFloat() else 0.toFloat()
                            )
                        )
                    }
                } else {
                    // 新版本使用
                    builder.round(
                        floatArrayOf(
                            view.roundTopLeftRadius.toFloat(),
                            view.roundTopRightRadius.toFloat(),
                            view.roundBottomRightRadius.toFloat(),
                            view.roundBottomLeftRadius.toFloat()
                        )
                    )
                }
            }
            // 边框
            val borderWidth = view.borderWidth
            if (borderWidth > 0) {
                val borderColor = view.borderColor
                builder.borderWidth = borderWidth.toFloat()
                builder.borderColor = borderColor
            }
        } else {
            if (defaultImage != null) {
                builder.placeholder(defaultImage)
            }
        }
        // 动画
        builder.into(view)
    }

    private var density = -1f

    fun dip2px(dpValue: Float): Int {
        return ((dpValue * getDensity() + 0.5f).toInt())
    }

    fun getDensity(): Float {
        if (density <= 0f) {
            density =
                applicationContext.resources.displayMetrics.density
        }
        return density
    }


}