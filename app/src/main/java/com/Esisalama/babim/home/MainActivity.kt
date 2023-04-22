package com.Esisalama.babim.home

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.Esisalama.babim.R
import com.Esisalama.babim.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
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
        showDialog()



    }

    private fun loadFragmant(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    override fun onBackPressed() {
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.new_version)
        dialog.setCancelable(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
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