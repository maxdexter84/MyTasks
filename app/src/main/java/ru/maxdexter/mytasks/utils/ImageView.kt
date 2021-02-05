package ru.maxdexter.mytasks.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.maxdexter.mytasks.R

fun <T> ImageView.setImage(context: Context, uri: T){
    Glide.with(context).load(uri).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_brocken_img)).diskCacheStrategy(
        DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
}

fun <T> ImageView.setImagePrev(context: Context, uri: T){
    Glide.with(context).load(uri).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_brocken_img)).override(300).diskCacheStrategy(
        DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
}