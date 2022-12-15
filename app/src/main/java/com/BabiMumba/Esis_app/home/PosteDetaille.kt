package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference

class PosteDetaille : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    var mon_nom:String = ""
    var photo_profil:String = ""
    var token_id:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poste_detaille)

    }
}