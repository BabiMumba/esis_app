package com.Esisalama.babim.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.users.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detaille_news.*
import kotlinx.android.synthetic.main.content_news.*
import kotlinx.android.synthetic.main.content_news.date_news
import kotlinx.android.synthetic.main.content_news.descri
import kotlinx.android.synthetic.main.layout_news.*

class DetailleNews : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaille_news)

        val titre = intent.getStringExtra("titre")

        val message = intent.getStringExtra("message")
        val date = intent.getStringExtra("date")
        val image = intent.getStringExtra("image")
        val auteur = intent.getStringExtra("auteur")
        val promot = intent.getStringExtra("promot")
        val addrese_mail = intent.getStringExtra("mail")


        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
        val admin_state = sharedPreferences.getString("mail",null)
        val admin_prof = sharedPreferences.getString("administrateur",null)

        if ((addrese_mail == admin_state) || admin_prof == "oui"){
            menu_btn.visibility = View.VISIBLE
        }else{
            menu_btn.visibility = View.GONE
        }
        promot_news.text = promot
        title_news.text = titre
        descri.text = message
        date_news.text = date
        autor_news.text = "par $auteur"
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        circularProgressDrawable.start()
        Glide
            .with(this)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(news_image_id)

        news_image_id.setOnClickListener {
            startActivity(Intent(this, ImageView::class.java)
                .putExtra("lien_image",image)
            )
        }

        popup_menu()
    }

    fun popup_menu(){
        val id_news = intent.getStringExtra("id_news").toString()
        val image = intent.getStringExtra("image")
        menu_btn.setOnClickListener {
            val pop = PopupMenu(this@DetailleNews, menu_btn)
            pop.menuInflater.inflate(R.menu.news_popup, pop.menu)
            pop.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (item.itemId == R.id.delete_id) {
                        Deletedialogue()
                    }else if (item.itemId == R.id.update){
                       startActivity(Intent(this@DetailleNews,AddnewsActivity::class.java)
                           .putExtra("titre",title_news.text.toString())
                           .putExtra("message",descri.text.toString())
                           .putExtra("id_news",id_news)
                           .putExtra("image",image)
                           .putExtra("mod","oui")

                       )
                    }
                    return true
                }
            })
            pop.show()
        }
    }

    fun Deletedialogue() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Suppression de votre communique")
        alertDialog.setMessage("voulez-vouz vraiment supprimer")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"oui"){
                d: DialogInterface, _:Int ->
            DeletePoste()
            d.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"annuler"){
                d: DialogInterface, _:Int ->
            d.dismiss()
        }

        alertDialog.show()
    }
    fun DeletePoste(){
        val id_news = intent.getStringExtra("id_news").toString()
        val database = FirebaseFirestore.getInstance()
        database.collection("communique").document(id_news)
            .delete()
            .addOnCompleteListener { 
                if (it.isSuccessful){
                    Toast.makeText(this, "supprimer avec succer", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }

    }
}