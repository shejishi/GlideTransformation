package com.ellison.glide.translibrary.transfor

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Preconditions
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest

/**
 * @author ellison
 * @date 2020年07月31日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
class CircleBorderTransformation(
    borderWidth: Float,
    @ColorInt borderColor: Int
) : BitmapTransformation() {
    private val borderWidth: Float
    private val borderColor: Int

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    override fun transform(
        pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        return GlideTransformationUtils.circleCrop(
            pool,
            toTransform,
            outWidth,
            outHeight,
            borderWidth,
            borderColor
        )
    }

    override fun equals(o: Any?): Boolean {
        if (o is CircleBorderTransformation) {
            return (borderWidth == o.borderWidth
                    && borderColor == o.borderColor)
        }
        return false
    }

    override fun hashCode(): Int {
        val hashCode = Util.hashCode(
            ID.hashCode(),
            Util.hashCode(borderWidth)
        )
        return Util.hashCode(borderColor, hashCode)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val radiusData = ByteBuffer.allocate(8)
            .putFloat(borderWidth)
            .putInt(borderColor)
            .array()
        messageDigest.update(radiusData)
    }

    companion object {
        // The version of this transformation, incremented to correct an error in a previous version.
        // See #455.
        private const val VERSION = 1
        private const val ID =
            "com.yilahuo.driftbottle.loader.transform.CircleBorderTransformation.$VERSION"
        private val ID_BYTES =
            ID.toByteArray(Key.CHARSET)
    }

    /**
     * Provide the radii to round the corners of the bitmap.
     */
    init {
        Preconditions.checkArgument(
            borderWidth > 0,
            "borderWidth must be more the 0."
        )
        this.borderWidth = borderWidth
        this.borderColor = borderColor
    }
}