package com.appsflyer.oaid.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.oaid.OaidClient
import com.appsflyer.oaid.app.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread {
            val info = OaidClient(this, 1, TimeUnit.SECONDS).run {
                setLogging(BuildConfig.DEBUG)
                fetch()
            }
            runOnUiThread {
                if (info == null) {
                    binding.oaid.text = "No Oaid"
                } else {
                    binding.oaid.text = info.id
                    info.lat?.let {
                        binding.lat.text = it.toString()
                    }
                }
            }
        }.start()
    }
}