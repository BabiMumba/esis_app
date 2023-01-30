package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.BabiMumba.Esis_app.R

class ThankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank)

        val webView : WebView = findViewById(R.id.web_view)
        // displaying content in WebView from html file that stored in assets folder
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/Sample.html")

    }
}