package com.Esisalama.babim.Authentification

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.Esisalama.babim.home.MainActivity
import com.Esisalama.babim.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_publication_syllabus.*
import kotlinx.android.synthetic.main.activity_update_profil.*

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
    fun getData(){
        val pd = ProgressDialog(this)
        pd.setTitle("recuperation de votre compte")
        pd.show()
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs")
            .document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply(){
                        putString("nom",document.data?.getValue("nom").toString())
                        putString("date d'inscription",document.data?.getValue("date d'inscription").toString())
                        putString("post_nom",document.data?.getValue("post-nom").toString())
                        putString("prenom",document.data?.getValue("prenom").toString())
                        putString("mail",document.data?.getValue("mail").toString())
                        putString("sexe",document.data?.getValue("sexe").toString())
                        putString("numero de telephone",document.data?.getValue("Numero de telephone").toString())
                        putString("promotion",document.data?.getValue("promotion").toString())
                        putString("mot de passe",document.data?.getValue("mot de passe").toString())
                        putString("lien profil",document.data?.getValue("profil").toString())
                        putString("administrateur",document.data?.getValue("administrateur").toString())
                        putString("premium",document.data?.getValue("premium").toString())
                    }.apply()
                    pd.dismiss()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                    
                }else{
                    pd.dismiss()
                    Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "document inconnue")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    fun clicmethode(){

        binding.createNewCompte.setOnClickListener {
            startActivity(Intent(this,GoogleCountActivity::class.java))
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
                    loading(false)
                    getData()
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
    }

}