package ru.maxdexter.mytasks.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestOptions
import com.google.rpc.context.AttributeContext
import ru.maxdexter.mytasks.R
import java.io.File
import java.net.URI

fun <T> ImageView.setImage(uri: T){
    Glide.with(this.context).load(uri).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_brocken_img)).diskCacheStrategy(
        DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
}

fun<T>  ImageView.setImagePrev(uri: T,type: String){
        val typeGroup = type.split("/").first()
    if (typeGroup == "image" || typeGroup == "video"){
        loadImage(uri,this)
    } else {
        val image = equalsMIMEType(type,this.context)
        loadImage(image,this)
    }

}

fun <T> loadImage(uri: T,imageView: ImageView){
    Glide.with(imageView.context)
        .load(uri).apply(RequestOptions().placeholder(R.drawable.loading_animation).error(R.drawable.ic_brocken_img)).override(300).diskCacheStrategy(
            DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun  equalsMIMEType(type: String, context: Context): Drawable{
  return when(type){
          "application/pdf" -> context.resources.getDrawable(R.drawable.pdf,context.theme)
          "application/zip", "application/gzip" -> context.resources.getDrawable(R.drawable.zip,context.theme)
          "application/x-bittorrent" -> context.resources.getDrawable(R.drawable.utorrent,context.theme)
          else -> context.resources.getDrawable(R.drawable.content,context.theme)
  }
}