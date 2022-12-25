package com.BabiMumba.Esis_app.admin.adpters


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.model.modeluser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class useradptr (options: FirestoreRecyclerOptions<modeluser>):FirestoreRecyclerAdapter<modeluser,useradptr.viewholder>(options) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): useradptr.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: useradptr.viewholder, position: Int, model: modeluser) {
        val sharedPreferences = holder.image.context.getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val mail_cach = sharedPreferences.getString("mail",null)
        val admin = sharedPreferences.getString("administrateur",null)

        holder.name.text = model.nom
        holder.mail.text = model.mail

        //holder.itemView.setBackgroundColor(Color.parseColor("#00000"))
        holder.itemView.visibility = if (model.mail==mail_cach) View.GONE else  View.VISIBLE
        holder.admin.visibility = if (model.mail == "babimumba243@gmail.com") View.VISIBLE else View.GONE

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

        holder.image.setOnClickListener {
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
            val date = dialog.findViewById<TextView>(R.id.dt)
            recent_cm.setOnClickListener {

                dialog.dismiss()
            }
            old_cm.setOnClickListener {

                dialog.dismiss()

            }

            dialog.show()
            dialog.window!!.attributes = lp
        }
    }

    fun ShortDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_shortby)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val recent_cm = dialog.findViewById<RelativeLayout>(R.id.r1)
        val old_cm = dialog.findViewById<RelativeLayout>(R.id.r2)
        recent_cm.setOnClickListener {

            dialog.dismiss()
        }
        old_cm.setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()
        dialog.window!!.attributes = lp
    }
}