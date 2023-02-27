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
import androidx.appcompat.app.AlertDialog
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
    lateinit var collection_name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        //initialisatin de la collection
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)
        if (adm == "oui"){
            collection_name = "Professeur"
        }else{
            collection_name = "Utilisateurs"
        }
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
        l4.setOnClickListener {
            choixpromo()
        }
    }
    private fun choixpromo() {
        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.developement_ic)
        alertDialog.setTitle("Promotion")
        val listItems = arrayOf(
            "L1",
            "L2_A",
            "L2_B",
            "L3_AS",
            "L3_TLC",
            "L3_GL",
            "L3_MSI",
            "L3_DESIGN",
            "L4_AS",
            "L4_TLC",
            "L4_MSI",
            "L4_GL",
            "L4_DESIGN",
            "VC_L1",
            "VC_L2",
            "VC_L3",
        )
        alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            val s = listItems[which]
            promot.text = s
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Annuler") { dialog, which ->
            dialog.dismiss()
        }
        val customAlertDialog = alertDialog.create()
        customAlertDialog.show()
    }
    private fun readData(){
        val fireuser= firebaseAuth.currentUser
        val mail = fireuser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection_name)
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
                    val admin = document.data?.getValue("adminP").toString()
                    promot.text = promotion
                    u_mail.text = mailTo
                    u_nume.text = num
                    ui_post_name.setText(postname)
                    prenom_ui.setText(prenoms)
                    ui_name.setText(name)

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
    private fun SavePrefData() {
        val sharedPreferences = getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply() {
            putString("nom", ui_name.text.toString())
            putString("post-nom", ui_post_name.text.toString())
            putString("prenom", prenom_ui.text.toString())
            putString("promotion", promot.text.toString())
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
        infor_user["promotion"] = promot.text.toString()
        infor_user["ouverture_application"] = count_app
        database.collection(collection_name)
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
        database.collection(collection_name)
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