package com.appsflyer.oaid.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.appsflyer.oaid.OaidClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Thread {
            val oaidString = OaidClient.fetch(this, 1200, TimeUnit.MILLISECONDS)
            if (oaidString != null) runOnUiThread { oaid.text = oaidString }
        }.start()
    }
}