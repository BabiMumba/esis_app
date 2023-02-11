package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addnews.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddnewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnews)
        send_commq.setOnClickListener {
            send_commi()
        }

    }
    private fun send_commi(){
        //loading(true)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["titre"] = title_news.text.toString()
        infor_user["message"] = message_news.text.toString()
        infor_user["date"] = date_dins
        infor_user["image"] = "https://www.esisalama.com/assets/img/actualite/img-25082022-141338.png"
        database.collection("commnique")
            .document(date_dins)
            .set(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "envoyer avec succe", Toast.LENGTH_SHORT).show()
                   // loading(false)
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                   // showtoast(it.exception?.message.toString())
                   // loading(false)
                }
            }
    }
}