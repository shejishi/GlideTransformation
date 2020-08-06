package com.ellison.glide.translibrary.transfor

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.ellison.glide.translibrary.utils.FastBlur
import java.security.MessageDigest

/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
class BlurTransformation @JvmOverloads constructor(
    private val radius: Int = MAX_RADIUS,
    private val sampling: Int = DEFAULT_DOWN_SAMPLING
) : BitmapTransformation() {
    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling
        val bitmap =
            pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        FastBlur.blur(bitmap, radius, true)
        return bitmap
    }

    override fun toString(): String {
        return "BlurTransformation(radius=$radius, sampling=$sampling)"
    }

    override fun equals(o: Any?): Boolean {
        return o is BlurTransformation && o.radius == radius && o.sampling == sampling
    }

    override fun hashCode(): Int {
        return ID.hashCode() + radius * 1000 + sampling * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius + sampling).toByteArray(Key.CHARSET))
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "BlurTransformation.$VERSION"
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }

}