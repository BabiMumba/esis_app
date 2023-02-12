package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_detaille_news.*
import kotlinx.android.synthetic.main.content_news.*

class DetailleNews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaille_news)
        val titre = intent.getStringExtra("titre")
        val message = intent.getStringExtra("message")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("image")
        val auteur = intent.getStringExtra("auteur")
        val promot = intent.getStringExtra("promot")
        val id_news = intent.getStringExtra("id_news")


        promot_news.text = promot
        title_news.text = titre
        descri.text = message
        date_news.text = date
        autor_news.text = "par $auteur"
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(this)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(news_image_id)


    }
}