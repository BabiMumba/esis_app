package com.BabiMumba.Esis_app.Authentification

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    lateinit var filepath: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clikbtn()

    }
    private fun clikbtn(){

        binding.promotionText.setOnClickListener {
            choixpromo()
        }
        binding.btnSignup.setOnClickListener {
            if (isValidSignUpDetails()){
                Toast.makeText(this, "tout est ok", Toast.LENGTH_SHORT).show()
            }
        }

        binding.layoutImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Selectionner une image"
                ), 101
            )

        }

        binding.genre.setOnClickListener {
            choigenre()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUri : Uri
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            filepath = data.data!!
            binding.imageProfil.setImageURI(imageUri)
            binding.txtAddPhoto.visibility = View.GONE
        }
    }
    private fun showtoast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignUpDetails(): Boolean {

        return if (binding.genreChoice.text.toString() == "") {
            showtoast("Choississez votre genre")
            false
        } else if (filepath == null) {
            showtoast("chosissez une image")
            false
        }

        else if (binding.nom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre nom")
            false
        }
        else if (binding.postNom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre post nom")
            false
        }
        else if (binding.prenom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre prenom")
            false
        }
        else if (binding.number.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre numero de telephone")
            false
        }
        else if (binding.number.text.toString().length <10 ||binding.number.text.toString().length > 10 ) {
            showtoast("Entrer un numero a 10 chiffre")
            binding.number.error = "Ex: 0975937553"
            false
        }
        else if (binding.inputMail.text.toString().trim().isEmpty()) {
            showtoast("Entrer un mail")
            false
        }
        else if (binding.promotionChoice.text.toString() == "") {
            showtoast("chossissez votre promotion")
            false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputMail.text.toString())
                .matches()
        ) {
            showtoast("Enter valid mail")
            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre mot de passe")
            false
        }
        else if (binding.inputPassword.text.toString().length < 6) {
            showtoast("Entrer un mot de passe fort")
            binding.inputPassword.error = "caractere recquise minimum 6"
            false
        }
        else if (binding.inputconfirmpassword.text.toString().trim().isEmpty()) {
            showtoast("confirmer votre mot de passe")
            false
        } else if (binding.inputPassword.text.toString() != binding.inputconfirmpassword.text.toString()
        ) {
            showtoast("mot de passe different")
            false
        } else {
            true
        }
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignup.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSignup.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
    private fun choixpromo() {
        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.developement_ic)
        alertDialog.setTitle("Promotion")
        val listItems = arrayOf(
            "Preparatoire",
            "L1",
            "G2 ",
            "GL",
            "G2 MSI",
            "G2 DSG",
            "G2 AS",
            "G2 TLC",
            "G3 GL",
            "G3 MSI",
            "G3 DSG",
            "G3 AS",
            "G3 TLC",
            " M1 AS-TLC",
            "M1 DESIGN",
            "M1 MIAGE"
        )
        alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            val s = listItems[which]
           binding.promotionChoice.text = s
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Annuler") { dialog, which ->
            dialog.dismiss()
        }
        val customAlertDialog = alertDialog.create()
        customAlertDialog.show()
    }
    private fun choigenre(){
        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.developement_ic)
        alertDialog.setTitle("genre")




    }


}