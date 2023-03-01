package com.BabiMumba.Esis_app.admin.adpters


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.model.modeluser
import com.BabiMumba.Esis_app.home.InfosActivity
import com.BabiMumba.Esis_app.home.PosteDetaille
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.hdodenhof.circleimageview.CircleImageView

class admin_adapters (options: FirestoreRecyclerOptions<modeluser>):
    FirestoreRecyclerAdapter<modeluser,admin_adapters.viewholder>(options) {

    inner  class  viewholder(item: View):RecyclerView.ViewHolder(item){

        var image:CircleImageView
        var name:TextView
        var mail:TextView
        var admin:TextView
        var rela_id:RelativeLayout

        init {
            image = item.findViewById(R.id.image_profil_rod)
            name = item.findViewById(R.id.name_user)
            mail = item.findViewById(R.id.mail_user)
            admin = item.findViewById(R.id.admin_v)
            rela_id = item.findViewById(R.id.rela_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): admin_adapters.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: admin_adapters.viewholder, position: Int, model: modeluser) {

        holder.name.text = model.nom
        holder.mail.text = model.mail

        //holder.itemView.setBackgroundColor(Color.parseColor("#00000"))
        //holder.itemView.visibility = if (model.mail==mail_cach) View.GONE else  View.VISIBLE
       // holder.admin.visibility = if (model.admin_assistant == "oui") View.VISIBLE else View.GONE
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

        //boite de dialogue

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, InfosActivity::class.java)
            intent.putExtra("mail",model.mail)
            intent.putExtra("admin",model.administrateur)
            holder.itemView.context.startActivity(intent)

        }

    }

}