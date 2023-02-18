package com.BabiMumba.Esis_app.home


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
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
    private lateinit var lyt_down: View
    private var rotate = false


    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_horaire)


        if (isConnectedNetwork(this)){

        }else{
            visibility_page.visibility = View.GONE
            lyt_btn.visibility = View.GONE
        }
        webView = findViewById(R.id.web_horaire)
        val promot_link = intent.getStringExtra("promot_link").toString()

        val lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20$promot_link.pdf"

        backDrop = findViewById(R.id.back_drop)
        lytMic = findViewById(R.id.lyt_mic)
        lytCall = findViewById(R.id.lyt_call)
        lyt_down = findViewById(R.id.lyt_downl)

        val fabMic: FloatingActionButton = findViewById(R.id.fab_mic)
        val fabCall: FloatingActionButton = findViewById(R.id.fab_call)
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        val dowload: FloatingActionButton = findViewById(R.id.dowload_btn)

        initShowOut(lytMic)
        initShowOut(lytCall)
        initShowOut(lyt_down)

        backDrop.visibility = View.GONE

        fabAdd.setOnClickListener { v: View ->
            toggleFabMode(v)
        }

        backDrop.setOnClickListener {
            toggleFabMode(fabAdd)
        }
        dowload.setOnClickListener {

            Toast.makeText(this, "telecharger", Toast.LENGTH_SHORT).show()
        }

        fabMic.setOnClickListener {
            val actulaity_link = "https://www.esisalama.com/index.php?module=horaire"
            //val actulaity_link = "https://www.esisalama.com/index.php"
            val intent = Intent(this, ActualiteActivity::class.java)
            intent.putExtra("url_link",actulaity_link)
            startActivity(intent)

        }

        fabCall.setOnClickListener {
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$lien")
        }
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
            showIn(lyt_down)
            backDrop.visibility = View.VISIBLE
        } else {
            showOut(lytMic)
            showOut(lytCall)
            showOut(lyt_down)
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