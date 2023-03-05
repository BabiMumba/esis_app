package com.BabiMumba.Esis_app.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.DetailleNews
import com.BabiMumba.Esis_app.model.news_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class NewsAdapters(options: FirestoreRecyclerOptions<news_model>):
    FirestoreRecyclerAdapter<news_model, NewsAdapters.viewholder>(options){

    inner class  viewholder(item:View):RecyclerView.ViewHolder(item){
        var image: ImageView
        var titre: TextView
        var message: TextView
        var date: TextView
        init {
            image = item.findViewById(R.id.img_news)
            titre = item.findViewById(R.id.titre_news)
            message = item.findViewById(R.id.message_news)
            date = item.findViewById(R.id.date_news)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapters.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_news,parent,false)
        return viewholder(v)

    }
    override fun onBindViewHolder(holder: NewsAdapters.viewholder, position: Int, model: news_model) {
        holder.titre.text = model.titre
        holder.message.text = model.message
        holder.date.text = model.date
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailleNews::class.java)
            intent.putExtra("date" ,model.date)
            intent.putExtra("image" ,model.image)
            intent.putExtra("titre" ,model.titre)
            intent.putExtra("message" ,model.message)
            intent.putExtra("auteur" ,model.autor)
            intent.putExtra("id_news" ,model.id_doc)
            intent.putExtra("promot" ,model.promot)
            intent.putExtra("mail" ,model.mail)
            holder.itemView.context.startActivity(intent)

        }
        val circularProgressDrawable = CircularProgressDrawable(holder.image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.itemView.context)
            .load(model.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(holder.image)


    }


}