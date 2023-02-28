package com.BabiMumba.Esis_app.chat

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
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
            val mediaPlayer = MediaPlayer()
            val afd: AssetFileDescriptor
            try {
                afd = assets.openFd("song.mp3")
                mediaPlayer.setDataSource(afd.fileDescriptor)
                mediaPlayer.prepare()
            }catch (e:Exception){
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed({
                mediaPlayer.start()
                mssg3.visibility = View.VISIBLE
                Handler().postDelayed({
                    mediaPlayer.start()
                    mssg3.visibility = View.VISIBLE

                },3000)
            },3000)


        }
       
        
    }
}