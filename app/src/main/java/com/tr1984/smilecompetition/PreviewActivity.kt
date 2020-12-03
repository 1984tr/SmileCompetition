package com.tr1984.smilecompetition

import android.os.Bundle
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.tr1984.smilecompetition.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val cameraSelector by lazy {
        CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
    }
    private val preview by lazy {
        Preview.Builder().setTargetResolution(targetResolution).build()
    }
    private val targetResolution = Size(480, 480) // 인식 가능한 최소 크기는 480 x 360 이다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityPreviewBinding.inflate(layoutInflater)
                .apply {
                    closeBtn.setOnClickListener { finish() }
                }.also {
                    binding = it
                }.run {
                    setContentView(root)
                }

        val providerFuture = ProcessCameraProvider.getInstance(this)
        providerFuture.addListener({
            bindPreview(providerFuture.get())
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(provider: ProcessCameraProvider) {
        provider.unbindAll()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        provider.bindToLifecycle(this, cameraSelector, preview)
    }
}