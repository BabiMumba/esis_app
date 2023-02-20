package com.BabiMumba.Esis_app.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.adpters.useradptr
import com.BabiMumba.Esis_app.admin.model.modeluser
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_promotionnell_page.*

class PromotionnellPage : AppCompatActivity() {

    private var layoutManager: LinearLayoutManager? = null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var adaps: useradptr


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotionnell_page)

        firebaseAuth = FirebaseAuth.getInstance()
        layoutManager = LinearLayoutManager(this@PromotionnellPage)
        promo_recyclerview.layoutManager = layoutManager
        //Rreference des utilisateur
        val ref = FirebaseFirestore.getInstance().collection("Utilisateurs")
        val options = FirestoreRecyclerOptions.Builder<modeluser>()
            .setQuery(
                ref,
                modeluser::class.java
            )
            .build()
        adaps = useradptr(options)
        promo_recyclerview.adapter = adaps
    }

    override fun onStart() {
        adaps.startListening()
        promo_recyclerview.recycledViewPool.clear()
        adaps.notifyDataSetChanged()
        super.onStart()
    }

    override fun onStop() {
        adaps.stopListening()
        super.onStop()
    }
}