package com.Esisalama.babim.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Esisalama.babim.R
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.home.DetailleActivity
import com.Esisalama.babim.home.InfosSyllabusActivity
import com.Esisalama.babim.home.SyllabusPromo
import com.Esisalama.babim.model.newsyllabus_model
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth


class syllabus_adapters(val syllabus_liste: ArrayList<newsyllabus_model>) :

    RecyclerView.Adapter<syllabus_adapters.myviewholder>() {

    var progressBar: ProgressBar? = null
    private var tlc_s: Int? = null

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
        var admin_i:TextView

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
            like_text = itemView.findViewById(R.id.read_view)
            promotion = itemView.findViewById(R.id.nom_promo)
            admin_i = itemView.findViewById(R.id.admin_ic)

        }
        fun binItems(syllabusModel: newsyllabus_model){
            val sharedPreferences = admin_i.context.getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
            val state_admin = sharedPreferences.getString("premium",null)
            description.text = syllabusModel.description
            nom_user.text = syllabusModel.nom_user
            date.text = syllabusModel.date_heure
            nom_prof.text = syllabusModel.nom_prof
            promotion.text = syllabusModel.nom_promotion
            nom_syllabus.text = syllabusModel.nom_syllabus
            nb_comment.text = syllabusModel.comment.toString()
            nb_download.text = syllabusModel.download.toString()
            like_text.text = syllabusModel.like.toString()
            admin_i.visibility = if (syllabusModel.admin_assistant=="oui") View.VISIBLE else View.GONE

            val circularProgressDrawable = CircularProgressDrawable(nom_user.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide
                .with(image_user.context)
                .load(syllabusModel.lien_profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(image_user)
            Glide
                .with(image_user.context)
                .load(syllabusModel.pochette)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(syllabus_icone)


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailleActivity::class.java)
                intent.putExtra("lien_book",syllabusModel.lien_du_livre)
                intent.putExtra("nom_prof",syllabusModel.nom_prof)
                intent.putExtra("syllabus",syllabusModel.nom_syllabus)
                intent.putExtra("user",syllabusModel.nom_user)
                intent.putExtra("id_users",syllabusModel.id_user)
                intent.putExtra("date",syllabusModel.date_heure)
                intent.putExtra("description",syllabusModel.description)
                intent.putExtra("promo",syllabusModel.nom_promotion)
                intent.putExtra("image_url",syllabusModel.lien_pdf)
                intent.putExtra("couverture",syllabusModel.pochette)
                intent.putExtra("id_book",syllabusModel.id_book)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.startActivity(intent)

            }

            image_user.setOnClickListener{
                val intent = Intent(itemView.context, InfosSyllabusActivity::class.java)
                intent.putExtra("mail",syllabusModel.mail_users)
                itemView.context.startActivity(intent)

            }



        }

    }

    override fun getItemCount(): Int {
        return syllabus_liste.size
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {
        holder.binItems(syllabus_liste[position])
    }

    /*   fun load_data(context: Context){
           val sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
           val tlc = sharedPreferences.getInt("point",0)
           tlc_s = tlc
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
           Toast.makeText(context, "lancement du telechargement...", Toast.LENGTH_SHORT).show()
       }

     *//*  fun increament_dwnlad(context: Context,pm:String,cle:String){

        var promo = pm
        if (promo!="L1" && promo!= "L2"){
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
*/

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