package com.BabiMumba.Esis_app.users

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.BabiMumba.Esis_app.R

class DeleteCount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_count)

        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val password = sharedPreferences.getString("mot de passe",null)


    }

}