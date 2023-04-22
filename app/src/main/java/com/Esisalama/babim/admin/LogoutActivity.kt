package com.Esisalama.babim.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.Esisalama.babim.R
import com.Esisalama.babim.admin.adpters.delete_adapters
import com.Esisalama.babim.admin.model.model_logout
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_logout.*

class LogoutActivity : AppCompatActivity() {
    private var layoutManager: LinearLayoutManager?= null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var adaps: delete_adapters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        firebaseAuth = FirebaseAuth.getInstance()

        layoutManager = LinearLayoutManager(this@LogoutActivity)
        count_delete_id.layoutManager = layoutManager
        val ref = FirebaseFirestore.getInstance().collection("Deconnection")
        val options = FirestoreRecyclerOptions.Builder<model_logout>()
            .setQuery(ref,model_logout::class.java)
            .build()
        adaps = delete_adapters(options)
        count_delete_id.adapter = adaps

    }

    override fun onStart() {
        adaps.startListening()
        count_delete_id.recycledViewPool.clear()
        adaps.notifyDataSetChanged()
        super.onStart()
    }

    override fun onStop() {
        adaps.stopListening()
        super.onStop()
    }
}