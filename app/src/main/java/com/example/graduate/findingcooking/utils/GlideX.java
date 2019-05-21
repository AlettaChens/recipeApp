package com.example.graduate.findingcooking.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.graduate.findingcooking.R;

import java.io.File;


public class GlideX {
    private static GlideX instance;
    public static final int placeholderImage = R.mipmap.default_user_avatar;
    public static final int errorImage = R.mipmap.default_user_avatar;


    public static GlideX getInstance() {
        if (instance == null) {
            synchronized (GlideX.class) {
                if (instance == null) {
                    instance = new GlideX();
                }
            }
        }
        return instance;
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(placeholderImage).error(errorImage).diskCacheStrategy(DiskCacheStrategy
                .ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 从文件中加载图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    public void loadImage(Context context, File file, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(placeholderImage).error(errorImage).diskCacheStrategy(DiskCacheStrategy
                .ALL);
        Glide.with(context).load(file).apply(options).into(imageView);
    }

    /**
     * 从资源中加载图片
     *
     * @param context
     * @param resourceId
     * @param imageView
     */
    public void loadImage(Context context, int resourceId, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(placeholderImage).error(errorImage).diskCacheStrategy(DiskCacheStrategy
                .ALL);
        Glide.with(context).load(resourceId).apply(options).into(imageView);
    }

    /**
     * 加载指定大小图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadImageSize(Context context, String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions().centerCrop().placeholder(placeholderImage).error(errorImage).override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /**
     * 跳过内存缓存
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadImageSizekipMemoryCache(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImage).error(errorImage).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop().circleCrop().placeholder(placeholderImage).error(errorImage).diskCacheStrategy
                (DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载动态图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    private void loadGif(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImage).error(errorImage);
        Glide.with(context).load(url).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);

    }
}
