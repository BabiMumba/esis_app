/*
babi mumba
modifier: 07/02/2023
 */
package com.Esisalama.babim.home

import android.Manifest
import android.app.DownloadManager
import android.content.*
import com.Esisalama.babim.R
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.Utils.Constant
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_detaille.*
import kotlinx.android.synthetic.main.content_syllabus.*
import java.net.MalformedURLException
import java.net.URL

class DetailleActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var currentuser: FirebaseUser? = null
    private var tlc_s: Int? = null
    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null
    lateinit var collection_name:String

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val snackbar = Snackbar
                    .make(findViewById(R.id.coodid), "telechargement termine", Snackbar.LENGTH_LONG)
                    .setAction("ouvrir") { v12: View? ->
                        startActivity(Intent(this@DetailleActivity,LectureActivity_Pdf::class.java))
                    }
                snackbar.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaille)

        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)

        auth = Firebase.auth
        db = Firebase.firestore
        currentuser = auth.currentUser

        collection_name = if (adm == "oui"){
            Constant.Admin
        }else{
            Constant.Etudiant
        }
        MobileAds.initialize(this){
            Log.d(TAG,"inias complet")
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("f01a4b37-2568-4128-9894-6d6453fd67bb"))
                .build()
        )
        adview = findViewById(R.id.bannerAd)
        val adRequest = AdRequest.Builder().build()
        adview?.loadAd(adRequest)
        adview?.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.d(TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailedToLoad: $p0")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "onAdImpression: ")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(TAG, "onAdOpened: ")
            }
        }

        storageReference = FirebaseStorage.getInstance().reference

        firebaseAuth = FirebaseAuth.getInstance()
        load_data()
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        val nom_prof = intent.getStringExtra("nom_prof")
        val syllabus = intent.getStringExtra("syllabus")
        val user = intent.getStringExtra("user")
        val id_uses = intent.getStringExtra("id_users")
        val date = intent.getStringExtra("date")
        val promo = intent.getStringExtra("promo").toString()
        val descrip = intent.getStringExtra("description")
        val cover_ic = intent.getStringExtra("couverture")


        nom_du_prof.text = nom_prof
        name_syllabus.text = syllabus
        user_id.text = user
        date_id.text = date
        description_tv.text = descrip
        my_txtv_pm.text = promo
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide
            .with(this)
            .load(cover_ic)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(pdf_id_icone)

        setListener()
        val firebaseUser = firebaseAuth.currentUser
        val id_last = firebaseUser?.uid.toString()
        val admin_state = sharedPreferences.getString("admin_assistant",null)
        if ((id_uses.toString() == id_last)|| (admin_state == "oui")) {
            dele_pst.visibility = View.VISIBLE
        } else {
            dele_pst.visibility = View.GONE
        }

    }

    fun load_data(){
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val tlc = sharedPreferences.getInt("point",0)
        tlc_s = tlc
    }

    fun setListener() {
        dele_pst.setOnClickListener {
            val pop = PopupMenu(this@DetailleActivity, dele_pst)
            pop.menuInflater.inflate(R.menu.popup_menu, pop.menu)
            pop.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (item.itemId == R.id.delete_id) {
                       DeleteBook()
                    }
                    return true
                }
            })
            pop.show()


        }
        btn_downl.setOnClickListener {
            tlc_s = tlc_s?.plus(1)
            val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                tlc_s?.let { putInt("point", it)
                }
            }.apply()
            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        telecharger()
                        incrementDowload()
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@DetailleActivity,
                            "vous devez accepter pour continuer",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }
        btn_read.setOnClickListener {
            incrementRead()
            val lien = intent.getStringExtra("lien_book")
            val intent = Intent(this, LectureActivity::class.java)
            intent.putExtra("nom", name_syllabus.text.toString())
            intent.putExtra("lien_book", lien)
 //incrementer le nombre de lecture
            startActivity(intent)
        }
    }

    private fun DeleteBook() {
        val doc = intent.getStringExtra("id_book").toString()
        db.collection("syllabus").document(doc)
            .delete()
            .addOnSuccessListener {
                DeleteBookStorage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur:${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun telecharger() {
        val intent = intent
        val lien = intent.getStringExtra("lien_book")
        val nom = intent.getStringExtra("syllabus")
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
            Environment.DIRECTORY_DOWNLOADS, "$nom.pdf"
        )
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        //increment le chiffre de telechargement
        incrementDowload()
        Toast.makeText(this, "lancement du telechargement", Toast.LENGTH_SHORT).show()
    }
    private fun incrementDowload() {
        val doc = intent.getStringExtra("id_book").toString()
        db.collection("syllabus").document(doc)
            .update("download",FieldValue.increment(1))
            .addOnSuccessListener {
                Toast.makeText(this, "incrementer", Toast.LENGTH_SHORT).show()
                
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur:${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun incrementRead() {
        val doc = intent.getStringExtra("id_book").toString()
        db.collection("syllabus").document(doc)
            .update("like",FieldValue.increment(1))
            .addOnSuccessListener {
                Log.d("Detaille_activity","lire add")
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur:${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    fun DeleteBookStorage() {
        val image_name_id = intent.getStringExtra("image_url").toString()
        val storageRef = storageReference
        val desertRef = storageRef.child(image_name_id)
        desertRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "fichier supprime", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }
    fun DeleteimagCoverStorage() {
        val image_name_id = intent.getStringExtra("image_url")
        val storageRef = storageReference
        val desertRef = storageRef.child(image_name_id.toString())
        desertRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "fichier supprime", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }

}