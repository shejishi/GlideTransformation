package com.ellison.glide.translibrary.transfor;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Synthetic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ellison
 * @date 2020年07月30日
 * @desc 用一句话描述这个类的作用
 * <p>
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
public class GlideTransformationUtils {
    public static final int PAINT_FLAGS = Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG;
    private static final int CIRCLE_CROP_PAINT_FLAGS = PAINT_FLAGS | Paint.ANTI_ALIAS_FLAG;
    private static final Paint CIRCLE_CROP_SHAPE_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
    private static final Paint CIRCLE_CROP_BITMAP_PAINT;

    // See #738.
    private static final Set<String> MODELS_REQUIRING_BITMAP_LOCK =
            new HashSet<>(
                    Arrays.asList(
                            // Moto X gen 2
                            "XT1085",
                            "XT1092",
                            "XT1093",
                            "XT1094",
                            "XT1095",
                            "XT1096",
                            "XT1097",
                            "XT1098",
                            // Moto G gen 1
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
                            "XT1045",
                            // Moto G gen 2
                            "XT1063",
                            "XT1064",
                            "XT1068",
                            "XT1069",
                            "XT1072",
                            "XT1077",
                            "XT1078",
                            "XT1079"));


    /**
     * https://github.com/bumptech/glide/issues/738 On some devices, bitmap drawing is not thread
     * safe. This lock only locks for these specific devices. For other types of devices the lock is
     * always available and therefore does not impact performance
     */
    private static final Lock BITMAP_DRAWABLE_LOCK =
            MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL) ? new ReentrantLock() : new NoLock();

    /**
     * Creates a bitmap from a source bitmap and rounds the corners, applying a potentially different
     * [X, Y] radius to each corner.
     *
     * <p>This method does <em>NOT</em> resize the given {@link Bitmap}, it only rounds it's corners.
     * To both resize and round the corners of an image, consider {@link
     * com.bumptech.glide.request.RequestOptions#transform(Transformation[])} and/or {@link
     * com.bumptech.glide.load.MultiTransformation}.
     *
     * @param inBitmap    the source bitmap to use as a basis for the created bitmap.
     * @param topLeft     top-left radius
     * @param topRight    top-right radius
     * @param bottomRight bottom-right radius
     * @param bottomLeft  bottom-left radius
     * @param borderWidth
     * @param borderColor
     * @return a {@link Bitmap} similar to inBitmap but with rounded corners.
     */
    public static Bitmap roundedCorners(
            @NonNull BitmapPool pool,
            @NonNull Bitmap inBitmap,
            final float topLeft,
            final float topRight,
            final float bottomRight,
            final float bottomLeft,
            final float borderWidth,
            final int borderColor) {
        return roundedBorderCorners(
                pool,
                inBitmap,
                borderWidth,
                borderColor,
                (canvas, paint, rect) -> {
                    Path path = new Path();
                    path.addRoundRect(
                            rect,
                            new float[]{
                                    topLeft + 4,
                                    topLeft + 4,
                                    topRight + 4,
                                    topRight + 4,
                                    bottomRight + 4,
                                    bottomRight + 4,
                                    bottomLeft + 4,
                                    bottomLeft + 4
                            },
                            Path.Direction.CW);
                    canvas.drawPath(path, paint);
                }, (canvas, paint, rect) -> {
                    Path path = new Path();
                    path.addRoundRect(
                            rect,
                            new float[]{
                                    topLeft,
                                    topLeft,
                                    topRight,
                                    topRight,
                                    bottomRight,
                                    bottomRight,
                                    bottomLeft,
                                    bottomLeft
                            },
                            Path.Direction.CW);
                    canvas.drawPath(path, paint);
                });
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
    private static Bitmap roundedBorderCorners(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap,
                                               final float borderWidth,
                                               @ColorInt final int borderColor, DrawRoundedCornerFn drawRoundedCornerFn, DrawRoundedBorderCornerFn roundedBorderCornerFn) {
        float halfBorder = borderWidth / 2;
        // Alpha is required for this transformation.
        Bitmap.Config safeConfig = getAlphaSafeConfig(inBitmap);
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);
        Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), safeConfig);

        result.setHasAlpha(true);

        BitmapShader shader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());

        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置边框样式
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setAntiAlias(true);
        RectF borderRect = new RectF(halfBorder, halfBorder, result.getWidth() - halfBorder, result.getHeight() - halfBorder);
        BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawRoundedCornerFn.drawRoundedCorners(canvas, paint, rect);

            roundedBorderCornerFn.drawRoundedCorners(canvas, borderPaint, borderRect);
            clear(canvas);
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
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
    public static Bitmap circleCrop(
            @NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int destWidth, int destHeight, float borderWidth, @ColorInt int borderColor) {
        int destMinEdge = Math.min(destWidth, destHeight);
        float radius = destMinEdge / 2f;

        int srcWidth = inBitmap.getWidth();
        int srcHeight = inBitmap.getHeight();

        float scaleX = destMinEdge / (float) srcWidth;
        float scaleY = destMinEdge / (float) srcHeight;
        float maxScale = Math.max(scaleX, scaleY);

        float scaledWidth = maxScale * srcWidth;
        float scaledHeight = maxScale * srcHeight;
        float left = (destMinEdge - scaledWidth) / 2f;
        float top = (destMinEdge - scaledHeight) / 2f;

        RectF destRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Alpha is required for this transformation.
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);

        Bitmap.Config outConfig = getAlphaSafeConfig(inBitmap);
        Bitmap result = pool.get(destMinEdge, destMinEdge, outConfig);
        result.setHasAlpha(true);


        //设置边框样式
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setAntiAlias(true);
        borderPaint.setFilterBitmap(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        BITMAP_DRAWABLE_LOCK.lock();
        try {
            Canvas canvas = new Canvas(result);
            // Draw a circle
            canvas.drawCircle(radius, radius, radius, CIRCLE_CROP_SHAPE_PAINT);
            // Draw the bitmap in the circle
            canvas.drawBitmap(toTransform, null, destRect, CIRCLE_CROP_BITMAP_PAINT);
            // draw border
            canvas.drawCircle(destRect.centerX(), destRect.centerY(), (destRect.width() - borderWidth) / 2, borderPaint);
            clear(canvas);
        } finally {
            BITMAP_DRAWABLE_LOCK.unlock();
        }

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    static {
        CIRCLE_CROP_BITMAP_PAINT = new Paint(CIRCLE_CROP_PAINT_FLAGS);
        CIRCLE_CROP_BITMAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    // Avoids warnings in M+.
    private static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    @NonNull
    public static Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }

        return Bitmap.Config.ARGB_8888;
    }

    public static Bitmap getAlphaSafeBitmap(
            @NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Bitmap.Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap = pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*paint*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap;
    }

    /**
     * Convenience function for drawing a rounded bitmap.
     */
    private interface DrawRoundedCornerFn {

        void drawRoundedCorners(Canvas canvas, Paint paint, RectF rect);
    }

    private interface DrawRoundedBorderCornerFn {

        void drawRoundedCorners(Canvas canvas, Paint paint, RectF rect);
    }


    private static final class NoLock implements Lock {

        @Synthetic
        NoLock() {
        }

        @Override
        public void lock() {
            // do nothing
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            // do nothing
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
            // do nothing
        }

        @NonNull
        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }
    }
}
