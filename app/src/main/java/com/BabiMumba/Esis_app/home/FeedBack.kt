package com.BabiMumba.Esis_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_feed_back.*

class FeedBack : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        btn_send.setOnClickListener {
            sen_message_feedback()
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
        val data:MutableMap<String,Any> = HashMap()
        val nom = intent.getStringExtra("mail")
        data["message"] = "$nom : ${txt_feedback.text}"
        val db = FirebaseFirestore.getInstance()
        // val message = db.collection("feedback").document(nom)
        db.collection("feedback")
            .add(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "message envoyer", Toast.LENGTH_SHORT).show()
                    loading(false)

                }else{
                    loading(false)
                    Toast.makeText(this, "erreur", Toast.LENGTH_SHORT).show()
                }
            }

    }
}