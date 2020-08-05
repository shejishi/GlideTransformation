package com.ellison.glide.translibrary.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.ellison.glide.translibrary.ImageUtils
import com.ellison.glide.translibrary.listener.IResultListener
import java.io.File


/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
open class LoaderBuilder {

    companion object {
        const val DISK_CACHE_DEFAULT = 0 //默认磁盘缓存

        const val DISK_CACHE_NONE = 1 //无磁盘缓存

        const val DISK_CACHE_ALL = 2 //缓存原始图片及变换后的图片

        const val DISK_CACHE_SOURCE = 3 //只缓存原始图片

        const val DISK_CACHE_RESULT = 4 //只缓存变换后的图片
    }

    @IntDef(
        DISK_CACHE_DEFAULT,
        DISK_CACHE_NONE,
        DISK_CACHE_ALL,
        DISK_CACHE_SOURCE,
        DISK_CACHE_RESULT
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class DiskCache

    var url: String? = null
    var file: File? = null
    var resId: Int? = Int.MIN_VALUE
    var uri: Uri? = null
    var placeholderResId = Int.MIN_VALUE
    var placeholderDrawable: Drawable? = null
    var errorResId = Int.MIN_VALUE
    var errorDrawable: Drawable? = null

    /**
     * 磁盘缓存策略，glide支持所有，fresco只支持有和没有两种
     */
    var diskCache = DISK_CACHE_DEFAULT

    /**
     * 针对该次请求使用的config，只支持RGB_565、ARGB_8888
     */
    var format: Bitmap.Config? = null

    /**
     * 除了对图片做变换，同时也会修改ImageView的ScaleType
     */
    var scaleType: ImageView.ScaleType? = null

    /**
     * 跳过内存缓存
     */
    var skipMemory = false

    /**
     * 为true的话gif不会自动播放
     */
    var asBitmap = false

    /**
     * 期望获得的图片宽高,负值表示按原图大小加载
     */
    var width = 0

    /**
     * 期望获得的图片宽高,负值表示按原图大小加载
     */
    var height: Int = 0

    /**
     * 圆形图片，支持placeholder，最好搭配fitCenter使用，否则fresco会用镜像显示小图片
     */
    var isCircle = false

    /**
     * 圆角图片，支持placeholder，最好搭配centerCrop使用，否则fresco会用镜像显示小图片
     */
    var roundCornerRadius = 0f

    /**
     * float array of 8 radii in pixels. Each corner receives two radius values [X, Y].
     * The corners are ordered top-left, top-right, bottom-right, bottom-left.
     */
    var roundCornerRadii: FloatArray? = null

    /**
     * 边框厚度
     */
    var borderWidth = 0f

    /**
     * 边框颜色
     */
    var borderColor = android.R.color.white

    /**
     * 高斯模糊时将原图缩小多少倍，可以节省内存，提高效率，不过会影响生成的图片大小，
     * 在使用CENTER_INSIDE之类的不会缩放小图的ScaleType时，请填1
     */
    var blurSampleSize = 0f

    /**
     * 高斯模糊采样半径
     */
    var blurRadius = 0

    /**
     * 透明渐变动画时长，0为关闭动画
     */
    var fadeDuration = 0

    /**
     * 统一的图形变换接口，fresco动图不支持
     */
    var transformationList: MutableList<BitmapTransformation>? = null
    var loadListener: IResultListener<Bitmap>? = null
    var targetView: ImageView? = null

    fun load(obj: Any?): LoaderBuilder {
        return when (obj) {
            is String -> {
                load(obj)
            }
            is Uri -> {
                load(obj)
            }
            is File -> {
                load(obj)
            }
            else -> {
                load(obj as Int?)
            }
        }
    }

    fun load(url: String?): LoaderBuilder {
        this.url = url
        return this
    }

    fun load(uri: Uri?): LoaderBuilder {
        this.uri = uri
        return this
    }

    fun load(file: File?): LoaderBuilder {
        this.file = file
        return this
    }

    fun load(resId: Int?): LoaderBuilder {
        this.resId = resId
        return this
    }

    fun borderWidth(borderWidth: Int?): LoaderBuilder {
        this.borderWidth = borderWidth!!.toFloat()
        return this
    }

    fun borderColor(@ColorInt borderColor: Int?): LoaderBuilder {
        this.borderColor = borderColor!!
        return this
    }

    fun width(width: Int?): LoaderBuilder {
        this.width = width!!
        return this
    }


    fun height(height: Int?): LoaderBuilder {
        this.height = height!!
        return this
    }

    fun placeholder(placeholderResId: Int): LoaderBuilder {
        this.placeholderResId = placeholderResId
        return this
    }

    fun placeholder(placeholderDrawable: Drawable?): LoaderBuilder {
        this.placeholderDrawable = placeholderDrawable
        return this
    }

    fun error(errorResId: Int): LoaderBuilder {
        this.errorResId = errorResId
        return this
    }

    fun error(errorDrawable: Drawable?): LoaderBuilder {
        this.errorDrawable = errorDrawable
        return this
    }

    fun diskCache(@DiskCache diskCache: Int): LoaderBuilder {
        this.diskCache = diskCache
        return this
    }

    fun format(format: Bitmap.Config?): LoaderBuilder {
        this.format = format
        return this
    }

    fun scaleType(scaleType: ImageView.ScaleType?): LoaderBuilder {
        this.scaleType = scaleType
        return this
    }

    fun skipMemory(skipMemory: Boolean): LoaderBuilder {
        this.skipMemory = skipMemory
        return this
    }

    fun asBitmap(asBitmap: Boolean): LoaderBuilder {
        this.asBitmap = asBitmap
        return this
    }

    fun override(width: Int, height: Int): LoaderBuilder {
        this.width = width
        this.height = height
        return this
    }

    fun circle(isCircle: Boolean): LoaderBuilder {
        this.isCircle = isCircle
        return this
    }

    fun round(roundCornerRadius: Float): LoaderBuilder {
        this.roundCornerRadius = roundCornerRadius
        return this
    }

    fun round(roundCornerRadii: FloatArray): LoaderBuilder {
        this.roundCornerRadii = roundCornerRadii
        return this
    }

    fun blur(blurSampleSize: Float, blurRadius: Int): LoaderBuilder {
        this.blurSampleSize = blurSampleSize
        this.blurRadius = blurRadius
        return this
    }

    fun fadeDuration(fadeDuration: Int): LoaderBuilder {
        this.fadeDuration = fadeDuration
        return this
    }

    fun addTransform(bitmapTransformation: BitmapTransformation): LoaderBuilder {
        if (transformationList == null) transformationList = ArrayList(1)
        transformationList!!.add(bitmapTransformation)
        return this
    }

    fun listener(loadListener: IResultListener<Bitmap>?): LoaderBuilder {
        this.loadListener = loadListener
        return this
    }

    fun into(targetView: ImageView?) {
        this.targetView = targetView
        ImageUtils.getLoader().load(this)
    }
}