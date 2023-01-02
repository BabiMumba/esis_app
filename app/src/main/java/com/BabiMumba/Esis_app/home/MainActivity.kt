package com.BabiMumba.Esis_app.home

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.fragment.ForumFragment
import com.BabiMumba.Esis_app.fragment.HomeFragment
import com.BabiMumba.Esis_app.fragment.ProfilFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

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



    }

    private fun loadFragmant(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    override fun onBackPressed() {
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.syllabus -> {
                startActivity(Intent(this,Syllabus_FragmentActivity::class.java))
            }
            R.id.actualite -> {
                val actulaity_link = "https://www.esisalama.com/index.php?module=actualite"
                val intent = Intent(this,ActualiteActivity::class.java)
                intent.putExtra("url_link",actulaity_link)
                startActivity(intent)

            }
            R.id.esis_web -> {
                val actulaity_link = "https://www.esisalama.com/index.php"
                val intent = Intent(this,ActualiteActivity::class.java)
                intent.putExtra("url_link",actulaity_link)
                startActivity(intent)
            }
            R.id.horaire ->{
                val actulaity_link = "https://www.esisalama.com/index.php?module=horaire"
                val intent = Intent(this,ActualiteActivity::class.java)
                intent.putExtra("url_link",actulaity_link)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                startActivity(Intent(this,SettingActivity::class.java))
            }
            R.id.nav_rate -> {
                showtoast("noter l'application")
            }
            R.id.nav_share ->{
                showtoast("partager")
            }

        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
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

            }
            true
        }

}