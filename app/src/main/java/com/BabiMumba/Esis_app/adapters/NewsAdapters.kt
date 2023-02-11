package com.BabiMumba.Esis_app.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.model.news_model
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

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

    }

    override fun onBindViewHolder(holder: NewsAdapters.viewholder, position: Int, model: news_model) {

    }


}