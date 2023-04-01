package com.BabiMumba.Esis_app.home

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.adapters.save_profil_adapters
import com.BabiMumba.Esis_app.model.save_profil_syllabus
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_infos_syllabus.*
import kotlinx.android.synthetic.main.content_user_syllabus.*

class InfosSyllabusActivity : AppCompatActivity() {

    var mon_nom:String = ""
    var lien_image:String = ""
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var currentuser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos_syllabus)
        //initialisation
        auth = Firebase.auth
        db = Firebase.firestore
        currentuser = auth.currentUser

        val ad_mail = intent.getStringExtra("mail").toString()

        Toast.makeText(this, ad_mail, Toast.LENGTH_SHORT).show()
        read_name()
        val saveProfilAdapters = save_profil_adapters()
        recycler_syllabus.apply {
            linearLayoutManager = LinearLayoutManager(this@InfosSyllabusActivity)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.onSaveInstanceState()
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = saveProfilAdapters
        }
        val book_save = mutableListOf<save_profil_syllabus>()
        db.collection("syllabus")
            .get()
            .addOnSuccessListener {result ->
                for (document in result){
                    val nom_syl = document.getString("nom_syllabus").toString()
                    val promotion = document.getString("nom_promotion").toString()
                    book_save.add(save_profil_syllabus("",nom_syl,"",promotion,"","",""))
                }
                saveProfilAdapters.items = book_save

            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur:${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    fun read_name(){
        val ad_mail = intent.getStringExtra("mail").toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(Constant.Etudiant).document(ad_mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post_nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    mon_nom = "$pren $postn"
                    lien_image = imgetxt.toString()
                    myname.text = mon_nom
                    val circularProgressDrawable = CircularProgressDrawable(this)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    Glide
                        .with(this)
                        .load(imgetxt)
                        // .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable)
                        .into(profil_me)
                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
}