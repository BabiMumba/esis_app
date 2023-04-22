package com.Esisalama.babim.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.Esisalama.babim.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_about_developpeur.*

class AboutDeveloppeur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_developpeur)
        clicmethode()

    }
    fun clicmethode(){
        note_btn.setOnClickListener {
            val url = getString(R.string.lien_app)
            val i = Intent(Intent.ACTION_VIEW)
            i.data= Uri.parse(url)
            startActivity(i)
        }
        wtsp.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://api.whatsapp.com/send?phone=243975937553")
            startActivity(i)
        }

        gith.setOnClickListener {
            val url = "https://github.com/BabiMumba"
            val i = Intent(Intent.ACTION_VIEW)
            i.data=Uri.parse(url)
            startActivity(i)
        }
        mail_brn.setOnClickListener {
            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("mailto:babimumba243@gmail.com")
            startActivity(i)
        }
        call_btn.setOnClickListener {
            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val nume = "tel:+243975937553"
                        val i = Intent(Intent.ACTION_CALL)
                        i.data=Uri.parse(nume)
                        startActivity(i)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@AboutDeveloppeur,
                            "vous devez accepter pour continuer",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }

        }
    }