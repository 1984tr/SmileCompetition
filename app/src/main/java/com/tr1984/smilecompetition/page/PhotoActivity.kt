package com.tr1984.smilecompetition.page

import android.os.Bundle
import android.os.Environment
import android.util.Log
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
        Log.d("1984tr", "${file}")
        Glide.with(this).load(file).into(binding.img)
    }
}