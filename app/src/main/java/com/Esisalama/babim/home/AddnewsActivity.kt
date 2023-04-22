package com.Esisalama.babim.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.fcm.FcmNotificationsSender
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
import java.text.SimpleDateFormat
import java.util.*


class AddnewsActivity : AppCompatActivity() {

    var filepath: Uri? = null
    var liste_promotion = mutableListOf<String>()
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
            if (liste_promotion.size < 1){
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
                    //send_data(image_news)
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
                                //send_data(it.toString())
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
    private fun send_data(image_news:String,promotion_selected:String){
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
        infor_user["id_doc"] = id_doc
        infor_user["date"] = date_dins
        infor_user["mail"] = mail_add.toString()
        infor_user["promot"] = promotion_selected
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
        val cbx_l1 = dialog.findViewById<CheckBox>(R.id.ckb_l1)
        val cbx_l2a = dialog.findViewById<CheckBox>(R.id.ckb_l2a)
        val cbx_l2b = dialog.findViewById<CheckBox>(R.id.ckb_l2b)
        val cbx_l3as = dialog.findViewById<CheckBox>(R.id.ckb_l3as)
        val cbx_l3tlc = dialog.findViewById<CheckBox>(R.id.ckb_l3tlc)
        val cbx_l3gl = dialog.findViewById<CheckBox>(R.id.ckb_l3gl)
        val cbx_l3msi = dialog.findViewById<CheckBox>(R.id.ckb_l3msi)
        val cbx_l3des = dialog.findViewById<CheckBox>(R.id.ckb_l3design)
        val cbx_l4as = dialog.findViewById<CheckBox>(R.id.ckb_l4as)
        val cbx_l4tlc = dialog.findViewById<CheckBox>(R.id.ckb_l4tlc)
        val cbx_l4gl = dialog.findViewById<CheckBox>(R.id.ckb_l4gl)
        val cbx_l4msi = dialog.findViewById<CheckBox>(R.id.ckb_l4msi)
        val cbx_l4des = dialog.findViewById<CheckBox>(R.id.ckb_l4design)
        //verifier si le case sont coche
        btn.setOnClickListener {

            if (cbx_l1.isChecked){
                liste_promotion.add("L1")
            }
            if (cbx_l2a.isChecked){
                liste_promotion.add("L2_A")
            }
            if (cbx_l2b.isChecked){
                liste_promotion.add("L2_B")
            }
            if (cbx_l3as.isChecked){
                liste_promotion.add("l3_AS")
            }
            if (cbx_l3tlc.isChecked){
                liste_promotion.add("L3_TLC")
            }
            if (cbx_l3gl.isChecked){
                liste_promotion.add("L3_GL")
            }
            if (cbx_l3msi.isChecked){
                liste_promotion.add("L3_MSI")
            }
            if (cbx_l3des.isChecked){
                liste_promotion.add("L3_DESIGN")
            }
            if (cbx_l4as.isChecked){
                liste_promotion.add("L4_AS")
            }
            if (cbx_l4tlc.isChecked){
                liste_promotion.add("L4_TLC")
            }
            if (cbx_l4gl.isChecked){
                liste_promotion.add("l4_GL")
            }
            if (cbx_l4msi.isChecked){
                liste_promotion.add("L4_MSI")
            }
            if (cbx_l4des.isChecked){
                liste_promotion.add("L4_DESIGN")
            }
            dialog.dismiss()

            val adapter_liste = ArrayAdapter(this,android.R.layout.simple_list_item_1,liste_promotion)
            lstv_promot.adapter = adapter_liste

          /*  Toast.makeText(this, "${liste_promotion.size}", Toast.LENGTH_SHORT).show()
            for (i in liste_promotion){

            }*/

        }

        dialog.show()
        dialog.window!!.attributes = lp

    }


}