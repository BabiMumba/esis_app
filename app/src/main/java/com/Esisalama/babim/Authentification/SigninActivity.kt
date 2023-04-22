package com.Esisalama.babim.Authentification

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Esisalama.babim.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.ads.mediationtestsuite.activities.HomeActivity
import kotlinx.android.synthetic.main.activity_google_count.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SigninActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1

    private val siginiprovides = listOf(AuthUI.IdpConfig.EmailBuilder()
        .setAllowNewAccounts(true)
        .setRequireName(true)
        .build()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_count)
        google_count.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(siginiprovides)
                .setLogo(R.drawable.logokitabu2)
                .build()
            startActivityForResult(intent,RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode== Activity.RESULT_OK){
                val progresssdialog = indeterminateProgressDialog("setting up your account")
                progresssdialog.setCancelable(false)
                    startActivity(intentFor<HomeActivity>().newTask().clearTask())
                    progresssdialog.dismiss()
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                if (response == null) return
                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK ->
                        longSnackbar(my_relative,"pas d'internet")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longSnackbar(my_relative,"Erreur inconnue")
                }

            }

        }
    }
}