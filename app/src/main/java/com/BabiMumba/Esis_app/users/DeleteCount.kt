package com.BabiMumba.Esis_app.users

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_delete_count.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DeleteCount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_count)




    }
    fun clik_method(){
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val password = sharedPreferences.getString("mot de passe",null)
        if (txt_passwor.text.toString() != password){
            Toast.makeText(this, "mot de passe different", Toast.LENGTH_SHORT).show()
        }else{
            save_person()
        }

    }

    private fun save_person() {
        loading(true)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date()).toString()
        val data:MutableMap<String,Any> = HashMap()
        val nom = intent.getStringExtra("mail")
        data[date_dins] = "${txt_passwor.text}"
        val db = FirebaseFirestore.getInstance()
        db.collection("Deconnection").document(nom.toString())
            .set(data, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "message envoyer", Toast.LENGTH_SHORT).show()
                    txt_passwor.setText("")
                    loading(false)

                }else{
                    loading(false)
                    Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun loading(isLoading: Boolean){
        if (isLoading){
            btn_verif.visibility = View.GONE
            progress_bars.visibility = View.VISIBLE
        }else{
            btn_verif.visibility = View.VISIBLE
            progress_bars.visibility = View.GONE
        }
    }


}