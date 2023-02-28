package com.BabiMumba.Esis_app.home

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.save_profil_adapters
import com.BabiMumba.Esis_app.model.save_profil_syllabus
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_infos_syllabus.*
import kotlinx.android.synthetic.main.content_user_syllabus.*

class InfosSyllabusActivity : AppCompatActivity() {


    var mon_nom:String = ""
    var lien_image:String = ""
    lateinit var adpter: save_profil_adapters
    private var mLayoutManager: LinearLayoutManager? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos_syllabus)

        firebaseAuth = FirebaseAuth.getInstance()

        val ad_mail = intent.getStringExtra("mail").toString()
        Toast.makeText(this, ad_mail, Toast.LENGTH_SHORT).show()

        read_name()
        val mail2 = ad_mail.replaceAfter("@"," ")
        mLayoutManager = LinearLayoutManager(this@InfosSyllabusActivity)

        recycler_syllabus.layoutManager = mLayoutManager
        val ref = FirebaseDatabase.getInstance().getReference("syllabus_poste_save")
        val options = FirebaseRecyclerOptions.Builder<save_profil_syllabus>()
            .setQuery(
                ref.child(mail2),
                save_profil_syllabus::class.java
            )
            .build()
        adpter = save_profil_adapters(options)
        recycler_syllabus.adapter = adpter


    }

    fun read_name(){
        val ad_mail = intent.getStringExtra("mail").toString()
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("Utilisateurs").document(ad_mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post-nom").toString()
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

    override fun onStart() {
        super.onStart()
        adpter.startListening()
        recycler_syllabus.recycledViewPool.clear()
        adpter.notifyDataSetChanged()
    }
    override fun onStop() {
        super.onStop()
        adpter.stopListening()
    }
}