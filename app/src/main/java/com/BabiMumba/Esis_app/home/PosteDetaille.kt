package com.BabiMumba.Esis_app.home

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.adapters.commentaire_poste_adapters
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.BabiMumba.Esis_app.model.commentaire_poste_model
import com.BabiMumba.Esis_app.model.post_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_poste_detaille.*
import kotlinx.android.synthetic.main.content_comment.*
import java.text.SimpleDateFormat
import java.util.*

class PosteDetaille : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    var mon_nom:String = ""
    var photo_profil:String = ""
    var token_id:String = ""

    lateinit var adpter: commentaire_poste_adapters
    private var mLayoutManager: LinearLayoutManager? = null
    lateinit var collection_name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poste_detaille)
        storageReference = FirebaseStorage.getInstance().reference

        firebaseAuth = FirebaseAuth.getInstance()
        read_name()

        //val cle = intent.getStringExtra("cle")

        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)

        collection_name = if (adm == "oui"){
            Constant.Admin
        }else{
            Constant.Etudiant
        }
        val txtdescrip = intent.getStringExtra("texte")
        val user_id = intent.getStringExtra("user_id")

        val firebaseUser = firebaseAuth.currentUser
        val id_last = firebaseUser?.uid.toString()

        if ((user_id.toString() == id_last)||( adm=="oui")){
            delete_btn.visibility = View.VISIBLE
        }else{
            delete_btn.visibility = View.GONE
        }


        descri.text = txtdescrip
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val image_poster = intent.getStringExtra("post_image")
        if (image_poster == "1"){
            poste_image_id.visibility = View.GONE
        }else{
            Glide
                .with(this)
                .load(image_poster)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable)
                .into(poste_image_id)
        }

        setListener()
        get_token()
        mLayoutManager = LinearLayoutManager(this@PosteDetaille)
        val id_poste = intent.getStringExtra("id_poste").toString()

        poste_recyclerview.layoutManager = mLayoutManager
        val ref = FirebaseFirestore.getInstance().collection("poste_forum").document(id_poste).collection("commentaire")
        val options = FirestoreRecyclerOptions.Builder<commentaire_poste_model>()
            .setQuery(
                ref,
                commentaire_poste_model::class.java
            )
            .build()
        adpter = commentaire_poste_adapters(options)
        poste_recyclerview.adapter = adpter

        check_teste()


    }
    override fun onStart() {
        super.onStart()
        adpter.startListening()
        poste_recyclerview!!.recycledViewPool.clear()
        adpter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        adpter.stopListening()
    }

    fun setListener(){
        delete_btn.setOnClickListener {
            val pop = PopupMenu(this@PosteDetaille, delete_btn)
            pop.menuInflater.inflate(R.menu.popup_menu, pop.menu)
            pop.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (item.itemId == R.id.delete_id) {
                        Deletedialogue()
                    }
                    return true
                }
            })
            pop.show()


        }
        shortby.setOnClickListener {
            ShortDialog()
        }
    }
    fun check_teste(){
        InputComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                send_comment.isVisible = InputComment.text.toString().trim().isNotEmpty()
                send_comment.setOnClickListener {
                   check_post(InputComment.text.toString())
                    InputComment.setText("")
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }
    fun ajouter_data(msg:String){
        val firebaseUser = firebaseAuth.currentUser
        val id_user = firebaseUser?.uid.toString()
        val cal = Calendar.getInstance()
        val sdf1 = SimpleDateFormat("HH:mm dd/M/yyyy")
        val strDate = sdf1.format(cal.time)
        val cle = intent.getStringExtra("id_poste")
        val tsp = SimpleDateFormat("yyyy-M-dd")
        val timespam = tsp.format(Date())
        val document = "$timespam-${System.currentTimeMillis()}"
        val data_comment = HashMap<String, Any>()
        data_comment["commentaire"] = msg
        data_comment["nom"] = mon_nom
        data_comment["date"] = strDate.toString()
        data_comment["profil"]= photo_profil
        data_comment["id_reserve"]= ""
        data_comment["id_reserve2"]= ""
        data_comment["id_user"]= id_user
        data_comment["timespam"]= document
        val db = Firebase.firestore

        db.collection("poste_forum").document(cle.toString()).collection("commentaire").document(document)
            .set(data_comment)
            .addOnSuccessListener {
                _increment_data()
                Toast.makeText(this, "commenter", Toast.LENGTH_SHORT).show()
                add_comment()
              /*  val nb = findViewById<TextView>(R.id.nb_comment)
                val MyScore = Integer.parseInt(nb.text.toString());
                if (MyScore > 1){
                    sendnotif(MyScore)
                }else if (MyScore<1 ){
                    sendnotifsimple()
                }*/

            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    fun read_name(){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        var docRef = db.collection(Constant.Etudiant).document(mail)

        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, MODE_PRIVATE)
        val admin = sharedPreferences.getString("administrateur","")
        docRef = if (admin == "oui"){
            db.collection(Constant.Admin).document(mail)
        }else{
            db.collection(Constant.Etudiant).document(mail)
        }
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post_nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    mon_nom = "$pren $postn"
                    photo_profil = imgetxt.toString()

                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
    fun add_comment(){
        val cle = intent.getStringExtra("id_poste")
        val increment: MutableMap<String, Any> = HashMap()
        increment["nb_comment"] = FieldValue.increment(1)
        val db = Firebase.firestore
            db.collection("poste_forum")
            .document((cle.toString()))
            .update(increment)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "commentaire ajouter", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun Deletedialogue() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Suppression de la publication")
        alertDialog.setMessage("voulez-vouz vraiment supprimer cette publication")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"oui"){
                d: DialogInterface, _:Int ->
            DeletePoste()
            Toast.makeText(this, "clique sur oui", Toast.LENGTH_SHORT).show()
            d.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"non"){
                d: DialogInterface, _:Int ->
            d.dismiss()
        }

        alertDialog.show()
    }
    fun DeletePoste(){
        val cle = intent.getStringExtra("id_poste")
        val image_poster = intent.getStringExtra("post_image")
        val ref = FirebaseFirestore.getInstance().collection("poste_forum")
        .document(cle.toString()).delete()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    if (image_poster != "1"){
                        DeletePosteStorage()
                    }
                    Toast.makeText(this, "publication supprimenr", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun DeletePosteStorage(){
        val image_name_id = intent.getStringExtra("image_url")
        val storageRef = storageReference
        val desertRef = storageRef.child(image_name_id.toString())
        desertRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "image supprime", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun _increment_data(){
        val db = FirebaseFirestore.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val increment = hashMapOf<String,Any>(
            "nb_commentaire_count" to FieldValue.increment(1),
        )
        db.collection(collection_name).document(mail)
            .set(increment, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "nombre incrementer", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Add_book","erreur:${it.exception}")
                }
            }
    }
    fun check_post(msg: String){
        val cle = intent.getStringExtra("id_poste")
        FirebaseFirestore.getInstance().collection("poste_forum").document(cle.toString())
            .get()
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                  val document = task.result
                    if (document.exists()){
                        Toast.makeText(this, "document existe", Toast.LENGTH_SHORT).show()
                        ajouter_data(msg)
                    }else{

                    }
                }

            }
    }
    fun ShortDialog()  {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_shortby)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val recent_cm = dialog.findViewById<RelativeLayout>(R.id.r1)
        val old_cm = dialog.findViewById<RelativeLayout>(R.id.r2)
        recent_cm.setOnClickListener {
            mLayoutManager!!.reverseLayout = true
            mLayoutManager!!.stackFromEnd = true
            dialog.dismiss()
        }
        old_cm.setOnClickListener {
            mLayoutManager!!.reverseLayout = false
            mLayoutManager!!.stackFromEnd = false
            dialog.dismiss()

        }

        dialog.show()
        dialog.window!!.attributes = lp
    }
    fun sendnotif(nb:Int){
        val id_token = intent.getStringExtra("token")
        if (id_token!=token_id){
            FcmNotificationsSender.pushNotification(
                this,
                id_token,
                getString(R.string.app_name),
                "${mon_nom} et $nb autres personnes ont commenter votre publication",

                )
        }

    }
    fun sendnotifsimple(){
        val id_token = intent.getStringExtra("token")
        if (id_token!=token_id){
            FcmNotificationsSender.pushNotification(
                this,
                id_token,
                getString(R.string.app_name),
                "${mon_nom} a commenter votre publication",

                )
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