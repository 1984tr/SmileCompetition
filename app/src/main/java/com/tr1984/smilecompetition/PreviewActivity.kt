package com.tr1984.smilecompetition

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.tr1984.smilecompetition.databinding.ActivityPreviewBinding
import com.tr1984.smilecompetition.util.ImageProcessor

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val cameraSelector by lazy {
        CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
    }
    private val targetResolution = Size(480, 480) // 인식 가능한 최소 크기는 480 x 360 이다.
    private val preview by lazy {
        Preview.Builder().setTargetResolution(targetResolution).build()
    }
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageProcessor: ImageProcessor? = null

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
            cameraProvider = providerFuture.get()
            bindPreview()
        }, ContextCompat.getMainExecutor(this))

        imageProcessor = ImageProcessor(this)
    }

    override fun onResume() {
        super.onResume()
        bindPreview()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.stop()
    }

    private fun bindPreview() {
        val provider = cameraProvider ?: return
        provider.unbindAll()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        provider.bindToLifecycle(this, cameraSelector, preview)


        val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(targetResolution)
                .build()

        imageProcessor?.stop()
        imageProcessor = ImageProcessor(this)

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), { image ->
            val w = image.width
            val h = image.height
            val rotationDegrees = image.imageInfo.rotationDegrees
            Log.d("1984tr", "(w, h) -> ($w, $h), rotationDegrees: $rotationDegrees")
            imageProcessor?.process(image) { left, right, smiling ->
                if (smiling ?: 0.0f < 0.7f) {
                    binding.lblStatus.text = "웃어요!"
                } else {
                    binding.lblStatus.text = "아이 예뻐!"
                }
            }
        })

        provider.bindToLifecycle(this, cameraSelector, imageAnalysis)
    }
}