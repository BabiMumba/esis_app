package com.BabiMumba.Esis_app.Authentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.home.MainActivity
import com.BabiMumba.Esis_app.databinding.ActivityLoginBinding
import com.BabiMumba.Esis_app.users.DeleteCount
import com.google.firebase.auth.FirebaseAuth

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

        binding.createNewCompte.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        binding.btnSignin.setOnClickListener {
            if (valide()){
                firebaseLogin()
            }
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
    private fun firebaseLogin() {
        loading(true)
        firebaseAuth.signInWithEmailAndPassword(binding.inputMail.text.toString(),binding.inputPassword.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful){

                    startActivity(Intent(this, MainActivity::class.java))
                    loading(false)
                    finish()

                }else{
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                    loading(false)
                }

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

    override fun onBackPressed() {
        show_dialogue()
        super.onBackPressed()
    }
    fun show_dialogue(){
        val builder = AlertDialog.Builder(this)
            .setTitle("Quitter")
            .setPositiveButton("quitter") { dialogInterface, i ->
               finish()
                dialogInterface.dismiss()
            }
            .setNegativeButton("Annuler") { dialogInterface, i ->
                Toast.makeText(this, "annuler", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
        val customAlertDialog = builder.create()
        customAlertDialog.show()
    }

}