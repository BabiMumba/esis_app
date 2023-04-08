package com.BabiMumba.Esis_app.adapters


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.home.InfosActivity
import com.BabiMumba.Esis_app.home.PosteDetaille
import com.BabiMumba.Esis_app.model.post_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.client.Firebase
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import de.hdodenhof.circleimageview.CircleImageView
import java.util.HashMap

class post_adapters (options: FirestoreRecyclerOptions<post_model>):FirestoreRecyclerAdapter<post_model, post_adapters.viewholder>(options){

    var progressBar: ProgressBar? = null
    var likereference: DatabaseReference? = null
    var mon_nom = ""
    var token_id = ""
    lateinit var collection_name:String
    private lateinit var firebaseAuth: FirebaseAuth

    inner class viewholder(itemview:View):RecyclerView.ViewHolder(itemview){
        var nom:TextView
        var like_text:TextView
        var nb_like:TextView
        var nb_vue:TextView
        var comment:TextView
        var message:TextView
        var layout_like:LinearLayout
        var date:TextView
        var image:CircleImageView
        var poste_image:ImageView
        var like_btn:ImageView
        var admin_i:TextView

        init {
            poste_image = itemview.findViewById(R.id.image_poste1)
            layout_like = itemview.findViewById(R.id.layout_like)
            like_btn = itemview.findViewById(R.id.like_btn)
            image = itemview.findViewById(R.id.imgView_proPic)
            nom = itemview.findViewById(R.id.tv_name)
            like_text = itemview.findViewById(R.id.like_text)
            nb_like = itemview.findViewById(R.id.nb_like)
            nb_vue = itemview.findViewById(R.id.nb_vue)
            comment = itemview.findViewById(R.id.nb_comment)
            message = itemview.findViewById(R.id.message)
            date = itemview.findViewById(R.id.date)
            admin_i = itemview.findViewById(R.id.admin_ir)
        }


        //si on clik sur le bouton

        fun getlikebuttonstatus(postkey: String?, userid: String?) {
            likereference = FirebaseDatabase.getInstance().getReference("likes_poste")
            likereference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(postkey!!).hasChild(userid!!)) {
                        val likecount = snapshot.child(postkey).childrenCount.toInt()
                        like_text.text = "$likecount"
                        nb_like.text = "$likecount"
                        like_btn.setImageResource(R.drawable.ic_round_thumb_up_24)
                    } else {
                        val likecount = snapshot.child(postkey).childrenCount.toInt()
                        like_text.text = "$likecount"
                        nb_like.text = "$likecount"
                        like_btn.setImageResource(R.drawable.ic_outline_thumb_up_24)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        val user_id = FirebaseAuth.getInstance().currentUser!!.uid

        fun get_status_btn_like(Id_post:String,userid: String){
            val ref = FirebaseFirestore.getInstance().collection("poste_forum").document(Id_post).collection("like_poste").document(userid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val document = task.result
                        if (document.exists()){
                            like_btn.setImageResource(R.drawable.ic_round_thumb_up_24)
                        }else{

                        }
                    }

                }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.row_feed,parent,false)
        return viewholder(v)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int, model: post_model) {

        val sharedPreferences = holder.admin_i.context.getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val admin = sharedPreferences.getString("administrateur",null)


        holder.admin_i.visibility = if (model.admin_assistant == "oui") View.VISIBLE else View.GONE
       // holder.like_text.text = count.toString()

        collection_name = if (admin == "oui"){
            Constant.Admin
        }else{
            Constant.Etudiant
        }

        read_name(collection_name)
        get_token()
        var testclick = false
        val circularProgressDrawable = CircularProgressDrawable(holder.message.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        holder.message.text = model.message
        holder.nom.text = model.nom
        holder.date.text = model.date
        holder.comment.text = model.nb_comment.toString()
        holder.nb_vue.text = model.vue.toString()

        Glide
            .with(holder.image.context)
            .load(model.profil)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(holder.image)
        holder.image.setOnClickListener{
            val intent = Intent(holder.itemView.context, InfosActivity::class.java)
            intent.putExtra("mail",model.ad_mail)
            intent.putExtra("admin",model.administrateur)
            holder.itemView.context.startActivity(intent)
        }

        holder.poste_image.visibility = if (model.image_poste=="1") View.GONE else View.VISIBLE

        Glide
            .with(holder.poste_image.context)
            .load(model.image_poste)
            //.apply(new RequestOptions().override(600, 200))
            //.diskCacheStrategy(DiskCacheStrategy.ALL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(400,400))
            .placeholder(circularProgressDrawable)
            .into(holder.poste_image)




        /*holder.getlikebuttonstatus(postkey,userid)*/

        /*holder.layout_like.setOnClickListener {
            testclick = true
            likereference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (testclick) {
                        testclick = if (snapshot.child(postkey!!).hasChild(userid)) {
                            likereference!!.child(postkey).child(userid).removeValue()
                            false
                        } else {
                            val mediaPlayer = MediaPlayer.create(holder.image.context, R.raw.like_raw)
                            mediaPlayer.start()
                            likereference!!.child(postkey).child(userid).setValue(true)
                            if (model.token_users != token_id){
                                FcmNotificationsSender.pushNotification(
                                    holder.message.context,
                                    model.token_users,
                                    holder.itemView.context.getString(R.string.app_name),
                                    "${mon_nom} a aimer votre publication",
                                )
                            }
                            false
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }*/

        val user_id = FirebaseAuth.getInstance().currentUser!!.uid
        holder.get_status_btn_like(model.id_poste,user_id)

        holder.layout_like.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val data_comment = HashMap<String, Any>()
            data_comment[user_id] = true
            db.collection("poste_forum").document(model.id_poste).collection("like_poste").document(user_id)
                .set(data_comment)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "vous avez aimer", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "ereur:${it.message}", Toast.LENGTH_SHORT).show()
                }

        }


        holder.itemView.setOnClickListener {
            //incrementer le nombre de vue

            val db = FirebaseFirestore.getInstance()
            db.collection("poste_forum").document(model.id_poste)
                .update("vue",FieldValue.increment(1))
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "incrementer", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "erreur:${it.message}", Toast.LENGTH_SHORT).show()
                }
            val intent = Intent(holder.itemView.context, PosteDetaille::class.java)
            intent.putExtra("post_image",model.image_poste)
            intent.putExtra("image_url",model.image_name_id)
            intent.putExtra("user_id",model.users_id)
            intent.putExtra("id_poste",model.id_poste)
            intent.putExtra("texte",model.message)
            intent.putExtra("token",model.token_users)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.itemView.context.startActivity(intent)



        }
    }
    override fun onDataChanged() {
        super.onDataChanged()
        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }
    }

    fun read_name(collection_name:String){
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection_name).document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post_nom").toString()
                    mon_nom = "$pren $postn"
                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG,"erreur $it")

            }
    }
    fun get_token(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            token_id = token.toString()
        })
    }

}