package com.BabiMumba.Esis_app.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.BabiMumba.Esis_app.Authentification.LoginActivity
import com.BabiMumba.Esis_app.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        Handler().postDelayed(
            {
           chek_users()

        },3000)

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