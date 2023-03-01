package com.BabiMumba.Esis_app.chat

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.BabiMumba.Esis_app.R
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val nom = intent.getStringExtra("nom").toString()
        name_User.text = nom
        back_btn.setOnClickListener{
            onBackPressed()
        }
        item_mic_click_parent.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this, R.raw.like_raw)

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

                    },3000)

                },4000)
            },1000)


        }
        mssg4.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=243975937553")
            startActivity(i)
        }
       
        
    }
}