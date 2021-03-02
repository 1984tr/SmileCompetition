package com.tr1984.smilecompetition.util

import android.net.Uri
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
    val file = File(FileUtils.getOutputDirectory(context), name)
    val uri = Uri.fromFile(file)
    Logger.d("adapter name: ${uri}, ${file.exists()}")
    Glide.with(context)
        .load(uri)
        .apply(RequestOptions.circleCropTransform())
        .into(view)
}