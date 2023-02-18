package com.BabiMumba.Esis_app.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_horaire.*

class HoraireActivity : AppCompatActivity() {

    private lateinit var backDrop: View
    private lateinit var lytMic: View
    private lateinit var lytCall: View
    private var rotate = false

    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_horaire)

        backDrop = findViewById(R.id.back_drop)
        lytMic = findViewById(R.id.lyt_mic)
        lytCall = findViewById(R.id.lyt_call)

        val fabMic: FloatingActionButton = findViewById(R.id.fab_mic)
        val fabCall: FloatingActionButton = findViewById(R.id.fab_call)
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)

        initShowOut(lytMic)
        initShowOut(lytCall)

        backDrop.visibility = View.GONE

        backDrop.setOnClickListener {
            toggleFabMode(fabAdd)
        }

        fabMic.setOnClickListener {
            Toast.makeText(this, "Voice clicked", Toast.LENGTH_SHORT).show()
        }

        fabCall.setOnClickListener {
            Toast.makeText(this, "Call clicked", Toast.LENGTH_SHORT).show()
        }


        webView = findViewById(R.id.web_horaire)
        val promot_link = intent.getStringExtra("promot_link").toString()

        val lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20$promot_link.pdf"

        val progressBar = ProgressDialog(this)
        progressBar.setTitle("Patienter...")
        progressBar.setMessage("chargement de la page")
        progressBar.setCancelable(true)
        if (savedInstanceState != null){
            webView.restoreState(savedInstanceState)
        }else{
            webView.settings.javaScriptEnabled = true
            webView.settings.builtInZoomControls = true
            webView.settings.setSupportZoom(true)
            webView.settings.allowFileAccess = true
            webView.settings.useWideViewPort = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = true
        }

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
        webView.webChromeClient = object :WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressHori.visibility = View.VISIBLE
                progressHori.progress = newProgress
                progressBar.show()
                if (newProgress == 100){
                    progressHori.visibility = View.GONE
                    if (view != null) {
                        title = view.title
                    }
                }
                super.onProgressChanged(view, newProgress)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    //bouton floating

    private fun toggleFabMode(v: View) {
        rotate = rotateFab(v, !rotate)
        if (rotate) {
            showIn(lytMic)
            showIn(lytCall)
            backDrop.visibility = View.VISIBLE
        } else {
            showOut(lytMic)
            showOut(lytCall)
            backDrop.visibility = View.GONE
        }
    }

    private fun initShowOut(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    private fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .rotation(if (rotate) 135f else 0f)
        return rotate
    }

    private fun showIn(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
    }

    private fun showOut(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }
}