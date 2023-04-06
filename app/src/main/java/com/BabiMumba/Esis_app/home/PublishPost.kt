package com.BabiMumba.Esis_app.home

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.BabiMumba.Esis_app.model.post_model
import com.BabiMumba.Esis_app.model.poste_users_model
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
import kotlinx.android.synthetic.main.activity_publication_syllabus.*
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
    lateinit var collection_name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_post)

        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)

        collection_name = if (adm == "oui"){
            Constant.Admin
        }else{
            Constant.Etudiant
        }


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
            .compress(18000) //l'image final sera compresser jusqu'a 12 mega octet
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
        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
        val admin_as = sharedPreferences.getString("admin_assistant",null).toString()
        val admin = sharedPreferences.getString("administrateur",null).toString()
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
        val tsp = SimpleDateFormat("yyyy-M-dd")
        val timespam = tsp.format(Date())
        val document = "$timespam-${System.currentTimeMillis()}"
        val poste_data = hashMapOf<String,Any>(
            "nom" to mon_nom,
            "admin_assistant" to admin_as,
            "administrateur" to admin,
            "ad_mail" to mail,
            "token_users" to token_id,
            "users_id" to id_user,
            "image_name_id" to name_image,
            "date" to date_de_pub,
            "message" to msg,
            "profil" to lien_image,
            "id_reserv1" to "",
            "id_reserv2" to "",
            "id_reserv3" to "",
            "image_poste" to "",
            "id_poste" to document,
            "vue" to 0,
            "nb_comment" to 0,
        )
        val db = Firebase.firestore
        db.collection("poste_forum").document(document).set(poste_data).addOnCompleteListener {
            if (it.isSuccessful){
                // Toast.makeText(this, "compte creer", Toast.LENGTH_SHORT).show()
                loading(false)
                sendnotif()
            }else{
                loading(false)
                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun publish_post2(uri: Uri,name_image:String){
        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
        val admin_as = sharedPreferences.getString("admin_assistant",null).toString()
        val admin = sharedPreferences.getString("administrateur",null).toString()
        loading(true)
        databaseReference = FirebaseDatabase.getInstance().getReference("forum_discussion")
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val id_user = firebaseUser?.uid.toString()
        val sdf = SimpleDateFormat("HH:mm dd/M/yyyy ")
        val date_de_pub = sdf.format(Date())
        val msg = message_commnq.text.toString()
        val tsp = SimpleDateFormat("yyyy-M-dd")
        val timespam = tsp.format(Date())
        val document = "$timespam-${System.currentTimeMillis()}"

        val poste_data = hashMapOf<String,Any>(
            "nom" to mon_nom,
            "admin_assistant" to admin_as,
            "administrateur" to admin,
            "ad_mail" to mail,
            "token_users" to token_id,
            "users_id" to id_user,
            "image_name_id" to name_image,
            "date" to date_de_pub,
            "message" to msg,
            "profil" to lien_image,
            "id_reserv1" to "",
            "id_reserv2" to "",
            "id_reserv3" to "",
            "image_poste" to uri,
            "id_poste" to document,
            "vue" to 0,
            "nb_comment" to 0,
        )
        val db = Firebase.firestore
        db.collection("poste_forum").document(document).set(poste_data).addOnCompleteListener {
            if (it.isSuccessful){
                // Toast.makeText(this, "compte creer", Toast.LENGTH_SHORT).show()
                loading(false)
                sendnotif()
            }else{
                loading(false)
                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun read_name(){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection_name).document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    val postn = it.data?.getValue("post_nom").toString()
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

    /*fun save_post_mprfl(userId:String,imposte:String,imageurl:String,id_post:String,msg:String){
        val firebaseUser = firebaseAuth.currentUser

        var mail = firebaseUser?.email.toString().replaceAfter("@"," ")
        if (mail.contains(".")){
           mail =  mail.replace(".","")
        }
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
    */
    fun sendnotif() {
        FcmNotificationsSender.pushNotification(
            this,
            "/topics/forum",
            getString(R.string.app_name),
            "${mon_nom} a publier une nouvelle photo",
        )
    }


}