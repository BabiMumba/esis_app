package com.BabiMumba.Esis_app.home


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.BabiMumba.Esis_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_horaire.*
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class HoraireActivity : AppCompatActivity() {

    private lateinit var backDrop: View
    private lateinit var lytMic: View
    private lateinit var lytCall: View
    private lateinit var lyt_down: View
    private var rotate = false
    var theDir = Environment.getExternalStorageDirectory().toString() + "/Download/"


    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_horaire)
/*
  val progressBar = ProgressDialog(this)
        progressBar.setTitle("Patienter...")
        progressBar.setMessage("chargement de la page")
        progressBar.setCancelable(true)

 */

        if (isConnectedNetwork(this)){

        }else{
            supportActionBar?.hide()
           // progressBar.dismiss()
            network_visibility.visibility = View.VISIBLE
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
            _createFolder(File(theDir + "Horaire"))
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

        if (savedInstanceState != null){
            webView.restoreState(savedInstanceState)
        }else{
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.builtInZoomControls = true
            webView.settings.setSupportZoom(true)
            webView.settings.allowContentAccess = true
            webView.isHorizontalScrollBarEnabled = false
            webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            webView.settings.allowFileAccess = true
            webView.settings.useWideViewPort = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = true
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        }

        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                //progressBar.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //progressBar.dismiss()
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
               // progressBar.show()
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

    private fun telecharger(lien:String,nom:String) {
        var url1: URL? = null
        try {
            url1 = URL(lien)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        val request = DownloadManager.Request(Uri.parse(url1.toString()))
        request.setTitle(nom)
        request.setMimeType("application/pdf")
        request.allowScanningByMediaScanner()
        request.setDescription("Telechargement...")
        request.setVisibleInDownloadsUi(true)
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS+"/Horaire/", "$nom.pdf"
        )
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(this, "lancement du telechargement", Toast.LENGTH_SHORT).show()
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

    //creer un dossier

    fun _createFolder(file: File) {
        val promot_link = intent.getStringExtra("promot_link").toString()
        val lien = "https://www.esisalama.com/assets/upload/horaire/pdf/HORAIRE%20$promot_link.pdf"

        if (!file.exists()) {
            val succ = file.mkdir()
            if (succ) {
                telecharger(lien,"Horaire $promot_link")
                //Toast.makeText(applicationContext, "dossier creer avec succe", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "dossier non creer", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (file.exists()) {
            val noms = "Horaire $promot_link"
            val file2:File = File("$theDir/Horaire/$noms.pdf")
            if (file2.exists()){
                Toast.makeText(this, "c fichier existe", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Voulez-vous ecraser l'ancien fichier sauvegarder de $promot_link")
                    .setTitle("Sauvegarder l'horaire")
                    .setNeutralButton("Ouvrir"){
                        _: DialogInterface?, i:Int ->
                        startActivity(
                            Intent(this, DocumentActivity2::class.java)
                                .putExtra("path", file2.absolutePath)
                        )
                    }
                    .setNegativeButton("Non", null)
                    .setPositiveButton("oui") { _: DialogInterface?, i: Int ->
                        //supprimer le fichier
                        val succe = file2.delete()
                        if (succe){
                            Toast.makeText(this, "ancien fichier ecraser", Toast.LENGTH_SHORT).show()
                            telecharger(lien,noms)
                        }else{
                            Toast.makeText(this, "erreur de suppression du fichier", Toast.LENGTH_SHORT).show()
                        }

                    }
                    .show()
            }else{
                Toast.makeText(this, "fichier n'existe pas ", Toast.LENGTH_SHORT).show()
               telecharger(lien,noms)
            }
            //Toast.makeText(applicationContext, "le dossier existe", Toast.LENGTH_SHORT).show()
        }
    }

    //creer un fichier

    fun _createFile(file: File) {
        if (!file.exists()) {
            try {
                val succ = file.createNewFile()
                if (succ) {
                    Toast.makeText(applicationContext, "fichier creer", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(applicationContext, "erreur de creation", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (file.exists()) {
            Toast.makeText(applicationContext, "fichier existe deja!", Toast.LENGTH_SHORT).show()
        }
    }

    //suprimer un dossier

    fun deleteFolder(dir: File) {
        if (dir.isDirectory) for (subDir in Objects.requireNonNull(dir.listFiles())) deleteFolder(
            subDir
        )
        val succ = dir.delete()
        if (succ) {
            Toast.makeText(applicationContext, "dossier suprimer ", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(applicationContext, "Folder could not deleted", Toast.LENGTH_SHORT)
            .show()
    }

    //suprimer un fichier

    fun deleteFile(file: File) {
        if (file.exists()) {
            if (file.isFile) {
                val succ = file.delete()
                if (succ) {
                    Toast.makeText(applicationContext, "fichier suprimer", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(
                    applicationContext,
                    "fichier non suprimer",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (file.isDirectory or file.exists()) {
                deleteFolder(file)
            }
        } else Toast.makeText(applicationContext, "c'est n'esxiste pas", Toast.LENGTH_SHORT)
            .show()
    }


}