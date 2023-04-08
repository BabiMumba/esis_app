package com.BabiMumba.Esis_app.users

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profil_user.*

class ProfilUser : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_user)
        firebaseAuth = FirebaseAuth.getInstance()
        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val admin = sharedPreferences.getString("administrateur","")
        if (admin == "oui"){
            l4.visibility = View.GONE
            admin_state.visibility = View.VISIBLE

        }

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
        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
            val name = sharedPreferences.getString("nom","")
            val postname = sharedPreferences.getString("post_nom","")
            val num = sharedPreferences.getString("numero de telephone","")
            val prenoms = sharedPreferences.getString("prenom","")
            val mailTo = sharedPreferences.getString("mail","")
            val promotion = sharedPreferences.getString("promotion","")
            val imgetxt = sharedPreferences.getString("lien profil","")
            promot.text = promotion
            u_mail.text = mailTo
            u_nume.text = num
            ui_post_name.text = postname
            prenom_ui.text = prenoms
            ui_name.text = name

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


    }

    override fun onResume() {
        readData()
        super.onResume()
    }
}