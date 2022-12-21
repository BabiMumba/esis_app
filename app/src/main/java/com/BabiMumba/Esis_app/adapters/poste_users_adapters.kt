package com.BabiMumba.Esis_app.adapters


import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.PosteDetaille
import com.BabiMumba.Esis_app.model.poste_users_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.makeramen.roundedimageview.RoundedImageView
import java.net.MalformedURLException
import java.net.URL

class poste_users_adapters (options:FirebaseRecyclerOptions<poste_users_model>):FirebaseRecyclerAdapter<poste_users_model, poste_users_adapters.viewholder>(options){


    var progressBar: ProgressBar? = null

    inner class viewholder(itemview:View):RecyclerView.ViewHolder(itemview){

        var poste_image:RoundedImageView
        var text_sh:TextView

        init {
            text_sh = itemview.findViewById(R.id.text_sh)
            poste_image = itemview.findViewById(R.id.image_poste)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.feed_card_user,parent,false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int, model: poste_users_model) {


        holder.poste_image.visibility = if (model.image_poste == "1") View.GONE else View.VISIBLE

        val circularProgressDrawable = CircularProgressDrawable(holder.poste_image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.itemView.context)
            .load(model.image_poste)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerInside()
            .placeholder(circularProgressDrawable)
            .into(holder.poste_image)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, PosteDetaille::class.java)
            intent.putExtra("cle",model.id_poste)
            intent.putExtra("texte",model.message_texte)
            intent.putExtra("token",model.token_user)
            intent.putExtra("user_id",model.users_id)
            intent.putExtra("image_url",model.image_url)
            intent.putExtra("post_image",model.image_poste)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.itemView.context.startActivity(intent)
        }
        holder.text_sh.text = model.message_texte


    }
    override fun onDataChanged() {
        super.onDataChanged()
        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }
    }
    fun telecharger(context: Context,nom:String,lien:String) {

        var url1: URL? = null
        try {
            url1 = URL(lien)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        val request = DownloadManager.Request(Uri.parse(url1.toString()))
        request.setTitle(nom)
        request.setMimeType("application/pdf")
        request.allowScanningByMediaScanner()
        request.setDescription("Telechargement...")
        request.setVisibleInDownloadsUi(true)
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, "$nom.pdf"
        )
        val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(context, "Telechargement Encours", Toast.LENGTH_SHORT).show()
    }
}