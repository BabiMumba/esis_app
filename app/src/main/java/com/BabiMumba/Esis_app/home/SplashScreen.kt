package com.BabiMumba.Esis_app.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.Authentification.LoginActivity
import com.BabiMumba.Esis_app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        Handler().postDelayed(
            {
                val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
                val name = sharedPreferences.getString("premium",null)

                if (name=="oui"){
                    Toast.makeText(this, "vous etes en mode payant", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "vous etes en mode grauit", Toast.LENGTH_SHORT).show()
                }

                slogan.visibility = View.VISIBLE
                Handler().postDelayed({
                  chek_users()
                },3000)

        },2000)

    }
    fun chek_users(){
        if (firebaseAuth.currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}