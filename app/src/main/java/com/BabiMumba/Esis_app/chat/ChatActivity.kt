package com.BabiMumba.Esis_app.chat

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val nom = intent.getStringExtra("nom").toString()
        val profil = intent.getStringExtra("lien_image").toString()
        name_User.text = nom
        back_btn.setOnClickListener{
            onBackPressed()
        }
        val sdf = SimpleDateFormat("HH:mm:ss")
        val heure = sdf.format(Date())
        heure1.text = heure
        text_time.text = heure
        heure2.text = heure
        heure3.text = heure

        check_teste()

        mssg4.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=243975937553")
            startActivity(i)
        }
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(applicationContext)
            .load(profil)
            // .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(image)
        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
        val prenom = sharedPreferences.getString("prenom","")

        item_mic_click_parent.setOnClickListener {
            if (item_send.isVisible){
                message_sent.text = item_input.text.toString()
                item_input.setText("")

                text_content.text = "hey 👋👋 bonjour $prenom\n c'est Babi Mumba"
                fst_mssg.visibility = View.VISIBLE
                val mediaPlayer = MediaPlayer.create(this@ChatActivity, R.raw.song)
                Handler().postDelayed({
                    mediaPlayer.start()
                    mssg1.visibility = View.VISIBLE
                    Handler().postDelayed({
                        mediaPlayer.start()
                        mssg2.visibility = View.VISIBLE
                        Handler().postDelayed({
                            mediaPlayer.start()
                            mssg3.visibility = View.VISIBLE
                            mssg4.visibility = View.VISIBLE

                        },2000)

                    },2000)
                },1000)
            }

        }
        
    }
    fun check_teste() {
        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
        val prenom = sharedPreferences.getString("prenom","")
        item_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                item_mic_icon.isInvisible = item_input.text.toString().trim().isNotEmpty()
                item_send.isVisible = item_input.text.toString().trim().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}