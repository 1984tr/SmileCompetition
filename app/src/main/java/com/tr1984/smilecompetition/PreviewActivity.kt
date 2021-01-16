package com.tr1984.smilecompetition

import android.content.Intent
import android.os.Bundle
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.tr1984.smilecompetition.databinding.ActivityPreviewBinding
import com.tr1984.smilecompetition.util.ImageProcessor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.min

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
    private var startTimestamp = Long.MAX_VALUE

    private val dataStore = createDataStore("smile_config")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityPreviewBinding.inflate(layoutInflater)
            .apply {
                closeBtn.setOnClickListener { finish() }
                calendarBtn.setOnClickListener { moveToCalendar(false) }
                settingBtn.setOnClickListener { moveToSetting() }
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

        lifecycleScope.launch {
            val preferences = dataStore.data.first()
            val key = preferencesKey<Long>("timer")
            val timer = preferences[key] ?: 10000L
            binding.progress.max = timer.toInt()
        }
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
            imageProcessor?.process(image) { left, right, smiling ->
                if (smiling ?: 0.0f > 0.8f) {
                    if (startTimestamp == Long.MAX_VALUE) {
                        startTimestamp = System.currentTimeMillis()
                    } else {
                        val progress = min(
                            (System.currentTimeMillis() - startTimestamp).toInt(),
                            binding.progress.max
                        )
                        binding.progress.progress = progress
                        if (progress >= binding.progress.max) {
                            moveToCalendar(true)
                            finish()
                        }
                    }
                } else {
                    startTimestamp = Long.MAX_VALUE
                    binding.progress.progress = 0
                }
            }
        })

        provider.bindToLifecycle(this, cameraSelector, imageAnalysis)
    }

    private fun moveToCalendar(withInsert: Boolean) {
        startActivity(Intent(this, CalendarActivity::class.java).apply {
            putExtra("withInsert", withInsert)
        })
    }

    private fun moveToSetting() {
        startActivity(Intent(this, ConfigActivity::class.java))

    }
}