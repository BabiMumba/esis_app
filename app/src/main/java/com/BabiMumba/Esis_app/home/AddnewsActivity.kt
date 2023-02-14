package com.BabiMumba.Esis_app.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addnews.*
import kotlinx.android.synthetic.main.activity_addnews.promotion_choice
import kotlinx.android.synthetic.main.activity_addnews.promotion_text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddnewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnews)

        val ttl = intent.getStringExtra("titre")
        val message = intent.getStringExtra("message")
        val mod = intent.getStringExtra("mod")

        if (mod == "oui"){
            title_news.setText(ttl.toString())
            message_news.setText(message.toString())
            send_commq.setText("modifier")
        }else{

        }

        send_commq.setOnClickListener {
            if (promotion_text.text.toString() == ""){
                Toast.makeText(this, "promotion obligatoire", Toast.LENGTH_SHORT).show()
            }else if (title_news.text.toString().trim().isEmpty()){
                title_news.error = "titre de votre communique"
            }else if (message_news.text.toString().trim().isEmpty()){
                message_news.error= "Votre message est obligatoire"
            }else{
                if (mod == "oui"){
                    update_data()
                }else{
                    send_data()
                }

            }
        }
        val checkedItem = intArrayOf(-1)
        promotion_choice.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.pdf_file)
            alertDialog.setTitle("Promotion")
            val listItems = arrayOf(
                "Toute les promotions",
                "L1",
                "L2",
                "G2 ",
                "GL",
                "G2 MSI",
                "G2 DSG",
                "G2 AS",
                "G2 TLC",
                "G3 GL",
                "G3 MSI",
                "G3 DSG",
                "G3 AS",
                "G3 TLC",
                " M1 AS-TLC",
                "M1 DESIGN",
                "M1 MIAGE"
            )

            alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
                checkedItem[0] = which
                val s = listItems[which]
                promotion_text.text = s
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            val customAlertDialog = alertDialog.create()
            customAlertDialog.show()
        }
    }
    private fun send_data(){
        val sharedPreferences = this.getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val prenoms = sharedPreferences.getString("prenom",null)
        val post_nom = sharedPreferences.getString("post-nom",null)
        //loading(true)
        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val id_doc = "le$date_dins"
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["titre"] = title_news.text.toString()
        infor_user["message"] = message_news.text.toString()
        infor_user["autor"] = "$prenoms $post_nom"
        infor_user["id_doc"] = id_doc
        infor_user["date"] = date_dins
        infor_user["promot"] = promotion_text.text.toString()
        infor_user["image"] = "https://www.esisalama.com/assets/img/actualite/img-25082022-141338.png"
        database.collection("communique")
            .document(id_doc)
            .set(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    sendnotif(title_news.text.toString())
                    Toast.makeText(this, "envoyer avec succe", Toast.LENGTH_SHORT).show()
                   // loading(false)
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                   // showtoast(it.exception?.message.toString())
                   // loading(false)
                }
            }
    }
    private fun update_data(){
        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val id_news = intent.getStringExtra("id_news").toString()
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["titre"] = title_news.text.toString()
        infor_user["message"] = message_news.text.toString()
        infor_user["date"] = date_dins
        infor_user["promot"] = promotion_text.text.toString()
        database.collection("communique").document(id_news)
            .update(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "modifier avec succee", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }




    }
    fun sendnotif(title: String) {
        FcmNotificationsSender.pushNotification(
            this,
            "/topics/communique",
            "ESIS COMMUNIQUE",
            "$title",
        )
    }


}