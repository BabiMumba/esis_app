package com.BabiMumba.Esis_app.admin.adpters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.model.modeluser
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import de.hdodenhof.circleimageview.CircleImageView

class useradptr(options: FirebaseRecyclerOptions<modeluser>):FirebaseRecyclerAdapter<modeluser,useradptr.viewholder>(options) {


    inner  class  viewholder(item: View):RecyclerView.ViewHolder(item){

        var image:CircleImageView
        var name:TextView
        var mail:TextView
        init {
            image = item.findViewById(R.id.image_profil_rod)
            name = item.findViewById(R.id.name_user)
            mail = item.findViewById(R.id.mail_user)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): useradptr.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: useradptr.viewholder, position: Int, model: modeluser) {

    }
}