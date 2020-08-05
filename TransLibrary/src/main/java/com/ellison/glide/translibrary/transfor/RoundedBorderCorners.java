package com.ellison.glide.translibrary.transfor;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @author ellison
 * @date 2020年07月30日
 * @desc 用一句话描述这个类的作用
 * <p>
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
public class RoundedBorderCorners extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundedBorderCorners";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final float topLeft;
    private final float topRight;
    private final float bottomRight;
    private final float bottomLeft;

    private final float borderWidth;
    private final int borderColor;

    /**
     * Provide the radii to round the corners of the bitmap.
     */
    public RoundedBorderCorners(
            int[] radius, float borderWidth, int borderColor) {
        Preconditions.checkArgument(radius.length == 4, "radius length must be 4.");
        this.topLeft = radius[0];
        this.topRight = radius[1];
        this.bottomRight = radius[2];
        this.bottomLeft = radius[3];

        this.borderWidth = borderWidth;
        this.borderColor = borderColor;

    }

    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return GlideTransformationUtils.roundedCorners(
                pool, toTransform, topLeft, topRight, bottomRight, bottomLeft, borderWidth, borderColor);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RoundedBorderCorners) {
            RoundedBorderCorners other = (RoundedBorderCorners) o;
            return topLeft == other.topLeft
                    && topRight == other.topRight
                    && bottomRight == other.bottomRight
                    && bottomLeft == other.bottomLeft
                    && borderWidth == other.borderWidth
                    && borderColor == other.borderColor;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = Util.hashCode(ID.hashCode(), Util.hashCode(topLeft));
        hashCode = Util.hashCode(topRight, hashCode);
        hashCode = Util.hashCode(bottomRight, hashCode);
        hashCode = Util.hashCode(bottomLeft, hashCode);
        hashCode = Util.hashCode(borderWidth, hashCode);
        return Util.hashCode(borderColor, hashCode);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData =
                ByteBuffer.allocate(24)
                        .putFloat(topLeft)
                        .putFloat(topRight)
                        .putFloat(bottomRight)
                        .putFloat(bottomLeft)
                        .putFloat(borderWidth)
                        .putInt(borderColor)
                        .array();
        messageDigest.update(radiusData);
    }
}
