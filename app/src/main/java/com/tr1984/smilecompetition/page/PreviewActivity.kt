package com.tr1984.smilecompetition.page

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.tr1984.smilecompetition.databinding.ActivityPreviewBinding
import com.tr1984.smilecompetition.util.*
import com.tr1984.smilecompetition.util.Logger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
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
    private var imageCapture: ImageCapture? = null
    private var startTimestamp = Long.MAX_VALUE
    private var isDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Analytics.logEvent("page_preview")
        ActivityPreviewBinding.inflate(layoutInflater)
            .apply {
                closeBtn.setOnClickListener { finish() }
                cameraBtn.setOnClickListener { takePicture() }
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
    }

    override fun onResume() {
        super.onResume()
        isDone = false
        bindPreview()
        loadConfig()
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
                        if (progress >= binding.progress.max && !isDone) {
                            isDone = true
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

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        provider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview, imageCapture)
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
    }

    private fun loadConfig() {
        lifecycleScope.launch {
            val dataStore = createDataStore("smile_config")
            val preferences = dataStore.data.first()
            val key = preferencesKey<Int>("duration")
            val timer = preferences[key] ?: DEFAULT_DURATION
            Logger.d("timer: $timer")
            binding.progress.max = timer * 1000
        }
    }

    private fun takePicture() {
        capture()
    }

    private fun moveToCalendar(withInsert: Boolean) {
        if (withInsert) {
            capture {
                startActivity(Intent(this, CalendarActivity::class.java).apply {
                    putExtra("withInsert", withInsert)
                })
            }
        } else {
            startActivity(Intent(this, CalendarActivity::class.java).apply {
                putExtra("withInsert", withInsert)
            })
        }
    }

    private fun moveToSetting() {
        startActivity(Intent(this, ConfigActivity::class.java))
    }

    private fun capture(callback: (() -> Unit)? = null) {
        var photoFile = FileUtils.createFile(FileUtils.getOutputDirectory(this))
        if (photoFile.exists()) {
            val deleted = photoFile.delete()
            Logger.d( "savedUri: $deleted")
            photoFile = FileUtils.createFile(FileUtils.getOutputDirectory(this))
        }
        Logger.d(photoFile.absolutePath)
        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputFileOptions,
            Executors.newSingleThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                    Logger.d( "savedUri: ${outputFileResults.savedUri}")

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        sendBroadcast(Intent(android.hardware.Camera.ACTION_NEW_PICTURE, savedUri))
                    }

                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)
                    MediaScannerConnection.scanFile(
                        this@PreviewActivity,
                        arrayOf(savedUri.toFile().absolutePath),
                        arrayOf(mimeType)
                    ) { _, uri ->
                        Logger.d("Image capture scanned into media store: $uri")
                    }
                    callback?.invoke()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    callback?.invoke()
                }
            }) ?: callback?.invoke()
    }
}