/*
babi mumba
modifier: 07/02/2023
 */
package com.BabiMumba.Esis_app.home

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.commentaire_adapters
import com.BabiMumba.Esis_app.model.commentaire_model
import com.BabiMumba.Esis_app.model.commentaire_poste_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
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
import java.text.SimpleDateFormat
import java.util.*

class DetailleActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var mon_nom: String = ""
    var photo_profil: String = ""
    private var tlc_s: Int? = null

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null


    lateinit var adpter: commentaire_adapters
    private var mLayoutManager: LinearLayoutManager? = null

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
        read_name()
        load_data()
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        val nom_prof = intent.getStringExtra("nom_prof")
        val syllabus = intent.getStringExtra("syllabus")
        val user = intent.getStringExtra("user")
        val id_uses = intent.getStringExtra("id_users")
        val date = intent.getStringExtra("date")
        var promo = intent.getStringExtra("promo").toString()
        val cles = intent.getStringExtra("cle")
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
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val admin_state = sharedPreferences.getString("administrateur",null)


        if ((id_uses.toString() == id_last)|| (admin_state == "oui")) {
            dele_pst.visibility = View.VISIBLE
        } else {
            dele_pst.visibility = View.GONE
        }

        mLayoutManager = LinearLayoutManager(this@DetailleActivity)
        //mLayoutManager!!.reverseLayout = true
        //mLayoutManager!!.stackFromEnd = true
        comment_recyclerview.layoutManager = mLayoutManager


        if (promo != "L1" && promo != "L2") {
            promo = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")
        val options = FirebaseRecyclerOptions.Builder<commentaire_model>()
            .setQuery(
                ref.child(promo).child(cles.toString()).child("comment_syl"),
                commentaire_model::class.java
            )
            .build()
        adpter = commentaire_adapters(options)
        comment_recyclerview.adapter = adpter


    }

    fun load_data(){
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val tlc = sharedPreferences.getInt("point",0)
        tlc_s = tlc
    }
    override fun onStart() {
        super.onStart()
        adpter.startListening()
        comment_recyclerview!!.recycledViewPool.clear()
        adpter.notifyDataSetChanged()
    }
    override fun onStop() {
        super.onStop()
        adpter.stopListening()
    }

    fun setListener() {
        
        dele_pst.setOnClickListener {
            val pop = PopupMenu(this@DetailleActivity, dele_pst)
            pop.menuInflater.inflate(R.menu.popup_menu, pop.menu)
            pop.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (item.itemId == R.id.delete_id) {
                        DeletePoste()
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
            Toast.makeText(this, "ajouter", Toast.LENGTH_SHORT).show()

            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        telecharger()
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
            val lien = intent.getStringExtra("lien_book")
            val intent = Intent(this, LectureActivity::class.java)
            intent.putExtra("nom", name_syllabus.text.toString())
            intent.putExtra("lien_book", lien)
            add_view()
            startActivity(intent)
        }
        check_teste()
    }

    fun check_teste() {
        InputComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                send_comment.isVisible = InputComment.text.toString().trim().isNotEmpty()
                send_comment.setOnClickListener {
                    check_post()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
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
            Environment.DIRECTORY_DOWNLOADS+"/syllabus esis/", "$nom.pdf"
        )
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        add_downloas()
        Toast.makeText(this, "lancement du telechargement", Toast.LENGTH_SHORT).show()
    }

    fun ajouter_data() {
        val cal = Calendar.getInstance()
        val sdf1 = SimpleDateFormat("HH:mm dd/M/yyyy")
        val strDate = sdf1.format(cal.time)
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        val msg = InputComment.text.toString()
        /*
        val hashMap = HashMap<String, Any>()
        hashMap["commentaire"] = InputComment.text.toString()
        hashMap["nom"] = mon_nom
        hashMap["date"] = strDate.toString()
        hashMap["profil"] = photo_profil

         */

        val donnee = commentaire_model(mon_nom, strDate.toString(),msg,photo_profil,"","","","")

        if (pm != "L1" && pm != "L2") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")
        ref.child(pm.toString()).child(cle.toString()).child("comment_syl").child(ref.push().key!!)
            .setValue(donnee)
            .addOnSuccessListener {
                InputComment.setText("")
                add_comment()
                Toast.makeText(this, "commenter", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    fun DeletePosteStorage() {
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
    fun DeletePoste() {
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        if (pm != "L1" && pm != "L2") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")

        ref.child(pm.toString()).child(cle.toString()).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    DeletePosteStorage()
                    Toast.makeText(this, "publication supprimenr", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun read_name() {
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs").document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it != null) {
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post-nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    mon_nom = "$pren $postn"
                    photo_profil = imgetxt.toString()

                } else {
                    Log.d(ContentValues.TAG, "no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
    fun add_downloas() {
        var promo = intent.getStringExtra("promo")
        val cle = intent.getStringExtra("cle")

        if (promo != "L1" && promo != "L2") {
            promo = "Tous"
        }
        val increment: MutableMap<String, Any> = HashMap()
        increment["download"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo.toString()).child((cle.toString()))
            .updateChildren(increment)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@DetailleActivity,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun add_view() {
        var promo = intent.getStringExtra("promo")
        val cle = intent.getStringExtra("cle")

        if (promo != "L1" && promo != "L2") {
            promo = "Tous"
        }
        val increment: MutableMap<String, Any> = HashMap()
        increment["like"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo.toString()).child((cle.toString()))
            .updateChildren(increment)
            .addOnCompleteListener { 
                if (it.isSuccessful){
                    
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun add_comment() {
        var promo = intent.getStringExtra("promo").toString()
        val cle = intent.getStringExtra("cle").toString()
        val increment: MutableMap<String, Any> = HashMap()
        if (promo != "L1" && promo != "L2") {
            promo = "Tous"
        }

        increment["comment"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo).child((cle))
            .updateChildren(increment)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@DetailleActivity,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun check_post() {
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        if (pm != "L1" && pm != "L2") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus").child(pm.toString())
            .child(cle.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    ajouter_data()
                    InputComment.setText("")
                } else {
                    Toast.makeText(
                        this@DetailleActivity,
                        "commentaire non permis",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) //Don't ignore potential errors!
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)

    }

}