package com.BabiMumba.Esis_app.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.model.Syllabus_model
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class syllabusAdaptersNew:RecyclerView.Adapter<syllabusAdaptersNew.ViewHolder>() {

    var items:MutableList<Syllabus_model> = mutableListOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_syllabus,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]
        holder.bind(book)
    }

    override fun getItemCount() = items.size
    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val syllabus_icone:ImageView = itemView.findViewById(R.id.syllabus_icone)
        val layout_dowload:ImageView = itemView.findViewById(R.id.layout_tlc)
        val description:TextView = itemView.findViewById(R.id.description)
        val image_user:CircleImageView = itemView.findViewById(R.id.profil_user)
        val like_btn:ImageView = itemView.findViewById(R.id.like_btn)
        val container:RelativeLayout = itemView.findViewById(R.id.container_syllabuys)
        val date:TextView = itemView.findViewById(R.id.date_de_publ)
        val  nom_syllabus:TextView = itemView.findViewById(R.id.name_syllabus)
        val nom_user:TextView = itemView.findViewById(R.id.name_user)
        val nom_prof:TextView = itemView.findViewById(R.id.prof_syllabus)
        val nb_comment:TextView = itemView.findViewById(R.id.tv_comment)
        val nb_download:TextView= itemView.findViewById(R.id.tv_download)
        val like_text:TextView = itemView.findViewById(R.id.read_view)
        val promotion:TextView = itemView.findViewById(R.id.nom_promo)
        val admin_i:TextView = itemView.findViewById(R.id.admin_ic)
        fun bind(book: Syllabus_model){
            nom_user.text = book.nom_user
            date.text = book.date_heure
            promotion.text = book.nom_promotion
            nom_syllabus.text = book.nom_syllabu
            nb_comment.text = book.comment.toString()
        }


    }

}