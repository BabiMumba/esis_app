package com.Esisalama.babim.home

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.Esisalama.babim.R
import com.Esisalama.babim.adapters.syllabusAdaptersNew
import com.Esisalama.babim.model.newsyllabus_model
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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
                document?.let {
                    edt_recherche.setHint("Rechercher syllabus($nb)")
                }
                val nom_syllabus = document.getString("nom_syllabus").toString()
                val nom_user = document.getString("nom_user").toString()
                val date_push = document.getString("date_heure").toString()
                val promotion = document.getString("nom_promotion").toString()
                val commnent = document.get("comment").toString()
                val like = document.get("like").toString()
                val download = document.get("download").toString()
                val lien_image = document.getString("lien_profil").toString()
                val pochette = document.getString("pochette").toString()
                val nom_prof = document.getString("nom_prof").toString()
                val descrip = document.getString("description").toString()
                val lien_livre = document.getString("lien_du_livre").toString()
                val id_book = document.getString("id_book").toString()
                val id_user = document.getString("id_user").toString()
                val id_storage = document.getString("lien_pdf").toString()
                val mail_user = document.getString("mail_users").toString()
                val token_user = document.getString("token_users").toString()
                val admin_assistant = document.getString("admin_assistant").toString()
                val adminstrateur = document.getString("administrateur").toString()
                books.add(newsyllabus_model(nom_syllabus,admin_assistant,adminstrateur,pochette,mail_user,id_user,id_storage,token_user,promotion,descrip,nom_prof,lien_livre,nom_user,date_push,lien_image,"","","","",id_book,like.toInt(),download.toInt(),commnent.toInt()))
            }
            syllabusAdaptersNew.items = books

        }

/*

        db.collection("syllabus")
            //.whereEqualTo("nom_promotion","L3_DESIGN")
            .get()

            .addOnSuccessListener { result->
               val nb=  result.count()

                for(document in result){
                   document?.let {
                       edt_recherche.setHint("Rechercher syllabus($nb)")
                   }
                    val nom_syllabus = document.getString("nom_syllabus").toString()
                    val nom_user = document.getString("nom_user").toString()
                    val date_push = document.getString("date_heure").toString()
                    val promotion = document.getString("nom_promotion").toString()
                    val commnent = document.get("comment").toString()
                    val like = document.get("like").toString()
                    val download = document.get("download").toString()
                    val lien_image = document.getString("lien_profil").toString()
                    val pochette = document.getString("pochette").toString()
                    val nom_prof = document.getString("nom_prof").toString()
                    val descrip = document.getString("description").toString()
                    val lien_livre = document.getString("lien_du_livre").toString()
                    val id_book = document.getString("id_book").toString()
                    val id_user = document.getString("id_user").toString()
                    val id_storage = document.getString("lien_pdf").toString()
                    val mail_user = document.getString("mail_users").toString()
                    val token_user = document.getString("token_users").toString()
                    val admin_assistant = document.getString("admin_assistant").toString()
                    val adminstrateur = document.getString("administrateur").toString()
                    books.add(newsyllabus_model(nom_syllabus,admin_assistant,adminstrateur,pochette,mail_user,id_user,id_storage,token_user,promotion,descrip,nom_prof,lien_livre,nom_user,date_push,lien_image,"","","","",id_book,like.toInt(),download.toInt(),commnent.toInt()))
                }
                syllabusAdaptersNew.items = books
            }

            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it.message}", Toast.LENGTH_SHORT).show()
            }
*/

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