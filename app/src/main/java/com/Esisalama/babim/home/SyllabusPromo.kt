package com.Esisalama.babim.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.show_toast_util
import com.Esisalama.babim.adapters.syllabus_adapters
import com.Esisalama.babim.model.newsyllabus_model
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.*
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_syllabus.*

class SyllabusPromo : AppCompatActivity() {

    lateinit var myadaptes_syllabus: syllabus_adapters
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var syllabus:ArrayList<newsyllabus_model>

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus)
        syllabus = arrayListOf()


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
          /*  r1.visibility = View.GONE
            txvp.visibility = View.VISIBLE
            non_internet.visibility = View.VISIBLE*/
        }

        val recp = findViewById<RecyclerView>(R.id.recycler_promo)
        l1.setOnClickListener {
            startActivity(Intent(this,SyllabusViewer::class.java))
        }
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true
        recp.layoutManager = linearLayoutManager
        val pm = intent.getStringExtra("promotion").toString()
        show_toast_util(this,pm)

        //verifier si la promotion est vide ou pas
        //si la promotion est vide alors on affiche tout les syllabus sinon on affiche les syllabus de la promotion sur firestore
        if (pm == ""){
            FirebaseFirestore.getInstance()
                .collection("syllabus")
            //obtenir les données
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty){
                        progressBar.visibility = View.GONE
                        for (document in it){
                            val syllab = document.toObject(newsyllabus_model::class.java)
                            syllabus.add(syllab)
                        }
                    }else{

                        show_toast_util(this,"pas de contenue")
                    }

                    myadaptes_syllabus = syllabus_adapters(syllabus)
                    recp.adapter = myadaptes_syllabus
                }

        }else{
            //afficher les syllabus de la promotion en triant par nom de promotion
            FirebaseFirestore.getInstance()
                .collection("syllabus")
                .whereEqualTo("nom_promotion",pm)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty){
                        progressBar.visibility = View.GONE
                        for (document in it){
                            val syllab = document.toObject(newsyllabus_model::class.java)
                            syllabus.add(syllab)
                        }
                    }else{
                        show_toast_util(this,"pas de contenue")
                    }

                    myadaptes_syllabus = syllabus_adapters(syllabus)
                    recp.adapter = myadaptes_syllabus
                }
            show_toast_util(this,pm)
        }



      /*  ref = if (pm == ""){
            FirebaseFirestore.getInstance()
                .collection("syllabus")

        }else{
            FirebaseFirestore.getInstance()
                .collection("syllabus")
                .whereEqualTo("nom_promotion",pm) as CollectionReference
        }


            ref.get()
                .addOnSuccessListener {
                    if (!it.isEmpty){
                        progressBar.visibility = View.GONE
                        for (document in it){
                            val syllab = document.toObject(newsyllabus_model::class.java)
                            syllabus.add(syllab)
                        }
                    }else{

                        show_toast_util(this,"pas de contenue")
                    }

                    myadaptes_syllabus = syllabus_adapters(syllabus)
                    recp.adapter = myadaptes_syllabus
                }
                .addOnFailureListener {
                    Log.d("syllabus_show","${it.message}")
                }*/


       // myadaptes_syllabus.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}