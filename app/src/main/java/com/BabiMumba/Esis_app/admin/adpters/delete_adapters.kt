package com.BabiMumba.Esis_app.admin.adpters


import android.app.Dialog
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.model.model_logout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class delete_adapters (options: FirestoreRecyclerOptions<model_logout>):FirestoreRecyclerAdapter<model_logout,delete_adapters.viewholder>(options) {

    inner  class  viewholder(item: View):RecyclerView.ViewHolder(item){

        var image:CircleImageView
        var name:TextView
        var date:TextView
        var admin:TextView
        var rela_id:RelativeLayout

        init {
            image = item.findViewById(R.id.image_profil_rod)
            name = item.findViewById(R.id.name_user)
            date = item.findViewById(R.id.mail_user)
            admin = item.findViewById(R.id.admin_v)
            rela_id = item.findViewById(R.id.rela_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): delete_adapters.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: delete_adapters.viewholder, position: Int, model: model_logout) {
        holder.name.text = model.nom
        holder.date .text = model.Date

        //holder.itemView.setBackgroundColor(Color.parseColor("#00000"))
        val circularProgressDrawable = CircularProgressDrawable(holder.image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.itemView.context)
            .load(model.profil)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerInside()
            .placeholder(circularProgressDrawable)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val dialog = Dialog(holder.image.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.inforamtion_user_dialgue)
            dialog.setCancelable(true)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT

            val profil_us = dialog.findViewById<ImageView>(R.id.pro_i)
            val promotion = dialog.findViewById<TextView>(R.id.pm)
            val genre = dialog.findViewById<TextView>(R.id.sx)
            val nom = dialog.findViewById<TextView>(R.id.nm)

            nom.text = model.nom
            promotion.text = model.promotion
            genre.text = model.Date
            Glide
                .with(holder.itemView.context)
                .load(model.profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.apply(RequestOptions.overrideOf(300,600))
                .centerInside()
                .placeholder(circularProgressDrawable)
                .into(profil_us)

            dialog.show()
            dialog.window!!.attributes = lp
        }
    }

}