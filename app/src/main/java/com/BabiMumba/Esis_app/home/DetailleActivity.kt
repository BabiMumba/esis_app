package com.BabiMumba.Esis_app.home

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.commentaire_adapters
import com.BabiMumba.Esis_app.model.commentaire_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_detaille.*
import kotlinx.android.synthetic.main.content_syllabus.*
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class DetailleActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var mon_nom: String = ""
    var photo_profil: String = ""

    lateinit var adpter: commentaire_adapters
    private var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaille)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        read_name()

        val nom_prof = intent.getStringExtra("nom_prof")
        val syllabus = intent.getStringExtra("syllabus")
        val user = intent.getStringExtra("user")
        val id_uses = intent.getStringExtra("id_users")
        val date = intent.getStringExtra("date")
        var promo = intent.getStringExtra("promo").toString()
        val cles = intent.getStringExtra("cle")
        val descrip = intent.getStringExtra("description")
        val cover_ic = intent.getStringExtra("couverture")

        nom_du_prof.text = nom_prof
        name_syllabus.text = syllabus
        user_id.text = user
        date_id.text = date
        description_tv.text = descrip
        my_txtv_pm.text = promo
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide
            .with(this)
            .load(cover_ic)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(pdf_id_icone)

        setListener()
        val firebaseUser = firebaseAuth.currentUser
        val id_last = firebaseUser?.uid.toString()

        if (id_uses.toString() != id_last) {
            dele_pst.visibility = View.GONE
        } else {

            dele_pst.visibility = View.VISIBLE
        }

        mLayoutManager = LinearLayoutManager(this@DetailleActivity)
        //mLayoutManager!!.reverseLayout = true
        //mLayoutManager!!.stackFromEnd = true
        comment_recyclerview.layoutManager = mLayoutManager


        if (promo != "Preparatoire" && promo != "L1") {
            promo = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")
        val options = FirebaseRecyclerOptions.Builder<commentaire_model>()
            .setQuery(
                ref.child(promo).child(cles.toString()).child("comment_syl"),
                commentaire_model::class.java
            )
            .build()
        adpter = commentaire_adapters(options)
        comment_recyclerview.adapter = adpter


    }

    override fun onStart() {
        super.onStart()
        adpter.startListening()
        comment_recyclerview!!.recycledViewPool.clear()
        adpter.notifyDataSetChanged()
    }
    override fun onStop() {
        super.onStop()
        adpter.stopListening()
    }

    fun setListener() {
        dele_pst.setOnClickListener {
            val pop = PopupMenu(this@DetailleActivity, dele_pst)
            pop.menuInflater.inflate(R.menu.popup_menu, pop.menu)
            pop.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (item.itemId == R.id.delete_id) {
                        DeletePoste()
                    }
                    return true
                }
            })
            pop.show()


        }
        btn_downl.setOnClickListener {
            add_downloas()
            //telecharger()
            // Toast.makeText(this, "telechargement", Toast.LENGTH_SHORT).show()
        }
        btn_read.setOnClickListener {
            val lien = intent.getStringExtra("lien_book")
            val intent = Intent(this, LectureActivity::class.java)
            intent.putExtra("nom", name_syllabus.text.toString())
            intent.putExtra("lien_book", lien)
            add_view()
            startActivity(intent)
        }
        check_teste()
    }

    fun check_teste() {
        InputComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                send_comment.isVisible = InputComment.text.toString().trim().isNotEmpty()
                send_comment.setOnClickListener {
                    check_post()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun telecharger() {
        val intent = intent
        val lien = intent.getStringExtra("lien_book")
        val nom = intent.getStringExtra("syllabus")
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
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        Toast.makeText(this, "Telechargement Encours", Toast.LENGTH_SHORT).show()
    }

    fun ajouter_data() {
        val cal = Calendar.getInstance()
        val sdf1 = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val strDate = sdf1.format(cal.time)
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        val hashMap = HashMap<String, Any>()
        hashMap["commentaire"] = InputComment.text.toString()
        hashMap["nom"] = mon_nom
        hashMap["date"] = strDate.toString()
        hashMap["profil"] = photo_profil



        if (pm != "Preparatoire" && pm != "L1") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")
        ref.child(pm.toString()).child(cle.toString()).child("comment_syl").child(ref.push().key!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                InputComment.setText("")
                add_comment()
                Toast.makeText(this, "commenter", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun DeletePosteStorage() {
        val image_name_id = intent.getStringExtra("image_url")
        val storageRef = storageReference
        val desertRef = storageRef.child(image_name_id.toString())
        desertRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "image supprime", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun DeletePoste() {
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        if (pm != "Preparatoire" && pm != "L1") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus")

        ref.child(pm.toString()).child(cle.toString()).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    DeletePosteStorage()
                    Toast.makeText(this, "publication supprimenr", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun read_name() {
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs").document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it != null) {
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post-nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    mon_nom = "$pren $postn"
                    photo_profil = imgetxt.toString()

                } else {
                    Log.d(ContentValues.TAG, "no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }

    fun add_downloas() {
        var promo = intent.getStringExtra("promo")
        val cle = intent.getStringExtra("cle")

        if (promo != "Preparatoire" && promo != "L1") {
            promo = "Tous"
        }
        val increment: MutableMap<String, Any> = HashMap()
        increment["download"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo.toString()).child((cle.toString()))
            .updateChildren(increment)
            .addOnSuccessListener {
                Toast.makeText(
                    this@DetailleActivity,
                    "plus 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@DetailleActivity,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun add_view() {
        var promo = intent.getStringExtra("promo")
        val cle = intent.getStringExtra("cle")

        if (promo != "Preparatoire" && promo != "L1") {
            promo = "Tous"
        }
        val increment: MutableMap<String, Any> = HashMap()
        increment["like"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo.toString()).child((cle.toString()))
            .updateChildren(increment)
            .addOnCompleteListener { 
                if (it.isSuccessful){
                    
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun add_comment() {
        var promo = intent.getStringExtra("promo").toString()
        val cle = intent.getStringExtra("cle").toString()
        val increment: MutableMap<String, Any> = HashMap()
        if (promo != "Preparatoire" && promo != "L1") {
            promo = "Tous"
        }
        increment["comment"] = ServerValue.increment(1)
        FirebaseDatabase.getInstance().reference.child("syllabus")
            .child(promo).child((cle))
            .updateChildren(increment)
            .addOnSuccessListener {
                Toast.makeText(
                    this@DetailleActivity,
                    "plus 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@DetailleActivity,
                    e.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    fun check_post() {
        val cle = intent.getStringExtra("cle")
        var pm = intent.getStringExtra("promo")
        if (pm != "Preparatoire" && pm != "L1") {
            pm = "Tous"
        }
        val ref = FirebaseDatabase.getInstance().getReference("syllabus").child(pm.toString())
            .child(cle.toString())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(this@DetailleActivity, "cette pub existe", Toast.LENGTH_SHORT)
                        .show()
                    ajouter_data()
                    InputComment.setText("")
                } else {
                    Toast.makeText(
                        this@DetailleActivity,
                        "commentaire non permis",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) //Don't ignore potential errors!
            }
        }
        ref.addListenerForSingleValueEvent(eventListener)

    }
}