package com.BabiMumba.Esis_app.home

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.poste_users_adapters
import com.BabiMumba.Esis_app.model.poste_users_model
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos)

        firebaseAuth = FirebaseAuth.getInstance()

        val ad_mail = intent.getStringExtra("mail").toString()
        Toast.makeText(this, ad_mail, Toast.LENGTH_SHORT).show()

        read_name()
        setListener()

        val mail2 = ad_mail.replaceAfter("@"," ")
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
                        .into(imgView_proPic)
                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
    fun setListener(){
        add_comment.setOnClickListener {
            startActivity(Intent(this,PublishPost::class.java))
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