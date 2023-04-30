package com.Esisalama.babim.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.home.Add_book
import com.Esisalama.babim.home.AddnewsActivity
import kotlinx.android.synthetic.main.activity_sale_controle.*

class SaleControleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_controle)

        visiblity()
       clikmethode()

    }
    fun visiblity(){
      val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, MODE_PRIVATE)
        val admin = sharedPreferences.getString("administrateur","")
        if (admin != "oui"){
            prof_count.visibility = View.GONE
            users_page.visibility = View.GONE
            logout_users.visibility = View.GONE
        }
    }
    fun clikmethode(){
        users_page.setOnClickListener {
            startActivity(Intent(this,AllusersActivity::class.java))
        }
        logout_users.setOnClickListener {
            startActivity(Intent(this,LogoutActivity::class.java))
        }
        add_commique.setOnClickListener {
            startActivity(Intent(this,AddnewsActivity::class.java)
                .putExtra("mod","non")
            )
        }
        add_syllabus.setOnClickListener {
            startActivity(Intent(this,Add_book::class.java)
            )
        }
        prof_count.setOnClickListener {
            startActivity(Intent(this,PromotionnellPage::class.java)
            )
        }
    }
}