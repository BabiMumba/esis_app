package com.BabiMumba.Esis_app.adapters


import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.model.save_profil_syllabus
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.net.MalformedURLException
import java.net.URL

class save_profil_adapters (options:FirebaseRecyclerOptions<save_profil_syllabus>):FirebaseRecyclerAdapter<save_profil_syllabus, save_profil_adapters.viewholder>(options){


    var progressBar: ProgressBar? = null

    inner class viewholder(itemview:View):RecyclerView.ViewHolder(itemview){

        var download:ImageView
        var name:TextView


        init {
            download = itemview.findViewById(R.id.dowload_btn)
            name = itemview.findViewById(R.id.n_syllabus)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.syllabus_card_user,parent,false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int, model: save_profil_syllabus) {

        holder.name.text = model.nom_livre
        holder.download.setOnClickListener {
            try {
                telecharger(holder.download.context,model.nom_livre,model.lien_livre)
            }catch (e:Exception){
                Toast.makeText(holder.download.context, "erreur: $e", Toast.LENGTH_SHORT).show()
            }

        }

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