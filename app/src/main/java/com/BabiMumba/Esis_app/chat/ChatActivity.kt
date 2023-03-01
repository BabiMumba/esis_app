package com.BabiMumba.Esis_app.chat

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.BabiMumba.Esis_app.R
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_syllabus.*


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
            val mediaPlayer = MediaPlayer.create(this, R.raw.song)
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
        check_teste()

        mssg4.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=243975937553")
            startActivity(i)
        }
       
        
    }
    fun check_teste() {
        InputComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                item_send.isVisible = item_input.text.toString().trim().isNotEmpty()
                item_send.setOnClickListener {
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

                            },3000)

                        },4000)
                    },1000)

                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}