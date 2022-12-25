package com.BabiMumba.Esis_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R
import kotlinx.android.synthetic.main.activity_sale_controle.*

class SaleControleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_controle)
        users_page.setOnClickListener {
            startActivity(Intent(this,AllusersActivity::class.java))
        }
        logout_users.setOnClickListener {
            startActivity(Intent(this,LogoutActivity::class.java))
        }

    }
}