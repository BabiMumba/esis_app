package com.BabiMumba.Esis_app.Authentification

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
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


}