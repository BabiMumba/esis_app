package com.Esisalama.babim.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.Esisalama.babim.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_feed_back.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FeedBack : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        btn_send.setOnClickListener {
            if (txt_feedback.text.toString().trim().isEmpty()){
                txt_feedback.error = "Entrer votre message"
            }else{
                sen_message_feedback()
            }

        }

    }
    fun loading(isLoading: Boolean){
        if (isLoading){
            btn_send.visibility = View.GONE
            progress_bar.visibility = View.VISIBLE
        }else{
            btn_send.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }
    }
    fun sen_message_feedback(){
        loading(true)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date()).toString()
        val data:MutableMap<String,Any> = HashMap()
        val nom = intent.getStringExtra("mail")
          data[date_dins] = "${txt_feedback.text}"
        val db = FirebaseFirestore.getInstance()
        db.collection("feedback").document(nom.toString())
            .set(data, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "message envoyer", Toast.LENGTH_SHORT).show()
                    txt_feedback.setText("")
                    loading(false)

                }else{
                    loading(false)
                    Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show()
                }
            }

    }
}