package com.BabiMumba.Esis_app.home

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.BabiMumba.Esis_app.R

class HoraireActivity : AppCompatActivity() {
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horaire)
        webView = findViewById(R.id.web_horaire)
        val promot_link = intent.getStringExtra("promot_link").toString()
        val lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20$promot_link.pdf"
        val progressBar = ProgressDialog(this)
        progressBar.setTitle("Patienter...")
        progressBar.setMessage("chargement de la page")
        progressBar.setCancelable(true)
        progressBar.show()
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.setAppCachePath(this.cacheDir.absolutePath)
        webView.settings.setAppCacheMaxSize( 5 * 1024 * 1024)
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.dismiss()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url!!)
                return true
            }
        }

        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$lien")
        println("le lien:$lien")

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}