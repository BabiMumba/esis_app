package com.BabiMumba.Esis_app.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_addnews.*
import kotlinx.android.synthetic.main.activity_addnews.progress_bar
import kotlinx.android.synthetic.main.activity_addnews.promotion_choice
import kotlinx.android.synthetic.main.activity_addnews.promotion_text
import kotlinx.android.synthetic.main.activity_publish_post.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddnewsActivity : AppCompatActivity() {

    var filepath: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnews)

        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val ttl = intent.getStringExtra("titre")
        val message = intent.getStringExtra("message")
        val mod = intent.getStringExtra("mod")

        promotion_choice.setOnClickListener {
            choise_promotion()
        }
        if (mod == "oui"){
            title_news.setText(ttl.toString())
            message_news.setText(message.toString())
            send_commq.setText("modifier")
        }else{


        }
        image_news.setOnClickListener {
            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        pick_image()
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@AddnewsActivity,
                            "vous devez accepter pour continuer",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
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
                    val image_news =intent.getStringExtra("image").toString()
                    if (filepath == null){
                        update_data(image_news)
                    }else{
                        progress_bar.visibility = View.VISIBLE
                        val firebaseUser = firebaseAuth.currentUser
                        var mail = firebaseUser?.email.toString()
                        if (mail.contains(".")){
                            mail =  mail.replace(".","")
                        }
                        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
                        val date_de_pub = sdf.format(Date())
                        val name = "news_image$date_de_pub"
                        val name_image = "news_post/$mail/$name.png"
                        val reference = storageReference.child(name_image)
                        reference.putFile(filepath!!)
                            .addOnSuccessListener {
                                reference.downloadUrl.addOnSuccessListener {
                                    update_data(it.toString())
                                    progress_bar.visibility = View.GONE
                                }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                            }
                            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                                val poucent =  (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                                progress_bar.progress = poucent.toInt()
                            }
                    }

                }else if (filepath == null){
                    val image_news = "https://www.esisalama.com/assets/img/actualite/img-25082022-141338.png"
                    send_data(image_news)
                }else{
                    progress_bar.visibility = View.VISIBLE
                    val firebaseUser = firebaseAuth.currentUser
                    var mail = firebaseUser?.email.toString()
                    if (mail.contains(".")){
                       mail =  mail.replace(".","")
                    }
                    val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
                    val date_de_pub = sdf.format(Date())
                    val name = "news_image$date_de_pub"
                    val name_image = "news_post/$mail/$name.png"
                    val reference = storageReference.child(name_image)
                    reference.putFile(filepath!!)
                        .addOnSuccessListener {
                            reference.downloadUrl.addOnSuccessListener {
                                send_data(it.toString())
                                progress_bar.visibility = View.GONE
                            }
                        }
                        .addOnFailureListener{
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                            val poucent =  (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                            progress_bar.progress = poucent.toInt()
                        }

                }

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUri : Uri
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            image_news.setImageURI(imageUri)
            text_ind.visibility = View.GONE
            filepath = data.data!!
        }
    }
    private fun send_data(image_news:String){
        val sharedPreferences = this.getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val prenoms = sharedPreferences.getString("prenom","")
        val post_nom = sharedPreferences.getString("post_nom","")
        val mail_add = sharedPreferences.getString("mail","")
        loading(true)
        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())//2021-03-01 12:08:43
        val id_doc = "le${System.currentTimeMillis()}"
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["titre"] = title_news.text.toString()
        infor_user["message"] = message_news.text.toString()
        infor_user["autor"] = "$prenoms $post_nom"
        infor_user["all_promotion"] = promotion_text.text.toString() == "Toutes les promotions"
        infor_user["id_doc"] = id_doc
        infor_user["date"] = date_dins
        infor_user["mail"] = mail_add.toString()
        infor_user["promot"] = promotion_text.text.toString()
        infor_user["image"] = image_news
        database.collection("communique")
            .document(id_doc)
            .set(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                   // sendnotif(title_news.text.toString())
                    Toast.makeText(this, "envoyer avec succe", Toast.LENGTH_SHORT).show()
                    loading(false)
                }else{
                    Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()
                   // showtoast(it.exception?.message.toString())
                    loading(false)
                }
            }
    }
    fun loading(isLoading: Boolean){
        if (isLoading){
            progress_bar_send.visibility = View.VISIBLE
            send_commq.visibility = View.GONE
        }else{
            progress_bar_send.visibility = View.GONE
            send_commq.visibility = View.VISIBLE

        }
    }
    private fun update_data(image_news: String){
        loading(true)
        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val id_news = intent.getStringExtra("id_news").toString()
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["titre"] = title_news.text.toString()
        infor_user["message"] = message_news.text.toString()
        infor_user["date"] = date_dins
        infor_user["image"] = image_news
        infor_user["promot"] = promotion_text.text.toString()
        database.collection("communique").document(id_news)
            .update(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loading(true)
                    Toast.makeText(this, "modifier avec succee", Toast.LENGTH_SHORT).show()
                }else{
                    loading(false)
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

    fun pick_image() {
        ImagePicker.Companion.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(18000) //l'image final sera compresser jusqu'a 12 mega octet
            .maxResultSize(
                700,
                700
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start(101)
    }
    fun choise_promotion(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_promotion_check)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val btn =dialog.findViewById<Button>(R.id.valide_btn)

        btn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp

    }


}