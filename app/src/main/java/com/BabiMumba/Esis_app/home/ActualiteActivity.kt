package com.BabiMumba.Esis_app.home

import android.app.Dialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_actualite.*

class ActualiteActivity : AppCompatActivity() {
    lateinit var webView: WebView

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val snackbar = Snackbar
                    .make(findViewById(R.id.actualite_id), "telechargement termine", Snackbar.LENGTH_LONG)
                    .setAction("ouvrir") { v12: View? ->
                        Toast.makeText(this@ActualiteActivity, "ouvrir clicker", Toast.LENGTH_SHORT).show()
                    }
                snackbar.show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualite)

        if (isConnectedNetwork(this)){
            //connecter
        }else{
            web_eventmtn.visibility = View.GONE
            txvp.visibility = View.VISIBLE
            non_internet.visibility = View.VISIBLE
        }

        webView = findViewById(R.id.web_eventmtn)

        val lien = intent.getStringExtra("url_link").toString()
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        val progressBar = ProgressDialog(this)
        progressBar.setTitle("Patienter...")
        progressBar.setMessage("chargement de la page")
        progressBar.setCancelable(false)
        progressBar.show()

        web_eventmtn.settings.javaScriptEnabled = true

        web_eventmtn!!.settings.builtInZoomControls = true
        web_eventmtn.webViewClient = object : WebViewClient(){
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


        web_eventmtn.loadUrl(lien)
        //Environment.DIRECTORY_DOWNLOADS + "/syllabus esis/", "$nom.pdf"
        web_eventmtn.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(
                Uri.parse(url)
            )

            val filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS+ "/syllabus esis/",
                filename
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(
                applicationContext,
                "Telechargement de l'horaire",  //To notify the Client that the file is being downloaded
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