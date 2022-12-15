package com.BabiMumba.Esis_app.users

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profil_user.*

class ProfilUser : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_user)

        firebaseAuth = FirebaseAuth.getInstance()

        readData()
        seTlistener()

    }

    fun seTlistener(){
        edit_profil.setOnClickListener {
            val intent = Intent(this,UpdateProfilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readData(){
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs")
            .document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val name = document.data?.getValue("nom").toString()
                    val postname = document.data?.getValue("post-nom").toString()
                    val num = document.data?.getValue("Numero de telephone").toString()
                    val prenoms = document.data?.getValue("prenom").toString()
                    val mailTo = document.data?.getValue("mail").toString()
                    val promotion = document.data?.getValue("promotion").toString()

                    promot.text = promotion
                    u_mail.text = mailTo
                    u_nume.text = num
                    ui_post_name.text = postname
                    prenom_ui.text = prenoms
                    ui_name.text = name
                    val imgetxt = document.data?.getValue("profil")

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


                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                }else{
                    Log.d(ContentValues.TAG, "document inconnue")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onResume() {
        readData()
        super.onResume()
    }
}