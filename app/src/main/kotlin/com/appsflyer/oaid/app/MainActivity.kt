package com.appsflyer.oaid.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.oaid.OaidClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {
            val info = OaidClient(this, 1, TimeUnit.SECONDS).run {
                setLogging(BuildConfig.DEBUG)
                fetch()
            }
            runOnUiThread {
                if (info == null) {
                    oaid.text = "No Oaid"
                } else {
                    oaid.text = info.id
                    info.lat?.let {
                        lat.text = it.toString()
                    }
                }
            }
        }.start()
    }
}