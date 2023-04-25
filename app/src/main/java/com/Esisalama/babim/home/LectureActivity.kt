package com.Esisalama.babim.home

import android.app.ProgressDialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.Esisalama.babim.R
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
        pd.setMessage("Ouverture du livre....!!")
        pd.setCancelable(true)
        pd.show()
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.setAppCacheMaxSize( 5 * 1024 * 1024)
        webView.settings.allowFileAccess = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true



        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                pd.show()
                view.settings.loadsImagesAutomatically = true;
                super.onPageStarted(view, url, favicon)


            }
            override fun onPageFinished(view: WebView, url: String) {
                if (!view.url.equals(url)) {
                    view.reload()
                    return
                }else{
                    title = view.title
                    super.onPageFinished(view, url)
                }

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
            pd.show()
            Toast.makeText(this, "Rafraichir", Toast.LENGTH_SHORT).show()
            webView.reload()

        }
    }
}