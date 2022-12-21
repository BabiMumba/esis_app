package com.BabiMumba.Esis_app.home

import android.app.Dialog
import android.content.ContentValues
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
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.commentaire_poste_adapters
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.BabiMumba.Esis_app.model.commentaire_poste_model
import com.BabiMumba.Esis_app.model.commnunique_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poste_detaille)
        storageReference = FirebaseStorage.getInstance().reference

        firebaseAuth = FirebaseAuth.getInstance()
        read_name()

        val cle = intent.getStringExtra("cle")
        val txtdescrip = intent.getStringExtra("texte")
        val user_id = intent.getStringExtra("user_id")

        val firebaseUser = firebaseAuth.currentUser
        val id_last = firebaseUser?.uid.toString()

        if (user_id.toString() != id_last){
            delete_btn.visibility = View.GONE
        }else{
            delete_btn.visibility = View.VISIBLE
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

        poste_recyclerview.layoutManager = mLayoutManager
        val ref = FirebaseDatabase.getInstance().getReference("communique")
        val options = FirebaseRecyclerOptions.Builder<commentaire_poste_model>()
            .setQuery(
                ref.child(cle.toString()).child("commente_poste"),
                commentaire_poste_model::class.java
            )
            .build()
        adpter = commentaire_poste_adapters(options)
        poste_recyclerview.adapter = adpter

        getAuterDate()
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
        val cal = Calendar.getInstance()
        val sdf1 = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val strDate = sdf1.format(cal.time)
        val cle = intent.getStringExtra("cle")

        val hashMap = HashMap<String, Any>()
        hashMap["commentaire"] = msg
        hashMap["nom"] = mon_nom
        hashMap["date"] = strDate.toString()
        hashMap["profil"]= photo_profil

        val ref = FirebaseDatabase.getInstance().getReference("communique")
        ref.child(cle.toString()).child("commente_poste").child(ref.push().key!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                add_comment()

                val nb = findViewById<TextView>(R.id.nb_comment)
                val MyScore = Integer.parseInt(nb.text.toString());

                if (MyScore > 1){
                    sendnotif(MyScore)
                }else if (MyScore<1 ){
                    sendnotifsimple()
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    fun read_name(){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs").document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post-nom").toString()
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
        val cle = intent.getStringExtra("cle")
        val increment: MutableMap<String, Any> = HashMap()
        increment["commentaire"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("communique")
            .child((cle.toString()))
            .updateChildren(increment)
            .addOnCompleteListener {
                if (it.isSuccessful){

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
        val cle = intent.getStringExtra("cle")
        val image_poster = intent.getStringExtra("post_image")
        val ref = FirebaseDatabase.getInstance().getReference("communique")
        ref.child(cle.toString()).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    DeletePosteMyprofil()
                    if (image_poster != "1"){
                        DeletePosteStorage()
                    }

                    Toast.makeText(this, "publication supprimenr", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun DeletePosteMyprofil(){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString().replaceAfter("@"," ")
        val cle = intent.getStringExtra("cle")
        val ref = FirebaseDatabase.getInstance().getReference("poste_save")
        ref.child(mail).child(cle.toString()).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    DeletePosteStorage()

                    Toast.makeText(this, "delete to my profil", Toast.LENGTH_SHORT).show()

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
    fun check_post(msg: String){
        val cle = intent.getStringExtra("cle")
        val ref = FirebaseDatabase.getInstance().getReference("communique").child(cle.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@PosteDetaille, "cette pub existe", Toast.LENGTH_SHORT).show()
                    ajouter_data(msg)

                }else{
                    Toast.makeText(this@PosteDetaille, "commentaire non permis", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) //Don't ignore potential errors!
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)



    }

    fun ShortDialog() {
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
                "${mon_nom} et $nb autres personnes ont commenter votre photo",

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
                "${mon_nom} a commenter votre photo",

                )
        }


    }
    fun getAuterDate(){

        val cle = intent.getStringExtra("cle")
        val ref = FirebaseDatabase.getInstance().getReference("communique").child(cle.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val donne: commnunique_model? = dataSnapshot.getValue(commnunique_model::class.java)
                nb_comment.text = donne?.commentaire.toString()
                nb_vue.text = donne?.vue.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) //Don't ignore potential errors!
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)




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