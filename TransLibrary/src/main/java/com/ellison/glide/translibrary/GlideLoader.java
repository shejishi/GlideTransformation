package com.ellison.glide.translibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.ellison.glide.translibrary.base.Loader;
import com.ellison.glide.translibrary.base.LoaderBuilder;
import com.ellison.glide.translibrary.transfor.BlurTransformation;
import com.ellison.glide.translibrary.transfor.CircleBorderTransformation;
import com.ellison.glide.translibrary.transfor.RoundedBorderCorners;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ellison
 * @date 2020年07月29日
 * @desc 用一句话描述这个类的作用
 * <p>
 * 邮箱： Ellison.Sun0808@outlook.com
 * 博客： <a href="https://www.jianshu.com/u/b1c92a64018a">简书博客</a>
 */
public class GlideLoader implements Loader {

    @Override
    public void resume(@NonNull Context context) {
        Glide.with(context).resumeRequests();
    }

    @Override
    public void pause(@NonNull Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void clearDiskCache(@NotNull Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void clearMemoryCache(@NotNull Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void onTrimMemory(@NotNull Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public void onLowMemory(@NotNull Context context) {
        Glide.get(context).onLowMemory();
    }


    @Override
    public void load(@NotNull final LoaderBuilder loadConfig) {
        if (loadConfig.getTargetView() == null && loadConfig.getLoadListener() == null) {
            return;
        }

        RequestManager requestManager;
        if (loadConfig.getTargetView() != null) {
            requestManager = Glide.with(loadConfig.getTargetView());
        } else {
            requestManager = Glide.with(ImageUtils.applicationContext);
        }
        RequestBuilder requestBuilder = null;

        // 设置bitmap
        boolean isBitmap = false;
        if (loadConfig.getAsBitmap() || (loadConfig.getLoadListener() != null)) {
            isBitmap = true;
            requestBuilder = requestManager.asBitmap();
        }

        // 获取bitmap 或者 drawable
        if (!TextUtils.isEmpty(loadConfig.getUrl())) {
            if (requestBuilder == null) {
                requestBuilder = requestManager.load(loadConfig.getUrl());
            } else {
                requestBuilder = requestBuilder.load(loadConfig.getUrl());
            }
        } else if (loadConfig.getFile() != null) {
            if (requestBuilder == null) {
                requestBuilder = requestManager.load(loadConfig.getFile());
            } else {
                requestBuilder = requestBuilder.load(loadConfig.getFile());
            }
        } else if (loadConfig.getResId() != null && loadConfig.getResId() != Integer.MIN_VALUE) {
            if (requestBuilder == null) {
                requestBuilder = requestManager.load(loadConfig.getResId());
            } else {
                requestBuilder = requestBuilder.load(loadConfig.getResId());
            }
        } else {
            if (requestBuilder == null) {
                requestBuilder = requestManager.load(loadConfig.getUri());
            } else {
                requestBuilder = requestBuilder.load(loadConfig.getUri());
            }
        }
        TransitionOptions<?, ?> transitionOptions;
        if (loadConfig.getFadeDuration() > 0) {
            DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder(loadConfig.getFadeDuration()).setCrossFadeEnabled(true).build();
            transitionOptions = isBitmap ? BitmapTransitionOptions.withCrossFade(factory) : DrawableTransitionOptions.withCrossFade(factory);
            requestBuilder = requestBuilder.transition(transitionOptions);
        }

        // 设置缓存模式
        RequestOptions requestOptions = setCacheMode(loadConfig);

        // 设置默认图  错误
        requestOptions = setPlaceholderAndErrorDrawable(loadConfig, requestOptions);


        // 设置宽高
        requestOptions = setWidthAndHeight(loadConfig, requestOptions);

        requestOptions = requestOptions.dontAnimate();

        // 设置 圆角、圆图、高斯模糊
        requestOptions = setTransformation(loadConfig, requestOptions);

        requestBuilder = requestBuilder.apply(requestOptions);
        // 设置 回调
        if (loadConfig.getLoadListener() != null) {
            requestBuilder = requestBuilder.listener(new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    if (loadConfig.getLoadListener() != null) {
                        loadConfig.getLoadListener().onFailedListener();
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    if (loadConfig.getLoadListener() != null) {
                        if (resource instanceof Bitmap) {
                            loadConfig.getLoadListener().onSuccessListener((Bitmap) resource);
                        } else {
                            loadConfig.getLoadListener().onFailedListener();
                        }
                    }
                    return false;
                }
            });
        }
        if (loadConfig.getTargetView() != null) {
            requestBuilder.into(loadConfig.getTargetView());
        } else {
            requestBuilder.into(new SimpleTarget() {
                @Override
                public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {
                    //do nothing
                }
            });
        }
    }

    private RequestOptions setTransformation(@NotNull LoaderBuilder loadConfig, RequestOptions requestOptions) {
        List<BitmapTransformation> list = new ArrayList<>(2);
        if (loadConfig.getBlurRadius() > 0) {
            list.add(new BlurTransformation((int) loadConfig.getBlurSampleSize(), loadConfig.getBlurRadius()));
        }

        // 设置 ScaleType
        if (loadConfig.getScaleType() != null) {
            if (loadConfig.getTargetView() != null) {
                loadConfig.getTargetView().setScaleType(loadConfig.getScaleType());
            }
            switch (loadConfig.getScaleType()) {
                case CENTER_CROP:
                    CenterCrop centerCrop = new CenterCrop();
                    list.add(centerCrop);
                    break;
                case CENTER_INSIDE:
                    CenterInside centerInside = new CenterInside();
                    list.add(centerInside);
                    break;
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    FitCenter fitCenter = new FitCenter();
                    list.add(fitCenter);
                    break;
            }
        }

        if (loadConfig.isCircle()) {
            if (loadConfig.getBorderWidth() > 0) {
                CircleBorderTransformation transformation = new CircleBorderTransformation(loadConfig.getBorderWidth(), loadConfig.getBorderColor());
                list.add(transformation);
            } else {
                CircleCrop transformation = new CircleCrop();
                list.add(transformation);
            }
        }
        // 存在数组，则优先取数组
        if (loadConfig.getRoundCornerRadii() != null) {
            if (loadConfig.getRoundCornerRadii()[0] == loadConfig.getRoundCornerRadii()[1] &&
                    loadConfig.getRoundCornerRadii()[0] == loadConfig.getRoundCornerRadii()[2] &&
                    loadConfig.getRoundCornerRadii()[0] == loadConfig.getRoundCornerRadii()[3]) {

                if (loadConfig.getRoundCornerRadii()[0] > 0) {
                    // 如果有边框
                    if (loadConfig.getBorderWidth() > 0) {
                        int[] co = new int[]{
                                (int) loadConfig.getRoundCornerRadii()[0],
                                (int) loadConfig.getRoundCornerRadii()[0],
                                (int) loadConfig.getRoundCornerRadii()[0],
                                (int) loadConfig.getRoundCornerRadii()[0]
                        };
                        RoundedBorderCorners roundedBorderCorners = new RoundedBorderCorners(
                                co,
                                (int) loadConfig.getBorderWidth(),
                                loadConfig.getBorderColor());
                        list.add(roundedBorderCorners);
                    } else {
                        RoundedCorners transformation = new RoundedCorners((int) loadConfig.getRoundCornerRadii()[0]);
                        list.add(transformation);
                    }
                }
            } else {
                // 如果有边框
                if (loadConfig.getBorderWidth() > 0) {
                    int[] co = new int[]{
                            (int) loadConfig.getRoundCornerRadii()[0],
                            (int) loadConfig.getRoundCornerRadii()[1],
                            (int) loadConfig.getRoundCornerRadii()[2],
                            (int) loadConfig.getRoundCornerRadii()[3]
                    };
                    RoundedBorderCorners roundedBorderCorners = new RoundedBorderCorners(
                            co,
                            (int) loadConfig.getBorderWidth(),
                            loadConfig.getBorderColor());
                    list.add(roundedBorderCorners);
                } else {
                    GranularRoundedCorners transformation = new GranularRoundedCorners(
                            loadConfig.getRoundCornerRadii()[0],
                            loadConfig.getRoundCornerRadii()[1],
                            loadConfig.getRoundCornerRadii()[2],
                            loadConfig.getRoundCornerRadii()[3]
                    );
                    list.add(transformation);
                }
            }
        } else {
            if (loadConfig.getRoundCornerRadius() > 0) {
                RoundedCorners transformation = new RoundedCorners((int) loadConfig.getRoundCornerRadius());
                list.add(transformation);
            }
        }

        if (loadConfig.getTransformationList() != null) {
            list.addAll(loadConfig.getTransformationList());
        }
        if (list.size() > 0)
            requestOptions = requestOptions.transform(new MultiTransformation(list));
        if (loadConfig.getFormat() != null) {
            if (loadConfig.getFormat() == Bitmap.Config.RGB_565) {
                requestOptions = requestOptions.format(DecodeFormat.PREFER_RGB_565);
            } else {
                requestOptions = requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
            }
        }
        return requestOptions;
    }

    /**
     * 设置宽高
     *
     * @param loadConfig     配置
     * @param requestOptions 选项
     * @return RequestOptions
     */
    private RequestOptions setWidthAndHeight(@NotNull LoaderBuilder loadConfig, RequestOptions requestOptions) {
        if (loadConfig.getWidth() > 0 && loadConfig.getHeight() > 0) {
            requestOptions = requestOptions.override(loadConfig.getWidth(), loadConfig.getHeight());
        } else if (loadConfig.getWidth() < 0 || loadConfig.getHeight() < 0 || loadConfig.getTargetView() == null) {
            requestOptions = requestOptions.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        }
        return requestOptions;
    }

    /**
     * 设置占位图和错误显示图
     *
     * @param loadConfig     配置
     * @param requestOptions 选项
     * @return RequestOptions
     */
    private RequestOptions setPlaceholderAndErrorDrawable(@NotNull LoaderBuilder loadConfig, RequestOptions requestOptions) {
        if (loadConfig.getPlaceholderDrawable() != null) {
            requestOptions = requestOptions.placeholder(loadConfig.getPlaceholderDrawable());
        }
        if (loadConfig.getPlaceholderResId() != Integer.MIN_VALUE) {
            requestOptions = requestOptions.placeholder(loadConfig.getPlaceholderResId());
        }
        if (loadConfig.getErrorDrawable() != null) {
            requestOptions = requestOptions.placeholder(loadConfig.getErrorDrawable());
        }
        if (loadConfig.getErrorResId() != Integer.MIN_VALUE) {
            requestOptions = requestOptions.placeholder(loadConfig.getErrorResId());
        }
        return requestOptions;
    }

    /**
     * 设置缓存模式
     *
     * @param loadConfig 配置
     * @return RequestOptions
     */
    @NotNull
    private RequestOptions setCacheMode(@NotNull LoaderBuilder loadConfig) {
        RequestOptions requestOptions = RequestOptions.skipMemoryCacheOf(loadConfig.getSkipMemory());
        if (loadConfig.getDiskCache() != LoaderBuilder.DISK_CACHE_DEFAULT) {
            switch (loadConfig.getDiskCache()) {
                case LoaderBuilder.DISK_CACHE_NONE:
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                    break;
                case LoaderBuilder.DISK_CACHE_ALL:
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    break;
                case LoaderBuilder.DISK_CACHE_RESULT:
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    break;
                case LoaderBuilder.DISK_CACHE_SOURCE:
                    requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                    break;
            }
        }
        return requestOptions;
    }
}
