package com.Esisalama.babim.adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.Esisalama.babim.home.DetailleNews
import com.Esisalama.babim.model.commentaire_poste_model
import com.Esisalama.babim.users.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView


class commentaire_poste_adapters (options: FirestoreRecyclerOptions<commentaire_poste_model>):FirestoreRecyclerAdapter<commentaire_poste_model, commentaire_poste_adapters.viewholder>(options){


    var progressBar: ProgressBar? = null

    inner class viewholder(itemview:View):RecyclerView.ViewHolder(itemview){
        var nom:TextView
        var commentaire:TextView
        var date:TextView
        var image:CircleImageView
        init {
            image = itemview.findViewById(R.id.user_image_profil)
            nom = itemview.findViewById(R.id.name_user)
            commentaire = itemview.findViewById(R.id.textcomment)
            date = itemview.findViewById(R.id.date_comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.item_comment_poste,parent,false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int, model: commentaire_poste_model) {
        holder.commentaire.text = model.commentaire
        holder.nom.text = model.nom
        holder.date.text = model.date
        val circularProgressDrawable = CircularProgressDrawable(holder.nom.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.image.context)
            .load(model.profil)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(holder.image)

        holder.image.setOnClickListener {
        /*    startActivity(
                Intent(this, ImageView::class.java)
                .putExtra("lien_image",image)
            )*/
            val intent = Intent(holder.itemView.context, ImageView::class.java)
                .putExtra("lien_image",model.profil)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun onDataChanged() {
        super.onDataChanged()
        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }
    }
}