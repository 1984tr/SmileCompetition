package com.tr1984.smilecompetition.page

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tr1984.smilecompetition.databinding.ActivityMainBinding
import com.tr1984.smilecompetition.util.Analytics

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Analytics.logEvent("page_main")
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
            startActivity(Intent(this@MainActivity, PreviewActivity::class.java))
        }
    }

    private fun actionSmile() {
        Analytics.logEvent("click_action")
        if (!hasAllPermissions()) {
            requestPermissions()
            return
        }
        startActivity(Intent(this@MainActivity, PreviewActivity::class.java))
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
