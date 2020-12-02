package com.tr1984.smilecompetition

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tr1984.smilecompetition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                ActivityMainBinding.inflate(layoutInflater)
                        .apply {
                            btnSmile.setOnClickListener { actionSmile() }
                        }.root
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasAllPermissions()) {
            startActivity(Intent(this@MainActivity, CameraXLivePreviewActivity::class.java))
        }
    }

    private fun actionSmile() {
        if (!hasAllPermissions()) {
            requestPermissions()
            return
        }
        startActivity(Intent(this@MainActivity, CameraXLivePreviewActivity::class.java))
    }

    private fun hasAllPermissions(): Boolean {
        val permissions = getMandatoryPermissions()
        return permissions.count { ActivityCompat.checkSelfPermission(this@MainActivity, it) != PackageManager.PERMISSION_GRANTED } <= 0
    }

    private fun requestPermissions() {
        val permissions = getMandatoryPermissions().filter { ActivityCompat.checkSelfPermission(this@MainActivity, it) != PackageManager.PERMISSION_GRANTED }
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1000)
    }

    private fun getMandatoryPermissions(): Array<String> {
        val pkgInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        return pkgInfo.requestedPermissions
    }
}
