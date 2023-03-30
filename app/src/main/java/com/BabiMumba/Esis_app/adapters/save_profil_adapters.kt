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
import java.net.MalformedURLException
import java.net.URL

class save_profil_adapters :RecyclerView.Adapter<save_profil_adapters.ViewHolder>(){


    var items:MutableList<save_profil_syllabus> = mutableListOf()
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
        var download:ImageView = itemView.findViewById(R.id.dowload_btn)
        var name:TextView = itemview.findViewById(R.id.n_syllabus)
        var pm:TextView = itemview.findViewById(R.id.promot)
        fun bind(book: save_profil_syllabus){
            name.text = book.nom_livre
            pm.text = book.promotion_

        }


    }


    private var search_book:MutableList<save_profil_syllabus> = mutableListOf()

    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()){
                    search_book = items
                }else{
                    val resultlist = items.filter {
                        it.nom_livre.lowercase().contains( charSearch.lowercase())
                    }
                    search_book = resultlist as MutableList<save_profil_syllabus>
                }
                val filterResults = FilterResults()
                filterResults.values = search_book
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, resulta: FilterResults?) {
                search_book = resulta?.values as MutableList<save_profil_syllabus>
                notifyDataSetChanged()
            }

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