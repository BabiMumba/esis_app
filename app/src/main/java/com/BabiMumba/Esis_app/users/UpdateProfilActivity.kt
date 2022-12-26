package com.BabiMumba.Esis_app.users

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.BabiMumba.Esis_app.R
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
import kotlinx.android.synthetic.main.activity_update_profil.*
import java.util.*

class UpdateProfilActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var filepath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)
        storageReference = FirebaseStorage.getInstance().reference

        firebaseAuth = FirebaseAuth.getInstance()
        readData()
        setListener()
    }
    fun setListener(){
        save_btn.setOnClickListener {
            if (filepath == null){
                update_data()
            }else{
                send_profil()
                update_data()
            }

        }
        edit_img.setOnClickListener {
            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        setImage()
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@UpdateProfilActivity,
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

    private fun readData(){
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs")
            .document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val name = document.data?.getValue("nom").toString()
                    val postname = document.data?.getValue("post-nom").toString()
                    val num = document.data?.getValue("Numero de telephone").toString()
                    val prenoms = document.data?.getValue("prenom").toString()
                    val mailTo = document.data?.getValue("mail").toString()
                    val promotion = document.data?.getValue("promotion").toString()
                    val admin = document.data?.getValue("administrateur").toString()
                    val premium = document.data?.getValue("premium").toString()
                    promot.text = promotion
                    u_mail.text = mailTo
                    u_nume.text = num
                    ui_post_name.setText(postname)
                    prenom_ui.setText(prenoms)
                    ui_name.setText(name)
                  
                    if (admin=="oui"){
                        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply() {
                            putString("administrateur", "oui")
                        }.apply()
                        //Toast.makeText(this, "vous etes un administrateur", Toast.LENGTH_SHORT).show()
                    }else{
                        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply() {
                            putString("administrateur", "non")
                        }.apply()
                    }
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                }else{
                    Log.d(ContentValues.TAG, "document inconnue")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun setImage() {
        ImagePicker.Companion.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                512,
                512
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start(101)
    }
    private fun pick_image() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Selectionner un fichier"
            ), 101
        )
    }

    private fun SavePrefData() {
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply() {
            putString("nom", ui_name.text.toString())
            putString("post-nom", ui_post_name.text.toString())
            putString("prenom", prenom_ui.text.toString())

        }.apply()
        showtoast("mise ajour reussi")
    }
    private fun update_data(){
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val count_app = sharedPreferences.getInt("count",0)

        loading(true)
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["nom"] = ui_name.text.toString()
        infor_user["post-nom"] = ui_post_name.text.toString()
        infor_user["prenom"] = prenom_ui.text.toString()
        infor_user["ouverture_application"] = count_app
        database.collection("Utilisateurs")
            .document(mail)
            .update(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    SavePrefData()
                    loading(false)
                }else{
                    loading(false)
                    showtoast(it.exception?.message.toString())
                }
            }
    }
    private fun showtoast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            save_btn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            save_btn.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUri : Uri
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            imgView_proPic.setImageURI(imageUri)
            filepath = data.data!!
        }
    }
    fun send_profil(){
        val pd = ProgressDialog(this)
        pd.setTitle("photo de profil change")
        pd.show()
        val name = "profil${u_mail.text}"
        val reference = storageReference.child("photo_profil/$name.png")
        filepath?.let {
            reference.putFile(it)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                        getprofil_link(uri.toString())
                        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply(){
                            putString("lien profil",uri.toString())
                        }.apply()

                        pd.dismiss()
                    }
                }
                .addOnFailureListener{ idst ->
                    Toast.makeText(this, "${idst.message}", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                }
                .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val percent =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                    pd.setMessage("importation de votre photo: " + percent.toInt() + "%")
                    pd.setCancelable(false)
                }
        }
    }
    private fun getprofil_link(p:String){
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["profil"] = p
        database.collection("Utilisateurs")
            .document(mail)
            .update(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showtoast("photo de profil mis ajours")
                }else{
                    showtoast(it.exception?.message.toString())
                }
            }
    }

}