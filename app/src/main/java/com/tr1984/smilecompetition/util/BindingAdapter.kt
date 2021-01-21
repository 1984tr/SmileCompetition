package com.tr1984.smilecompetition.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isSelected")
fun setSelected(view: View, value: Boolean) {
    view.isSelected = value
}