package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.poste_users_adapters
import com.google.firebase.auth.FirebaseAuth

class InfosActivity : AppCompatActivity() {
    var mon_nom:String = ""
    var lien_image:String = ""
    lateinit var adpter: poste_users_adapters
    private var mLayoutManager: LinearLayoutManager? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infos)
    }
}