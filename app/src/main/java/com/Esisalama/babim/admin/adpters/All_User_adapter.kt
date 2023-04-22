package com.Esisalama.babim.admin.adpters


import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.Esisalama.babim.admin.model.modeluser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.hdodenhof.circleimageview.CircleImageView

class All_User_adapter : RecyclerView.Adapter<All_User_adapter.viewholder>()
     {

    var items:MutableList<modeluser> = mutableListOf()
        set(value){
            field = value
            search_users = value
            notifyDataSetChanged()
        }


    inner  class  viewholder(item: View):RecyclerView.ViewHolder(item){

        var image:CircleImageView = item.findViewById(R.id.image_profil_rod)
        var name:TextView =item.findViewById(R.id.name_user)
        var mail:TextView =item.findViewById(R.id.mail_user)
        var admin:TextView = item.findViewById(R.id.admin_v)
        var rela_id:RelativeLayout =item.findViewById(R.id.rela_id)

        fun bind(user :modeluser) {
            name.text = user.prenom
            mail.text =user.mail

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(itemView.context)
                .load(user.profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): All_User_adapter.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }
    override fun getItemCount()= search_users.size
    private var search_users:MutableList<modeluser> = mutableListOf()

    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()){
                    search_users = items
                }else{
                    val resultlist = items.filter {
                        it.prenom.lowercase().contains( charSearch.lowercase())
                    }
                    search_users = resultlist as MutableList<modeluser>
                }
                val filterResults = FilterResults()
                filterResults.values = search_users
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, resulta: FilterResults?) {
                search_users = resulta?.values as MutableList<modeluser>
                notifyDataSetChanged()
            }

        }
    }

         override fun onBindViewHolder(holder: viewholder, position: Int) {
             val user = search_users[position]
             holder.bind(user)
         }


     }