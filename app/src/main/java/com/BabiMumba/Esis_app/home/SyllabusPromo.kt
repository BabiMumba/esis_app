package com.BabiMumba.Esis_app.home

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.adapters.syllabus_adapters
import com.BabiMumba.Esis_app.model.Syllabus_model
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.ads.*
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_syllabus.*

class SyllabusPromo : AppCompatActivity() {

    lateinit var myadaptes_syllabus: syllabus_adapters
    lateinit var linearLayoutManager: LinearLayoutManager

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus)

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
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true
        val pm = intent.getStringExtra("promotion").toString()

        val ref = FirebaseFirestore.getInstance().collection("syllabus")
        recp.layoutManager = linearLayoutManager
        val options = FirestoreRecyclerOptions.Builder<Syllabus_model>()
            .setQuery(
                ref,
                Syllabus_model::class.java
            )
            .build()
        myadaptes_syllabus = syllabus_adapters(options)
        recp.adapter = myadaptes_syllabus
        myadaptes_syllabus.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        myadaptes_syllabus.startListening()

    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
    override fun onStart() {
        recycler_promo.recycledViewPool.clear()
        myadaptes_syllabus.notifyDataSetChanged()
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        myadaptes_syllabus.stopListening()
    }
}