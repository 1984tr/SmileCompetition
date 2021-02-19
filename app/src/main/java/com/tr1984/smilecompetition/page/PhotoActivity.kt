package com.tr1984.smilecompetition.page

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tr1984.smilecompetition.databinding.ActivityPhotoBinding
import java.io.File

class PhotoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = intent.getStringExtra("fileName")
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        Glide.with(this).load(file).into(binding.img)
    }
}