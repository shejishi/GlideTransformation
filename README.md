> 使用`Glide` 的时候，经常使用自定义 `view` 来加载有圆角、边框、圆形之类的图片，当然，使用一个自定义`view`就能实现其中的功能。但是，最新版的`Glide v4.11.0`已经实现了圆角和圆形图片的`Transformation`，我们就在其上实现边框即可，也不需要造轮子了~

#### 效果图：
![0b46f21fbe096b63d30f4b590d338744ebf8aca0.png](https://upload-images.jianshu.io/upload_images/2158207-58fdd29d51514208.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/340/h/460)

#### 项目地址：[https://github.com/shejishi/GlideTransformation](https://github.com/shejishi/GlideTransformation)

如果不想看实现过程的童鞋可以直接略过下面的内容，毕竟文字比较枯燥，直接看代码还是来得快，`clone`或者直接下载`zip`编译即可运行如图的`app`

# #封装
类图如下：
![WX20200806-174625@2x.png](https://upload-images.jianshu.io/upload_images/2158207-13d9c1cc820ca6fe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/340/h/460)

`Glide`加载图片已经对用户来说非常方便了，但是我们为什么还需要对他封装？ 重构过项目的朋友都知道，在早起使用`Fresco`、`image-loader`的项目如果没有进行封装，则会非常痛苦，所以我们有必要对项目再次封装。

项目模块划分， 抽象出上层类`Loader`， 包含清除缓存、暂停加载、唤醒加载、加载图片等基础功能（主要根据`Fresco`、`Glide`功能来封装）

使用类建造者模式`LoaderBuilder`设置参数；因为我们设置图片时，需要传递的参数比较多而杂，所以不适用方法重构的方式，直接用`javabean`来代替会方便得多

###### 添加变换
使用`Glide`，你可以使用多个 `Transformation`变换来操作你的图片：
```java
@Override
public void load(@NotNull final LoaderBuilder loadConfig) {
    // 获取Glide配置
    RequestManager requestManager = Glide.with(loadConfig.getTargetView());
    RequestBuilder requestBuilder = requestManager.load(loadConfig.getUrl());
    RequestOptions requestOptions = RequestOptions.skipMemoryCacheOf(loadConfig.getSkipMemory());
    ...
    // 添加多个 变换 Transformation
    requestOptions.transform(new MultiTransformation(list));
}
```

核心功能就这么多，你没看错，接下来就是介绍下使用，两种方法：
 ###### 一 、直接使用`ImageView`与`LoaderBuilder`
创建一个`LoaderBuilder`，设置需要设置的值即可
```kotlin
override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val recyclerBean = listData[position]
        val builderConfig = LoaderBuilder()
            .round(recyclerBean.roundCorners.toFloat())
            .circle(recyclerBean.isRound)
            .width(recyclerBean.picWidth)
            .height(recyclerBean.picHeight)
            .round(
                floatArrayOf(
                    if (recyclerBean.leftTopRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.rightTopRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.leftBottomRound) recyclerBean.roundCorners.toFloat() else 0f,
                    if (recyclerBean.rightBottomRound) recyclerBean.roundCorners.toFloat() else 0f
                )
            )
            .borderColor(recyclerBean.borderColor)
            .borderWidth(recyclerBean.borderWidth)
            .load(recyclerBean.pic)

        ImageUtils.getInstance().bind(holder.iv, builderConfig)
}
```

<br/>

 ###### 一 、创建自定义`view`——`SimpleDraweeView`
使用过`Fresco`的朋友对这个`SimpleDraweeView`很熟悉 ，是的，当初我们的项目也是使用`Fresco`，后面因为项目重构而使用`Glide`，但是使用`Fresco`的都知道这个库的侵入性非常大，对重构项目非常不友好，我们项目也到处使用了`<com.facebook.drawee.view.SimpleDraweeView />`， 一开始我想着将所有的`View`都替换掉，但是我全局搜索之后放弃了，因为有200+的文件，一个个改不是不可能，但是我这个人非常懒，对这种无意义的工作不太想浪费时间，所以我直接删除掉`Fresco`之后创建了自定义`SimpleDraweeView`:
```java
public class SimpleDraweeView extends ImageView {

  public SimpleDraweeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleDraweeView);
        placeHolderImage = ta.getDrawable(R.styleable.SimpleDraweeView_placeholderImage);
        placeHolderImageScaleType = ta.getInt(R.styleable.SimpleDraweeView_placeholderImageScaleType, DraweeImageScaleType.none);

        roundAsCircle = ta.getBoolean(R.styleable.SimpleDraweeView_roundAsCircle, false);

        // 圆角
        roundedCornerRadius = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundedCornerRadius, 0);

        roundTopLeft = ta.getBoolean(R.styleable.SimpleDraweeView_roundTopLeft, false);
        roundTopRight = ta.getBoolean(R.styleable.SimpleDraweeView_roundTopRight, false);
        roundBottomRight = ta.getBoolean(R.styleable.SimpleDraweeView_roundBottomRight, false);
        RoundBottomLeft = ta.getBoolean(R.styleable.SimpleDraweeView_roundBottomLeft, false);
        // 不同圆角
        roundTopLeftRadius = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundTopLeftRadius, 0);
        roundTopRightRadius = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundTopRightRadius, 0);
        roundBottomRightRadius = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundBottomRightRadius, 0);
        roundBottomLeftRadius = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundBottomLeftRadius, 0);

        borderWidth = ta.getDimensionPixelOffset(R.styleable.SimpleDraweeView_roundingBorderWidth, 0);
        borderColor = ta.getColor(R.styleable.SimpleDraweeView_roundingBorderColor, 0xFFFFFFFF);
        ta.recycle();
    }
}
```
上面保留了最常用的几个属性：
```
<declare-styleable name="SimpleDraweeView">

        <eat-comment />

        <!-- A drawable or color to be be used as a placeholder. -->
        <attr name="placeholderImage" format="reference" />
        <!-- Scale type of the placeholder image. Ignored if placeholderImage is not specified. -->
        <attr name="placeholderImageScaleType">
            <enum name="none" value="-1" />
            <enum name="fitXY" value="0" />
            <enum name="fitStart" value="1" />
            <enum name="fitCenter" value="2" />
            <enum name="fitEnd" value="3" />
            <enum name="center" value="4" />
            <enum name="centerInside" value="5" />
            <enum name="centerCrop" value="6" />
            <enum name="focusCrop" value="7" />
            <enum name="fitBottomStart" value="8" />
        </attr>

        <!-- Rounding params -
        Declares attributes for rounding shape, mode and border. -->
        <!-- Round as circle. -->
        <attr name="roundAsCircle" format="boolean" />
        <!-- Rounded corner radius. Ignored if roundAsCircle is used. -->
        <attr name="roundedCornerRadius" format="dimension" />
        <attr name="roundTopLeft" format="boolean" />
        <attr name="roundTopRight" format="boolean" />
        <attr name="roundBottomRight" format="boolean" />
        <attr name="roundBottomLeft" format="boolean" />

        <attr name="roundTopLeftRadius" format="dimension" />
        <attr name="roundTopRightRadius" format="dimension" />
        <attr name="roundBottomRightRadius" format="dimension" />
        <attr name="roundBottomLeftRadius" format="dimension" />

        <!-- Rounding border width-->
        <attr name="roundingBorderWidth" format="dimension" />
        <!-- Rounding border color -->
        <attr name="roundingBorderColor" format="color|reference" />

    </declare-styleable>
```
在封装的单例类中拿到对应的属性即可：

```java
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
    fun bind(view: ImageView?, url: Any?, width: Int? = 0, height: Int? = 0, isNeedBlur: Boolean? = false, defaultImage: Int?, isUseDp: Boolean = true) {
        if (view == null) {
            return
        }

        val builder = LoaderBuilder().load(url)
        if (isUseDp) {
            if(width != null && height!=null) {
                if (width > 0 && height > 0) {
                    builder.width = DensityUtil.dip2px(width.toFloat())
                    builder.height = DensityUtil.dip2px(height.toFloat())
                }
            }
        } else {
            if(width != null && height!=null) {
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
                if(defaultImage != null) {
                    builder.placeholder(defaultImage)
                }
            }
            val roundAsCircle = view.isRoundAsCircle
            if (roundAsCircle) {
                builder.circle(true)
            } else {
                val roundedCornerRadius = view.roundedCornerRadius
                if (roundedCornerRadius > 0) {
                    if (!view.isRoundTopLeft &&
                            !view.isRoundTopRight &&
                            !view.isRoundBottomRight &&
                            !view.isRoundBottomLeft) {
                        // 兼容旧版
                        builder.round(floatArrayOf(roundedCornerRadius.toFloat(), roundedCornerRadius.toFloat(), roundedCornerRadius.toFloat(), roundedCornerRadius.toFloat()))
                    } else {
                        builder.round(floatArrayOf(
                                if (view.isRoundTopLeft) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundTopRight) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundBottomRight) roundedCornerRadius.toFloat() else 0.toFloat(),
                                if (view.isRoundBottomLeft) roundedCornerRadius.toFloat() else 0.toFloat()))
                    }
                } else {
                    // 新版本使用
                    builder.round(floatArrayOf(
                            view.roundTopLeftRadius.toFloat(),
                            view.roundTopRightRadius.toFloat(),
                            view.roundBottomRightRadius.toFloat(),
                            view.roundBottomLeftRadius.toFloat()))
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
            if(defaultImage != null) {
                builder.placeholder(defaultImage)
            }
        }
        // 动画
        builder.into(view)
    }
```

<br/>

# #边框原理
最新版[Glide v4.11.0]([https://github.com/bumptech/glide](https://github.com/bumptech/glide)
)查看源码，不同的变换`Transformation`由不同的变换实现类操作，`Transformation`是一个接口，具体有如下实现：
![transformation实现类.png](https://upload-images.jianshu.io/upload_images/2158207-9938432d8ca4a090.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到，圆形图片实现类为`CricleCrop`, 圆角实现类为：`RoundedCorners`、`GranularRoundedCorners`，圆角这两个第一个是所有的四个角弧度都一样，第二个则可以设置不同的角度，他们两都调用了`TransformationUtils#roundedCorners()`方法，阅读这几个类的源码，我们可以仿照其写一个`CircleBorderCrop`的`Transformation`， 首先参照源码继承`BitmapTransformation`：


```java
public class CircleBorderTransformation extends BitmapTransformation {
}
```
根据`Glide`文档，我们必须要实现`equals()`、`hashCode()`方法，以保证每张图片的唯一性与复用性，源码`GircleCrop`非常简单，直接调用了`TransformationUtils`中的方法：
```
/**
 * A Glide {@link BitmapTransformation} to circle crop an image. Behaves similar to a {@link
 * FitCenter} transform, but the resulting image is masked to a circle.
 *
 * <p>Uses a PorterDuff blend mode, see http://ssp.impulsetrain.com/porterduff.html.
 */
public class CircleCrop extends BitmapTransformation {
  // The version of this transformation, incremented to correct an error in a previous version.
  // See #455.
  private static final int VERSION = 1;
  private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop." + VERSION;
  private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

  // Bitmap doesn't implement equals, so == and .equals are equivalent here.
  @SuppressWarnings("PMD.CompareObjectsWithEquals")
  @Override
  protected Bitmap transform(
      @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
    return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof CircleCrop;
  }

  @Override
  public int hashCode() {
    return ID.hashCode();
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    messageDigest.update(ID_BYTES);
  }
}
```

我们传入两个参数，边框与边框颜色，重写一下：
```
public class CircleBorderTransformation extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.yilahuo.driftbottle.loader.transform.CircleBorderTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final float borderWidth;
    private final int borderColor;

    /**
     * Provide the radii to round the corners of the bitmap.
     */
    public CircleBorderTransformation(float borderWidth, @ColorInt int borderColor) {
        Preconditions.checkArgument(borderWidth > 0, "borderWidth must be more the 0.");

        this.borderWidth = borderWidth;
        this.borderColor = borderColor;

    }

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        // 自定义的 TransformationUtils
        return GlideTransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight, borderWidth, borderColor);
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof CircleBorderTransformation) {
            CircleBorderTransformation other = (CircleBorderTransformation) o;
            return borderWidth == other.borderWidth
                    && borderColor == other.borderColor;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = Util.hashCode(ID.hashCode(), Util.hashCode(borderWidth));
        return Util.hashCode(borderColor, hashCode);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData =
                ByteBuffer.allocate(8)
                        .putFloat(borderWidth)
                        .putInt(borderColor)
                        .array();
        messageDigest.update(radiusData);
    }
}
```
具体的封装了查看我们自定义的`GlideTransformationUtils`类，该类也是和`Glide`框架一样的方法，我们实现了`equals()`、`hashCode()`、`updateDiskCacheKey()`，这几个方法也是参照类`GranularRoundedCorners`中的实现！



















