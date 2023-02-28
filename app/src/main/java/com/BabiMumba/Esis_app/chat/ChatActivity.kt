package com.BabiMumba.Esis_app.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}