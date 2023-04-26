package com.Esisalama.babim.home

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.show_toast_util
import com.Esisalama.babim.adapters.syllabusAdaptersNew
import com.Esisalama.babim.model.newsyllabus_model
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_syllabus_promo.*

class SyllabusViewer : AppCompatActivity() {

    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var currentuser: FirebaseUser? = null

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus_promo)

        //initialisation
        auth = Firebase.auth
        db = Firebase.firestore
        currentuser = auth.currentUser

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
               // Toast.makeText(this@SyllabusPromo, "impression effectuer", Toast.LENGTH_SHORT).show()
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
        if (isConnectedNetwork(this)){
            //connecter
        }else{
           /* r1.visibility = View.GONE
            txvp.visibility = View.VISIBLE
            non_internet.visibility = View.VISIBLE*/
        }

        val syllabusAdaptersNew = syllabusAdaptersNew()
        recycler_promo.apply {
            linearLayoutManager = LinearLayoutManager(this@SyllabusViewer)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.onSaveInstanceState()
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = syllabusAdaptersNew
        }
        val books = mutableListOf<newsyllabus_model>()
        val query = db.collection("syllabus")
        query.addSnapshotListener{snapshot,exception ->
            if (exception != null){
                return@addSnapshotListener
            }

            for(document in snapshot!!.documents){
                val nb=  snapshot.count()
                if (document != null){
                    edt_recherche.setHint("Rechercher syllabus($nb)")
                    progressBar.visibility = View.GONE

                }else{

                }
             val all_book = document.toObject(newsyllabus_model::class.java)
                if (all_book != null) {
                    books.add(all_book)
                }
            }
            syllabusAdaptersNew.items = books

        }
        edt_recherche.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //syllabusAdaptersNew.filter.filter(s.toString())
                syllabusAdaptersNew.getFilter().filter(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

}