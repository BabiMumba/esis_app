package com.Esisalama.babim.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.Esisalama.babim.home.DetailleActivity
import com.Esisalama.babim.home.InfosSyllabusActivity
import com.Esisalama.babim.model.newsyllabus_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.hdodenhof.circleimageview.CircleImageView

class syllabusAdaptersNew:RecyclerView.Adapter<syllabusAdaptersNew.ViewHolder>() {

    var items:MutableList<newsyllabus_model> = mutableListOf()
        set(value){
            field = value
            search_book = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item_syllabus,parent,false)
        return ViewHolder(itemview)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = search_book[position]
        holder.bind(book)
    }

    override fun getItemCount() = search_book.size

    inner class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val syllabus_icone:ImageView = itemView.findViewById(R.id.syllabus_icone)
        val layout_dowload:LinearLayout = itemView.findViewById(R.id.layout_tlc)
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
        fun bind(book: newsyllabus_model){
            nom_user.text = book.nom_user
            description.text = book.description
            date.text = book.date_heure
            promotion.text = book.nom_promotion
            nom_syllabus.text = book.nom_syllabus
            nb_comment.text = book.comment.toString()
            nb_download.text = book.download.toString()
            nom_prof.text = book.nom_prof
            like_text.text = book.like.toString()

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(itemView.context)
                .load(book.lien_profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(image_user)
            Glide
                .with(itemView.context)
                .load(book.pochette)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(syllabus_icone)

            //quand on click sur le syllabus
           itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailleActivity::class.java)
                intent.putExtra("lien_book",book.lien_du_livre)
                intent.putExtra("nom_prof",book.nom_prof)
                intent.putExtra("syllabus",book.nom_syllabus)
                intent.putExtra("user",book.nom_user)
                intent.putExtra("id_users",book.id_user)
                intent.putExtra("date",book.date_heure)
                intent.putExtra("description",book.description)
                intent.putExtra("promo",book.nom_promotion)
                intent.putExtra("image_url",book.lien_pdf)
                intent.putExtra("couverture",book.pochette)
                intent.putExtra("id_book",book.id_book)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.startActivity(intent)
            }
            image_user.setOnClickListener {
                val intent = Intent(itemView.context, InfosSyllabusActivity::class.java)
                intent.putExtra("mail",book.mail_users)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.startActivity(intent)
            }


        }


    }

    private var search_book:MutableList<newsyllabus_model> = mutableListOf()
    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()){
                    search_book = items
                }else{
                    val resultlist = items.filter {
                       it.nom_syllabus.lowercase().contains( charSearch.lowercase())
                    }
                    search_book = resultlist as MutableList<newsyllabus_model>
                }
                val filterResults = FilterResults()
                filterResults.values = search_book
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, resulta: FilterResults?) {
                search_book = resulta?.values as MutableList<newsyllabus_model>
                notifyDataSetChanged()
            }

        }
    }

}