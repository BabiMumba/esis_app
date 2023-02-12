package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R

class DetailleNews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaille_news)
        val titre = intent.getStringExtra("titre")
        val message = intent.getStringExtra("message")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("image")


    }
}