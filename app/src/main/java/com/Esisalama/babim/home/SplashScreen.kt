package com.Esisalama.babim.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.Esisalama.babim.Authentification.GoogleCountActivity
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
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
                val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
                val name = sharedPreferences.getInt("count",0)
                val profil_com = sharedPreferences.getInt("profil_completed",0)
                lnb = name+1
                val editor = sharedPreferences.edit()
                editor.apply() {
                    putInt("count", lnb!!)
                }.apply()
                slogan.visibility = View.VISIBLE
                Handler().postDelayed({
                    if (firebaseAuth.currentUser != null){
                        if (profil_com == 1){
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this, "Completez votre profil", Toast.LENGTH_SHORT).show()
                            firebaseAuth.signOut()
                            startActivity(Intent(this,GoogleCountActivity::class.java))
                        }
                    }else{
                        startActivity(Intent(this,GoogleCountActivity::class.java))
                    }

                },1000)

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