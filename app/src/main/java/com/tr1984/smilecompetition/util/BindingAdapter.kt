package com.tr1984.smilecompetition.util

import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

@BindingAdapter("app:isSelected")
fun setSelected(view: View, value: Boolean) {
    view.isSelected = value
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, fileName: String?) {
    val name = fileName ?: return
    val context = view.context
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), name)
    Log.d("1984tr", "name: ${file}, ${file.exists()}")
    Glide.with(context)
        .load(file)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
//        .apply(RequestOptions.circleCropTransform())
        .into(view)
}