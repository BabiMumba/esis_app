package com.BabiMumba.Esis_app.home

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    var mm = ""
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        firebaseAuth = FirebaseAuth.getInstance()
        readData()
        clickmethode()
        checkstate()
    }
    private fun readData(){
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()

        val db = FirebaseFirestore.getInstance()
        db.collection("Utilisateurs").document(mail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null){

                    val name = document.data?.getValue("nom").toString()
                    val num = document.data?.getValue("Numero de telephone").toString()
                    val prenoms = document.data?.getValue("prenom").toString()
                    val mail = document.data?.getValue("mail").toString()
                    val imgetxt = document.data?.getValue("profil")
                    txt2.text = "+243 $num"
                    txt1.text = "$prenoms $name"
                    mm=mail

                    val circularProgressDrawable = CircularProgressDrawable(this)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    Glide
                        .with(this)
                        .load(imgetxt)
                        // .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable)
                        .into(p1)
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                }else{
                    Log.d(ContentValues.TAG, "document inconnue")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "echec ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun clickmethode(){
        md.setOnClickListener {
            startActivity(Intent(this,ProfilUser::class.java))
        }
        rr4.setOnClickListener {
            if (isConnectedNetwork(this)){
                val intent = Intent(this,InfosActivity::class.java)
                intent.putExtra("mail",mm)
                startActivity(intent)
            }else{
                Toast.makeText(this, "probleme de connexion ", Toast.LENGTH_SHORT).show()
            }

        }
        val horaire = s1
        horaire.setOnClickListener {
            if (horaire.isChecked) {
                // When switch checked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
                horaire.isChecked = true
                abonnement("horaire")
            } else {
                // When switch unchecked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                horaire.isChecked = false
                desabonnement("horaire")
            }
        }
        syllabus.setOnClickListener {
            if (syllabus.isChecked) {
                // When switch checked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("syllabus_state", true)
                editor.apply()
                syllabus.isChecked = true
                abonnement("syllabus")
            } else {
                // When switch unchecked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("syllabus_state", false)
                editor.apply()
                syllabus.isChecked = false
                desabonnement("syllabus")
            }

        }
        resultat_id.setOnClickListener {
            if (resultat_id.isChecked) {
                // When switch checked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("resultat_state", true)
                editor.apply()
                resultat_id.isChecked = true
                abonnement("resultat")
            } else {
                // When switch unchecked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("resultat_state", false)
                editor.apply()
                resultat_id.isChecked = false
                desabonnement("resultat")
            }

        }
        forum_notif.setOnClickListener {
            if (forum_notif.isChecked) {
                // When switch checked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("forum_state", true)
                editor.apply()
                forum_notif.isChecked = true
                abonnement("forum")
            } else {
                // When switch unchecked
                val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
                editor.putBoolean("forum_state", false)
                editor.apply()
                forum_notif.isChecked = false
                desabonnement("forum")
            }

        }

    }
    override fun onResume() {
        readData()
        super.onResume()
    }
    override fun onStart() {
        readData()
        super.onStart()
    }
    private fun checkstate(){
        val sharedPreferences = getSharedPreferences("save", MODE_PRIVATE)
        s1.isChecked = sharedPreferences.getBoolean("value", false)
        syllabus.isChecked = sharedPreferences.getBoolean("syllabus_state",false)
        resultat_id.isChecked = sharedPreferences.getBoolean("resultat_state",false)
        forum_notif.isChecked = sharedPreferences.getBoolean("forum_state",false)
    }
    private fun abonnement(nom:String){
        //  FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic(nom).addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "notification $nom active",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    private fun desabonnement(nom:String){
        //  FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(nom).addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "notification $nom desactiver",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

}