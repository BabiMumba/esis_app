/*
babi mumba
modifier: 07/02/2023
 */
package com.BabiMumba.Esis_app.home

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.adapters.poste_users_adapters
import com.BabiMumba.Esis_app.chat.ChatActivity
import com.BabiMumba.Esis_app.model.poste_users_model
import com.BabiMumba.Esis_app.users.ImageView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_infos.*
import kotlinx.android.synthetic.main.content_user_poste.*

class InfosActivity : AppCompatActivity() {
    var mon_nom:String = ""
    var lien_image:String = ""
    lateinit var adpter: poste_users_adapters
    private var mLayoutManager: LinearLayoutManager? = null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var collection_name:String

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos)

        val adm = intent.getStringExtra("admin")
        collection_name = if (adm == "oui"){
           Constant.Admin
        }else{
            Constant.Etudiant
        }
        /// initialisation d'admob
        MobileAds.initialize(this){
            Log.d(TAG,"initialisation complet")
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

        firebaseAuth = FirebaseAuth.getInstance()
        val ad_mail = intent.getStringExtra("mail").toString()
        read_name()
        var mail2 = ad_mail.replaceAfter("@"," ")
        if (mail2.contains(".")){
            mail2 =  mail2.replace(".","")
        }
        mLayoutManager = GridLayoutManager(this@InfosActivity,3)
        recycler_users.layoutManager = mLayoutManager
        val ref = FirebaseDatabase.getInstance().getReference("poste_save")
        val options = FirebaseRecyclerOptions.Builder<poste_users_model>()
            .setQuery(
                ref.child(mail2),
                poste_users_model::class.java
            )
            .build()
        adpter = poste_users_adapters(options)
        recycler_users.adapter = adpter
        mLayoutManager!!.reverseLayout = true

        message_btn.setOnClickListener {
            startActivity(Intent(this,ChatActivity::class.java)
                .putExtra("nom",mon_nom)
                .putExtra("lien_image",lien_image)
            )
        }
        imgView_proPic.setOnClickListener {
            startActivity(Intent(this,ImageView::class.java)
                .putExtra("lien_image",lien_image)
            )
        }
    }
    fun read_name(){

        val ad_mail = intent.getStringExtra("mail").toString()

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection_name).document(ad_mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post_nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    val mail = it.data?.getValue("mail")
                    val promotion = it.data?.getValue("promotion")
                    item_text_4.text = mail.toString()
                    item_text_2.text = "promotion: $promotion"
                    mon_nom = "$pren $postn"
                    lien_image = imgetxt.toString()
                    item_text_1.text = mon_nom
                    val circularProgressDrawable = CircularProgressDrawable(this)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    Glide
                        .with(applicationContext)
                        .load(imgetxt)
                        // .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable)
                        .into(imgView_proPic)
                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onStart() {
        super.onStart()
        adpter.startListening()
        recycler_users.recycledViewPool.clear()
        adpter.notifyDataSetChanged()
    }
    override fun onStop() {
        super.onStop()
        adpter.stopListening()
    }

}