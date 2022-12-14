package com.BabiMumba.Esis_app.Authentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        clicmethode()

    }
    fun clicmethode(){
        create_new_compte.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
    private fun showtoast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignin.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSignin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
    private fun valide():Boolean{
        return if(binding.inputMail.text.toString().trim().isEmpty()){
            showtoast("entrer un mail")
            false
        }else if  (!Patterns.EMAIL_ADDRESS.matcher(binding.inputMail.text.toString())
                .matches()) {
            showtoast("Entrer un mail valide")
            false
        }
        else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre mot de passe")
            false
        }else{
            true
        }
    }

}