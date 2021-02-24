package com.tr1984.smilecompetition.page

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tr1984.smilecompetition.databinding.ActivityPhotoBinding
import com.tr1984.smilecompetition.util.FileUtils
import java.io.File

class PhotoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = intent.getStringExtra("fileName")
        val file = File(FileUtils.getOutputDirectory(this), fileName)
        val uri = Uri.fromFile(file)
        Log.d("1984tr", "name: ${uri}, ${file.exists()}")
        Log.d("1984tr", "${file}")
        Glide.with(this).load(uri).into(binding.img)
    }
}