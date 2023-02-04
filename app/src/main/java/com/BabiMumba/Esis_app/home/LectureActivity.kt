package com.BabiMumba.Esis_app.home

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import kotlinx.android.synthetic.main.activity_lecture.*
import java.net.URLEncoder

class LectureActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        webView = findViewById(R.id.my_view_pdf)

        val nom_livre = intent.getStringExtra("nom")
        val lien_livre = intent.getStringExtra("lien_book")

        webView.settings.javaScriptEnabled = true
        val pd = ProgressDialog(this)
        pd.setTitle(nom_livre)
        pd.setMessage("Ouverture du livre....!!!")
        pd.setCancelable(true)
        pd.show()
        webView.settings.builtInZoomControls = true;
        webView.settings.setSupportZoom(true);
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pd.show()
            }
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pd.dismiss()
            }

        }
        var url = ""
        try {
            url = URLEncoder.encode(lien_livre, "UTF-8")
        } catch (ex: Exception) {
        }
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        syncroniser.setOnClickListener {
            Toast.makeText(this, "Rafrechire", Toast.LENGTH_SHORT).show()
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        }
    }
}