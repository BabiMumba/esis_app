package com.Esisalama.babim.home

import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Esisalama.babim.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_actualite.*
import kotlinx.android.synthetic.main.activity_horaire.*
import kotlinx.android.synthetic.main.activity_horaire.non_internet
import kotlinx.android.synthetic.main.activity_horaire.progressHori


class ActualiteActivity : AppCompatActivity() {
    private var tlc_s: Int? = null
    lateinit var webView: WebView

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                tlc_s = tlc_s?.plus(1)
                val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply{
                    tlc_s?.let { putInt("point", it)
                    }
                }.apply()
                val snackbar = Snackbar
                    .make(findViewById(R.id.actualite_id), "telechargement termine", Snackbar.LENGTH_LONG)
                    .setAction("ouvrir") { v12: View? ->
                        startActivity(Intent(this@ActualiteActivity,LectureActivity_Pdf::class.java))
                    }
                snackbar.show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualite)
        load_data()

        webView = findViewById(R.id.web_eventmtn)

        if (isConnectedNetwork(this)){
            //connecter
        }else{
            web_eventmtn.visibility = View.GONE
            text_noi.visibility = View.VISIBLE
            non_internet.visibility = View.VISIBLE

        }

        val lien = intent.getStringExtra("url_link").toString()
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        //webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //progressBar.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

               // progressBar.dismiss()
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
                if (newProgress == 100){
                    progressHori.visibility = View.GONE
                    if (view != null) {
                        title = view.title
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }


        webView.loadUrl(lien)
        //Environment.DIRECTORY_DOWNLOADS + "/syllabus esis/", "$nom.pdf"

        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(
                Uri.parse(url)
            )
            val filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //notifie la personne si le telechargment termine
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                filename
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(
                applicationContext,
                "Telechargement...",  //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG
            ).show()
        }

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
    fun save_toImage(){

    }


    /*
      private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
     */

    fun load_data(){
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val tlc = sharedPreferences.getInt("point",0)
        tlc_s = tlc
    }
    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.non_internet)
        dialog.setCancelable(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.findViewById<View>(R.id.quit).setOnClickListener { v: View? -> onBackPressed() }
        // dialog.findViewById<View>(R.id.btn_feedback).setOnClickListener { v: View? -> startActivity(settg)}
        dialog.show()
        dialog.window!!.attributes = lp
    }
}