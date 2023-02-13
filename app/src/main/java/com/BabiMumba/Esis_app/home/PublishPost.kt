package com.BabiMumba.Esis_app.home

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.BabiMumba.Esis_app.model.post_model
import com.BabiMumba.Esis_app.model.poste_users_model
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_publish_post.*
import java.text.SimpleDateFormat
import java.util.*

class PublishPost : AppCompatActivity() {
    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var mon_nom:String = ""
    var lien_image:String = ""
    var token_id:String = ""
    var filepath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_post)

        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        read_name()
        get_token()
        publish_btn.setOnClickListener {
            if (filepath == null){
                if (message_commnq.text.toString().trim().isEmpty()){
                    message_commnq.error = "entrer quelque chose"
                }else{
                    publish_post1()
                }

            }else{
                publish_poste(filepath)
            }

        }

        iimg.setOnClickListener {
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
                            this@PublishPost,
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



    }

    fun loading(isLoading: Boolean){
        if (isLoading){
            progress_bar.visibility = View.VISIBLE
            publish_btn.visibility = View.GONE
        }else{
            progress_bar.visibility = View.GONE
            publish_btn.visibility = View.VISIBLE

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUri : Uri
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            image_file.setImageURI(imageUri)
            txt1.visibility = View.GONE
            filepath = data.data!!
        }
    }
    fun pick_image() {
        ImagePicker.Companion.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(12000) //l'image final sera compresser jusqu'a 12 mega octet
            .maxResultSize(
                700,
                700
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start(101)
    }
    fun publish_poste(filepath: Uri?) {

        val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        val date_de_pub = sdf.format(Date())

        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val pd = ProgressDialog(this)
        pd.setTitle("publication")
        pd.show()
        val name = "post_image$date_de_pub"
        val name_image = "forum_post/$mail/$name.png"
        val reference = storageReference.child(name_image)
        reference.putFile(filepath!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                    publish_post2(uri,name_image)
                    pd.dismiss()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "importation echouer", Toast.LENGTH_SHORT).show()
                pd.dismiss()
            }
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val percent =
                    (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                pd.setMessage("Importation: " + percent.toInt() + "%")
                pd.setCancelable(false)
            }
    }
    fun publish_post1(){
        loading(true)
        databaseReference = FirebaseDatabase.getInstance().getReference("forum_discussion")
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val id_user = firebaseUser?.uid.toString()
        val sdf = SimpleDateFormat("HH:mm dd/M/yyyy ")
        val date_de_pub = sdf.format(Date())
        val name = "image${System.currentTimeMillis()}"
        val msg = message_commnq.text.toString()
        val name_image = "forum_post/$mail/$name.png"
        val id_pst = databaseReference.push().key!!.toString()
        val donnee = post_model(mon_nom,mail,"","","",id_pst,token_id,id_user,name_image,date_de_pub,msg,lien_image,"1",0,0)
        databaseReference.child(id_pst).setValue(donnee)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    save_post_mprfl(id_user,"1",lien_image,id_pst,msg)
                    message_commnq.setText("")
                    message_commnq.hint = "nouveau poste"
                    Toast.makeText(this, "publier avec succes", Toast.LENGTH_SHORT).show()
                    sendnotif()
                    loading(false)
                }else{
                    loading(false)
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun publish_post2(uri: Uri,name_image:String){
        loading(true)
        databaseReference = FirebaseDatabase.getInstance().getReference("forum_discussion")
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val id_user = firebaseUser?.uid.toString()
        val sdf = SimpleDateFormat("HH:mm dd/M/yyyy ")
        val date_de_pub = sdf.format(Date())
        val msg = message_commnq.text.toString()
        val id_pst = databaseReference.push().key!!.toString()
        val donnee = post_model(mon_nom,mail,"","","",id_pst,token_id,id_user,name_image,date_de_pub,msg,lien_image,uri.toString(),0,0)
        databaseReference.child(id_pst).setValue(donnee)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    save_post_mprfl(id_user,uri.toString(),lien_image,id_pst,msg)
                    message_commnq.setText("")
                    message_commnq.hint = "nouveau poste"
                    Toast.makeText(this, "publier avec succes", Toast.LENGTH_SHORT).show()
                    sendnotif()
                    loading(false)
                }else{
                    loading(false)
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun read_name(){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs").document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post-nom").toString()
                    val imgetxt = it.data?.getValue("profil")
                    mon_nom = "$pren $postn"
                    lien_image = imgetxt.toString()
                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "erreur ${it}", Toast.LENGTH_SHORT).show()
            }
    }
    fun get_token(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            token_id = token.toString()
        })
    }
    fun save_post_mprfl(userId:String,imposte:String,imageurl:String,id_post:String,msg:String){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString().replaceAfter("@"," ")
        val data = poste_users_model(imageurl,id_post,token_id,userId,imposte,msg,"","","","")
        databaseReference = FirebaseDatabase.getInstance().getReference("poste_save")
        databaseReference.child(mail).child(id_post).setValue(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                }
            }

    }
    fun sendnotif() {
        FcmNotificationsSender.pushNotification(
            this,
            "/topics/forum",
            getString(R.string.app_name),
            "${mon_nom} a publier une nouvelle photo",
        )
    }


}