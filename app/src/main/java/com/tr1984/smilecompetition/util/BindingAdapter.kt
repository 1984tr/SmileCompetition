package com.tr1984.smilecompetition.util

import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

@BindingAdapter("app:isSelected")
fun setSelected(view: View, value: Boolean) {
    view.isSelected = value
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, fileName: String) {
    val context = view.context
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
    Glide.with(context).load(file).apply(RequestOptions.circleCropTransform()).into(view)
}