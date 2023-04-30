package com.Esisalama.babim.home

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        bottomNavigationView = findViewById(R.id.navigation)

        loadFragmant(HomeFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.home

        //chek version app play store
        val db = FirebaseFirestore.getInstance()
        db.collection("version")
            .document("version_app")
            .get()
            .addOnSuccessListener {
                val code = it.getString("code")
                val cancelable = it.getBoolean("cancelable")
                val message = it.getString("message")
                val version_code = it.getString("version_code")
                val taille = it.getString("taille")

                //chek the difference
                val version_code_mobil = Constant.version_code
                if (version_code_mobil != code!!.toInt()){
                    chek_version(cancelable!!,message!!,taille!!,version_code!!)
                }else{
                    Toast.makeText(this,"votre application est à jour",Toast.LENGTH_LONG).show()
                }


            }
            .addOnFailureListener {
                //log the error
                Log.d("TAG", "erreur: ${it.message}")

            }



    }

    private fun loadFragmant(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    override fun onBackPressed() {
    }


    private fun chek_version(cancel:Boolean, messages:String, tailles:String, version_codes:String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.new_version)
        dialog.setCancelable(cancel)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val message = dialog.findViewById<TextView>(R.id.message)
        val taille = dialog.findViewById<TextView>(R.id.taille)
        val version_code = dialog.findViewById<TextView>(R.id.version_code)
        //init the view
        message.text = messages
        taille.text = tailles
        version_code.text = version_codes

        dialog.findViewById<View>(R.id.update_btn).setOnClickListener { v: View? ->
            dialog.cancel()

        }
        // dialog.findViewById<View>(R.id.btn_feedback).setOnClickListener { v: View? -> startActivity(settg)}
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showtoast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.forum -> {
                    loadFragmant(ForumFragment())
                }
                R.id.home -> {
                    loadFragmant(HomeFragment())
                }
                R.id.menu -> {
                    loadFragmant(ProfilFragment())
                }
                R.id.commq -> {
                    loadFragmant(CommuniqueFragment())
                }
                R.id.course -> {
                    loadFragmant(CourseFragment())
                }

            }
            true
        }

}