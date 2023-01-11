package com.BabiMumba.Esis_app.Authentification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_count.*

class GoogleCountActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAut: FirebaseAuth

    companion object {
        private const val RC_SIGN_IN = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_count)

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAut = FirebaseAuth.getInstance()

        google_count.setOnClickListener {
            sign_in()
        }

    }

    private fun sign_in() {
        val signiIntent = googleSignInClient.signInIntent
        startActivityForResult(signiIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    progressDialog.dismiss()
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("se connecter","firebase authentification"+account.id)
                    //Toast.makeText(this, "valider", Toast.LENGTH_SHORT).show()
                    firebaseAuthWithGoogle(account.idToken!!)
                }catch (e:Exception){
                    progressDialog.dismiss()
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }else{
                progressDialog.dismiss()
                Toast.makeText(this, "Verifier votre connexion et Ressayer  ", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        progressDialog.show()
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAut.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                progressDialog.dismiss()
                if (task.isSuccessful){
                    val firebaseuser = mAut.currentUser
                    val mail = firebaseuser!!.email
                    val nom = firebaseuser?.displayName
                    cretedoc(mail.toString())
                    feedback(mail.toString())
                    save_Data(nom.toString(),mail.toString())
                    date_arriver()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    progressDialog.dismiss()
                }
            }

    }

}