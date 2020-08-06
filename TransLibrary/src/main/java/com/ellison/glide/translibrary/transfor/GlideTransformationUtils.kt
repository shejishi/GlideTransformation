package com.ellison.glide.translibrary.transfor

import android.graphics.*
import android.os.Build
import androidx.annotation.ColorInt
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.util.Synthetic
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author ellison
 * @date 2020年07月30日
 * @desc 用一句话描述这个类的作用
 *
 *
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： [简书博客](https://www.jianshu.com/u/b1c92a64018a)
 */
object GlideTransformationUtils {
    const val PAINT_FLAGS =
        Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG
    private const val CIRCLE_CROP_PAINT_FLAGS =
        PAINT_FLAGS or Paint.ANTI_ALIAS_FLAG
    private val CIRCLE_CROP_SHAPE_PAINT =
        Paint(CIRCLE_CROP_PAINT_FLAGS)
    private var CIRCLE_CROP_BITMAP_PAINT: Paint

    // See #738.
    private val MODELS_REQUIRING_BITMAP_LOCK: Set<String> =
        HashSet(
            Arrays.asList( // Moto X gen 2
                "XT1085",
                "XT1092",
                "XT1093",
                "XT1094",
                "XT1095",
                "XT1096",
                "XT1097",
                "XT1098",  // Moto G gen 1
                "XT1031",
                "XT1028",
                "XT937C",
                "XT1032",
                "XT1008",
                "XT1033",
                "XT1035",
                "XT1034",
                "XT939G",
                "XT1039",
                "XT1040",
                "XT1042",
                "XT1045",  // Moto G gen 2
                "XT1063",
                "XT1064",
                "XT1068",
                "XT1069",
                "XT1072",
                "XT1077",
                "XT1078",
                "XT1079"
            )
        )

    /**
     * https://github.com/bumptech/glide/issues/738 On some devices, bitmap drawing is not thread
     * safe. This lock only locks for these specific devices. For other types of devices the lock is
     * always available and therefore does not impact performance
     */
    private val BITMAP_DRAWABLE_LOCK =
        if (MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL)) ReentrantLock() else NoLock()

    /**
     * Creates a bitmap from a source bitmap and rounds the corners, applying a potentially different
     * [X, Y] radius to each corner.
     *
     *
     * This method does *NOT* resize the given [Bitmap], it only rounds it's corners.
     * To both resize and round the corners of an image, consider [ ][com.bumptech.glide.request.RequestOptions.transform] and/or [ ].
     *
     * @param inBitmap    the source bitmap to use as a basis for the created bitmap.
     * @param topLeft     top-left radius
     * @param topRight    top-right radius
     * @param bottomRight bottom-right radius
     * @param bottomLeft  bottom-left radius
     * @param borderWidth
     * @param borderColor
     * @return a [Bitmap] similar to inBitmap but with rounded corners.
     */
    @JvmStatic
    fun roundedCorners(
        pool: BitmapPool,
        inBitmap: Bitmap,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
        borderWidth: Float,
        borderColor: Int
    ): Bitmap {
        return roundedBorderCorners(
            pool,
            inBitmap,
            borderWidth,
            borderColor,
            object : DrawRoundedCornerFn {
                override fun drawRoundedCorners(canvas: Canvas?, paint: Paint?, rect: RectF?) {
                    val path = Path()
                    path.addRoundRect(
                        rect, floatArrayOf(
                            topLeft + 4,
                            topLeft + 4,
                            topRight + 4,
                            topRight + 4,
                            bottomRight + 4,
                            bottomRight + 4,
                            bottomLeft + 4,
                            bottomLeft + 4
                        ),
                        Path.Direction.CW
                    )
                    canvas?.drawPath(path, paint)
                }

            },
            object : DrawRoundedBorderCornerFn {
                override fun drawRoundedCorners(canvas: Canvas?, paint: Paint?, rect: RectF?) {
                    val path = Path()
                    path.addRoundRect(
                        rect, floatArrayOf(
                            topLeft,
                            topLeft,
                            topRight,
                            topRight,
                            bottomRight,
                            bottomRight,
                            bottomLeft,
                            bottomLeft
                        ),
                        Path.Direction.CW
                    )
                    canvas?.drawPath(path, paint)
                }
            }
        )
    }

    /**
     * 圆角边框
     *
     * @param pool
     * @param inBitmap
     * @param borderWidth
     * @param borderColor
     * @param drawRoundedCornerFn
     * @param roundedBorderCornerFn
     * @return
     */
    private fun roundedBorderCorners(
        pool: BitmapPool,
        inBitmap: Bitmap,
        borderWidth: Float,
        @ColorInt borderColor: Int,
        drawRoundedCornerFn: DrawRoundedCornerFn,
        roundedBorderCornerFn: DrawRoundedBorderCornerFn
    ): Bitmap {
        val halfBorder = borderWidth / 2
        // Alpha is required for this transformation.
        val safeConfig =
            getAlphaSafeConfig(inBitmap)
        val toTransform =
            getAlphaSafeBitmap(pool, inBitmap)
        val result =
            pool[toTransform.width, toTransform.height, safeConfig]
        result.setHasAlpha(true)
        val shader = BitmapShader(
            toTransform,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = shader
        val rect = RectF(0f, 0f, result.width.toFloat(), result.height.toFloat())
        val borderPaint =
            Paint(Paint.ANTI_ALIAS_FLAG) //设置边框样式
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.isAntiAlias = true
        val borderRect = RectF(
            halfBorder,
            halfBorder,
            result.width - halfBorder,
            result.height - halfBorder
        )
        BITMAP_DRAWABLE_LOCK.lock()
        try {
            val canvas = Canvas(result)
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            drawRoundedCornerFn.drawRoundedCorners(canvas, paint, rect)
            roundedBorderCornerFn.drawRoundedCorners(canvas, borderPaint, borderRect)
            clear(canvas)
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock()
        }
        if (toTransform != inBitmap) {
            pool.put(toTransform)
        }
        return result
    }

    /**
     * Crop the image to a circle and resize to the specified width/height. The circle crop will have
     * the same width and height equal to the min-edge of the result image.
     *
     * @param pool        The BitmapPool obtain a bitmap from.
     * @param inBitmap    The Bitmap to resize.
     * @param destWidth   The width in pixels of the final Bitmap.
     * @param destHeight  The height in pixels of the final Bitmap.
     * @param borderWidth
     * @param borderColor
     * @return The resized Bitmap (will be recycled if recycled is not null).
     */
    fun circleCrop(
        pool: BitmapPool,
        inBitmap: Bitmap,
        destWidth: Int,
        destHeight: Int,
        borderWidth: Float,
        @ColorInt borderColor: Int
    ): Bitmap {
        val destMinEdge = Math.min(destWidth, destHeight)
        val radius = destMinEdge / 2f
        val srcWidth = inBitmap.width
        val srcHeight = inBitmap.height
        val scaleX = destMinEdge / srcWidth.toFloat()
        val scaleY = destMinEdge / srcHeight.toFloat()
        val maxScale = Math.max(scaleX, scaleY)
        val scaledWidth = maxScale * srcWidth
        val scaledHeight = maxScale * srcHeight
        val left = (destMinEdge - scaledWidth) / 2f
        val top = (destMinEdge - scaledHeight) / 2f
        val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Alpha is required for this transformation.
        val toTransform =
            getAlphaSafeBitmap(pool, inBitmap)
        val outConfig =
            getAlphaSafeConfig(inBitmap)
        val result = pool[destMinEdge, destMinEdge, outConfig]
        result.setHasAlpha(true)


        //设置边框样式
        val borderPaint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.isAntiAlias = true
        borderPaint.isFilterBitmap = true
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        BITMAP_DRAWABLE_LOCK.lock()
        try {
            val canvas = Canvas(result)
            // Draw a circle
            canvas.drawCircle(
                radius,
                radius,
                radius,
                CIRCLE_CROP_SHAPE_PAINT
            )
            // Draw the bitmap in the circle
            canvas.drawBitmap(
                toTransform,
                null,
                destRect,
                CIRCLE_CROP_BITMAP_PAINT
            )
            // draw border
            canvas.drawCircle(
                destRect.centerX(),
                destRect.centerY(),
                (destRect.width() - borderWidth) / 2,
                borderPaint
            )
            clear(canvas)
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock()
        }
        if (toTransform != inBitmap) {
            pool.put(toTransform)
        }
        return result
    }

    // Avoids warnings in M+.
    private fun clear(canvas: Canvas) {
        canvas.setBitmap(null)
    }

    fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }

    fun getAlphaSafeBitmap(
        pool: BitmapPool, maybeAlphaSafe: Bitmap
    ): Bitmap {
        val safeConfig =
            getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }
        val argbBitmap =
            pool[maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig]
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f, 0f, null /*paint*/)

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap
    }

    /**
     * Convenience function for drawing a rounded bitmap.
     */
    private interface DrawRoundedCornerFn {
        fun drawRoundedCorners(
            canvas: Canvas?,
            paint: Paint?,
            rect: RectF?
        )
    }

    private interface DrawRoundedBorderCornerFn {
        fun drawRoundedCorners(
            canvas: Canvas?,
            paint: Paint?,
            rect: RectF?
        )
    }

    private class NoLock @Synthetic internal constructor() :
        Lock {
        override fun lock() {
            // do nothing
        }

        @Throws(InterruptedException::class)
        override fun lockInterruptibly() {
            // do nothing
        }

        override fun tryLock(): Boolean {
            return true
        }

        @Throws(InterruptedException::class)
        override fun tryLock(
            time: Long,
            unit: TimeUnit
        ): Boolean {
            return true
        }

        override fun unlock() {
            // do nothing
        }

        override fun newCondition(): Condition {
            throw UnsupportedOperationException("Should not be called")
        }
    }

    init {
        CIRCLE_CROP_BITMAP_PAINT = Paint(CIRCLE_CROP_PAINT_FLAGS)
        CIRCLE_CROP_BITMAP_PAINT.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
}