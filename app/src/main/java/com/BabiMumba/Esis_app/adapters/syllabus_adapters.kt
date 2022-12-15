package com.BabiMumba.Esis_app.adapters


import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.model.syllabus_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.net.MalformedURLException
import com.BabiMumba.Esis_app.R
import java.net.URL
import java.util.HashMap


class syllabus_adapters(options: FirebaseRecyclerOptions<syllabus_model>) :

    FirebaseRecyclerAdapter<syllabus_model, syllabus_adapters.myviewholder>(options) {

    var progressBar: ProgressBar? = null
    var likereference: DatabaseReference? = null

    override fun onBindViewHolder(holder: myviewholder, position: Int, syllabusModel: syllabus_model) {
        var testclick = false

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userid = firebaseUser!!.uid
        val postkey = getRef(position).key

        holder.description.text = syllabusModel.description
        holder.nom_user.text = syllabusModel.nom_user
        holder.date.text = syllabusModel.date_heure
        holder.nom_prof.text = syllabusModel.nom_prof
        holder.promotion.text = syllabusModel.nom_promotion
        holder.nom_syllabus.text = syllabusModel.nom_syllabu
        holder.nb_comment.text = syllabusModel.comment.toString()
        holder.nb_download.text = syllabusModel.download.toString()
       // holder.image_user.setImageBitmap(getConversionImage(syllabusModel.lien_profil))
        val circularProgressDrawable = CircularProgressDrawable(holder.nom_user.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.image_user.context)
            .load(syllabusModel.lien_profil)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(holder.image_user)
        holder.itemView.setOnClickListener {
           val cle = getRef(position).key
            val intent = Intent(holder.itemView.context,DetailleActivity::class.java)
            intent.putExtra("lien_book",syllabusModel.lien_du_livre)
            intent.putExtra("nom_prof",syllabusModel.nom_prof)
            intent.putExtra("syllabus",syllabusModel.nom_syllabu)
            intent.putExtra("user",syllabusModel.nom_user)
            intent.putExtra("id_users",syllabusModel.id_user)
            intent.putExtra("date",syllabusModel.date_heure)
            intent.putExtra("description",syllabusModel.description)
            intent.putExtra("promo",syllabusModel.nom_promotion)
            intent.putExtra("image_url",syllabusModel.lien_pdf)
            intent.putExtra("cle",cle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.itemView.context.startActivity(intent)

        }

        holder.image_user.setOnClickListener{
            val intent = Intent(holder.itemView.context,InfosSyllabusActivity::class.java)
            intent.putExtra("mail",syllabusModel.mail_users)

            holder.itemView.context.startActivity(intent)

        }
        holder.layout_dowload.setOnClickListener {
            val cle = getRef(position).key.toString()
            try {
                telecharger(holder.layout_dowload.context,syllabusModel.nom_syllabu,syllabusModel.lien_du_livre)
                increament_dwnlad(holder.layout_dowload.context,syllabusModel.nom_promotion,cle)

            }catch (e:Exception){
                Toast.makeText(holder.image_user.context, "$e", Toast.LENGTH_SHORT).show()
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_syllabus, parent, false)
        return myviewholder(view)
    }

    inner class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var syllabus_icone: ImageView
        var like_btn: ImageView
        var container:RelativeLayout
        var layout_dowload:LinearLayout
        var image_user: CircleImageView
        var description: TextView
        var date:TextView
        var nom_syllabus:TextView
        var nom_prof:TextView
        var like_text:TextView
        var nom_user:TextView
        var nb_comment:TextView
        var nb_download:TextView
        var promotion:TextView


        init {
            syllabus_icone = itemView.findViewById(R.id.syllabus_icone)
            layout_dowload = itemView.findViewById(R.id.layout_tlc)
            description = itemView.findViewById(R.id.description)
            image_user = itemView.findViewById(R.id.profil_user)
            like_btn = itemView.findViewById(R.id.like_btn)
            container = itemView.findViewById(R.id.container_syllabuys)
            date = itemView.findViewById(R.id.date_de_publ)
            nom_syllabus = itemView.findViewById(R.id.name_syllabus)
            nom_user = itemView.findViewById(R.id.name_user)
            nom_prof = itemView.findViewById(R.id.prof_syllabus)
            nb_comment = itemView.findViewById(R.id.tv_comment)
            nb_download = itemView.findViewById(R.id.tv_download)
            like_text = itemView.findViewById(R.id.like_text)
            promotion = itemView.findViewById(R.id.nom_promo)

        }

    }

    override fun onDataChanged() {
        super.onDataChanged()
        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }
    }
    fun telecharger(context: Context, nom:String, lien:String) {

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
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(context, "Telechargement Encours", Toast.LENGTH_SHORT).show()
    }
    fun increament_dwnlad(context: Context,pm:String,cle:String){

        var promo = pm
        if (promo!="Preparatoire" && promo!= "L1"){
            promo = "Tous"
        }
        val increment: MutableMap<String, Any> = HashMap()
        increment["download"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo).child((cle))
            .updateChildren(increment)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "plus 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /*
     fun getlikebuttonstatus(postkey: String?, userid: String?) {
            likereference = FirebaseDatabase.getInstance().getReference("likes")
            likereference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(postkey!!).hasChild(userid!!)) {
                        val likecount = snapshot.child(postkey).childrenCount.toInt()
                        like_text.text = "$likecount"
                        like_btn.setImageResource(R.drawable.ic_baseline_favorite_24)
                    } else {
                        val likecount = snapshot.child(postkey).childrenCount.toInt()
                        like_text.text = "$likecount"
                        like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
     */

    /*

     */


}

/*
 holder.getlikebuttonstatus(postkey,userid)
        holder.layout_like.setOnClickListener {
            testclick = true
            likereference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (testclick) {
                        testclick = if (snapshot.child(postkey!!).hasChild(userid)) {
                            likereference!!.child(postkey).child(userid).removeValue()
                            false
                        } else {
                            likereference!!.child(postkey).child(userid).setValue(true)
                            false
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        }
 */