package com.BabiMumba.Esis_app.Authentification

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.databinding.ActivityRegisterAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var filepath: Uri? = null
    var inputMail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //recuperation des informations

        binding.nom.setText( intent.getStringExtra("postnom").toString()
        )
        binding.prenom.setText(intent.getStringExtra("nom").toString())
        binding.admeil.text = intent.getStringExtra("mail").toString()
        inputMail =intent.getStringExtra("mail").toString()

    }
}