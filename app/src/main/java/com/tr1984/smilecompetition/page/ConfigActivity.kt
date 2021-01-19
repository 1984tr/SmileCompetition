package com.tr1984.smilecompetition.page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModelProvider
import com.tr1984.smilecompetition.databinding.ActivityConfigBinding

class ConfigActivity : AppCompatActivity() {

    private lateinit var viewModel: ConfigViewModel
    private lateinit var binding: ActivityConfigBinding

    private val dataStore = createDataStore("smile_config")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ConfigViewModel::class.java)

        setContentView(ActivityConfigBinding.inflate(layoutInflater)
            .apply {
                lifecycleOwner = this@ConfigActivity
                viewModel = this@ConfigActivity.viewModel

                btnClose.setOnClickListener { finish() }

            }.also {
                binding = it
            }.root)
    }
}