package com.BabiMumba.Esis_app.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.adpters.useradptr
import com.google.firebase.auth.FirebaseAuth

class PromotionnellPage : AppCompatActivity() {

    private var layoutManager: LinearLayoutManager?= null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var adaps: useradptr


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotionnell_page)

    }
}