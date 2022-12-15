package com.BabiMumba.Esis_app.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.fragment.ForumFragment
import com.BabiMumba.Esis_app.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.navigation)

        loadFragmant(HomeFragment())
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.home

        val navview = findViewById<NavigationView>(R.id.nav_view)

        val tete:View = navview.getHeaderView(0)
        val image:View = navview.getHeaderView(0)
        val mail:View = navview.getHeaderView(0)

        val image_mail: ImageView = image.findViewById(R.id.profil_user)
        val nom: TextView = tete.findViewById(R.id.name)
        val mail_user: TextView = mail.findViewById(R.id.mail_user)


    }

    private fun loadFragmant(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.openDrawer(GravityCompat.START)
        }
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
            R.id.quit -> {
                finish()
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
                    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
                    drawer.openDrawer(GravityCompat.START)
                }

            }
            true
        }

}