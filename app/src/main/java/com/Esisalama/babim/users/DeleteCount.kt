package com.Esisalama.babim.users

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.home.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_delete_count.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DeleteCount : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_count)
        auth = FirebaseAuth.getInstance()
        clik_method()

    }
    fun clik_method(){
        btn_verif.setOnClickListener {
            val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
            val password = sharedPreferences.getString("mot de passe",null)
            if (txt_passwor.text.toString() != password){
                Toast.makeText(this, "mot de passe incorrecte", Toast.LENGTH_SHORT).show()
            }else if (txt_passwor.text.toString()==password){

                txt_message.visibility = View.VISIBLE

                if (txt_message.text.toString().trim().isNotEmpty()){
                    save_person()
                }else{
                    txt_message.error = "champ obligatoire"
                }
            }
        }

    }

    private fun save_person() {
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val promo = sharedPreferences.getString("promotion",null)
        val pstom = sharedPreferences.getString("post_nom",null)
        val prenom = sharedPreferences.getString("prenom",null)
        val profil_users = sharedPreferences.getString("lien profil",null)
        loading(true)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
        val date_dins = sdf.format(Date()).toString()
        val data:MutableMap<String,Any> = HashMap()
        data["date"] = date_dins
        data["promotion"] = "$promo"
        data["nom"] = "$prenom $pstom"
        data["profil"] = "$profil_users"
        data["Raison"] = txt_message.text.toString()
        val db = FirebaseFirestore.getInstance()
        db.collection("Deconnection")
            .add(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loading(false)
                    auth.signOut()
                   val intent = Intent(this,SplashScreen::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    loading(false)
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun loading(isLoading: Boolean){
        if (isLoading){
            btn_verif.visibility = View.GONE
            progress_bars.visibility = View.VISIBLE
        }else{
            btn_verif.visibility = View.VISIBLE
            progress_bars.visibility = View.GONE
        }
    }


}