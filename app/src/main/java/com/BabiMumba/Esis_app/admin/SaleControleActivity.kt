package com.BabiMumba.Esis_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.AddnewsActivity
import com.BabiMumba.Esis_app.home.PublicationSyllabus
import kotlinx.android.synthetic.main.activity_sale_controle.*

class SaleControleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_controle)

       clikmethode()

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
            startActivity(Intent(this,PublicationSyllabus::class.java)
            )
        }
        prof_count.setOnClickListener {
            startActivity(Intent(this,PromotionnellPage::class.java)
            )
        }
    }
}