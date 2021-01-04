package com.tr1984.smilecompetition.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class ImageProcessor(context: Context) {

    private val detector: FaceDetector
    private var isShutdown = false
    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

    init {
        // 아래의 옵션을 같이 사용하면 퍼포먼스가 떨어집니다.
        // 윤곽선 인식 + 랜드마크
        // 윤곽선 인식 + 분류
        // 윤곽선 인식 + 랜드마크 인식 + 분류
        // *윤곽선 인식을 한 후 얼굴 추적을 사용하면 결과가 잘못 나오거나 인식 속도가 아주 안 좋다고 하네요!
        val faceDetectorOptions = FaceDetectorOptions.Builder()
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE) // 눈, 귀, 코, 뺨, 입과 같은 얼굴의 '랜드마크'를 식별할 것인지 여부입니다.
                .setClassificationMode(FaceDetectorOptions.CONTOUR_MODE_NONE) // 얼굴 특징의 윤곽선을 인식할지 여부입니다. 윤곽은 이미지 속 가장 뚜렷한 얼굴에 대해서만 인식됩니다.
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // 얼굴을 '웃고 있음'이나 '눈을 뜸'과 같은 카테고리로 분류할 것인지 여부입니다.
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE) // 얼굴을 인식할 때 속도 또는 정확도를 우선시합니다.
                .setMinFaceSize(0.1f) // 이미지를 기준으로, 인식할 얼굴의 최소 크기
                .enableTracking() // 얼굴에 ID를 할당할 것인지 여부이며, 서로 다른 이미지에서 얼굴을 추적하는 데 사용할 수 있습니다.
                .build()
        detector = FaceDetection.getClient(faceDetectorOptions)
    }

    fun stop() {
        executor.shutdown()
        isShutdown = true
        detector.close()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    fun process(image: ImageProxy, callback: (Float?, Float?, Float?) -> Unit) {
        if (isShutdown) {
            return
        }
        val img = image.image ?: return
        val input = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)
        detector.process(input).addOnSuccessListener(executor) { results ->
            for (face in results) {
                callback.invoke(face.leftEyeOpenProbability, face.rightEyeOpenProbability, face.smilingProbability)
            }
        }.addOnFailureListener(executor) { e ->
            e.printStackTrace()
        }.addOnCompleteListener {
            image.close()
        }
    }
}