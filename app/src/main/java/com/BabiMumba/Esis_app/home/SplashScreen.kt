package com.BabiMumba.Esis_app.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.Authentification.GoogleCountActivity
import com.BabiMumba.Esis_app.Authentification.LoginActivity
import com.BabiMumba.Esis_app.Authentification.RegisterActivity
import com.BabiMumba.Esis_app.Authentification.SigninActivity
import com.BabiMumba.Esis_app.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    var lnb: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        Handler().postDelayed(
            {
                val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
                val name = sharedPreferences.getInt("count",0)
                lnb = name+1
                val editor = sharedPreferences.edit()
                editor.apply() {
                    putInt("count", lnb!!)
                   // Toast.makeText(this@SplashScreen, "sa fait $lnb", Toast.LENGTH_SHORT).show()
                }.apply()
                slogan.visibility = View.VISIBLE
                Handler().postDelayed({
                  chek_users()
                },2000)

        },1000)

    }
    fun chek_users(){
        if (firebaseAuth.currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            startActivity(Intent(this,GoogleCountActivity::class.java))
        }
    }
}