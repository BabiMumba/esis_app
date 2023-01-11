package com.BabiMumba.Esis_app.Authentification

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_count.*

class GoogleCountActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAut: FirebaseAuth

    lateinit var progressDialog:ProgressDialog
    var nameu = ""
    var admail = ""
    var pname = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_count)

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        progressDialog = ProgressDialog(this,R.style.MyDialogTheme)
        progressDialog.setTitle("Patienter...")
        progressDialog.setMessage("chargement du compte")
        progressDialog.setCancelable(false)
        progressDialog.setIcon(R.drawable.icone)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAut = FirebaseAuth.getInstance()
        google_count.setOnClickListener {
            sign_in()
        }
        check_btn.setOnClickListener {
            if (mAut.currentUser != null){
                Toast.makeText(this, "vous etes ok", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "pas ok", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sign_in() {
        progressDialog.show()
        val signiIntent = googleSignInClient.signInIntent
        startActivityForResult(signiIntent, 120)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            progressDialog.dismiss()
             if (task.isSuccessful){
                try {
                    progressDialog.dismiss()
                    val account = task.getResult(ApiException::class.java)!!
                    val adm= account.email.toString()
                    nameu= account.givenName.toString()
                    admail = account.email.toString()
                    pname = account.familyName.toString()
                    if (adm.contains("esisalama.org")){
                        firebaseAuthWithGoogle(account.idToken!!)
                    }else{
                        googleSignInClient.signOut()
                        Toast.makeText(this, "selectionnez un compte d'esis", Toast.LENGTH_SHORT).show()
                    }

                }catch (e:Exception){
                    progressDialog.dismiss()
                    Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                }
            }else{
                progressDialog.dismiss()
                Toast.makeText(this, "erreur: ${task.exception}", Toast.LENGTH_SHORT).show()
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
                    val intent = Intent(this, RegisterActivity::class.java)
                        .putExtra("","")
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "erreur:${task.exception}", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }

    }

    override fun onBackPressed() {

    }

}