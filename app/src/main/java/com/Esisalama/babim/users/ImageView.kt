package com.Esisalama.babim.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        val lien_image = intent.getStringExtra("lien_image")

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(applicationContext)
            .load(lien_image)
            // .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(image_viewr)
    }
}