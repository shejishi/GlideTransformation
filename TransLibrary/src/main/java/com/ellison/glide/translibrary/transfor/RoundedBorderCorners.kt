package com.ellison.glide.translibrary.transfor

import android.graphics.Bitmap
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Preconditions
import com.bumptech.glide.util.Util
import com.ellison.glide.translibrary.transfor.GlideTransformationUtils.roundedCorners
import java.nio.ByteBuffer
import java.security.MessageDigest

/**
 * @author ellison
 * @date 2020年07月30日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
class RoundedBorderCorners(
    radius: IntArray, borderWidth: Float, borderColor: Int
) : BitmapTransformation() {
    private val topLeft: Float
    private val topRight: Float
    private val bottomRight: Float
    private val bottomLeft: Float
    private val borderWidth: Float
    private val borderColor: Int
    override fun transform(
        pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        return roundedCorners(
            pool, toTransform, topLeft, topRight, bottomRight, bottomLeft, borderWidth, borderColor
        )
    }

    override fun equals(o: Any?): Boolean {
        if (o is RoundedBorderCorners) {
            val other = o
            return topLeft == other.topLeft && topRight == other.topRight && bottomRight == other.bottomRight && bottomLeft == other.bottomLeft && borderWidth == other.borderWidth && borderColor == other.borderColor
        }
        return false
    }

    override fun hashCode(): Int {
        var hashCode = Util.hashCode(
            ID.hashCode(),
            Util.hashCode(topLeft)
        )
        hashCode = Util.hashCode(topRight, hashCode)
        hashCode = Util.hashCode(bottomRight, hashCode)
        hashCode = Util.hashCode(bottomLeft, hashCode)
        hashCode = Util.hashCode(borderWidth, hashCode)
        return Util.hashCode(borderColor, hashCode)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val radiusData = ByteBuffer.allocate(24)
            .putFloat(topLeft)
            .putFloat(topRight)
            .putFloat(bottomRight)
            .putFloat(bottomLeft)
            .putFloat(borderWidth)
            .putInt(borderColor)
            .array()
        messageDigest.update(radiusData)
    }

    companion object {
        private const val ID =
            "com.bumptech.glide.load.resource.bitmap.RoundedBorderCorners"
        private val ID_BYTES =
            ID.toByteArray(Key.CHARSET)
    }

    /**
     * Provide the radii to round the corners of the bitmap.
     */
    init {
        Preconditions.checkArgument(
            radius.size == 4,
            "radius length must be 4."
        )
        topLeft = radius[0].toFloat()
        topRight = radius[1].toFloat()
        bottomRight = radius[2].toFloat()
        bottomLeft = radius[3].toFloat()
        this.borderWidth = borderWidth
        this.borderColor = borderColor
    }
}