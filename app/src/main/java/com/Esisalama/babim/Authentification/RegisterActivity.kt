package com.Esisalama.babim.Authentification

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.Esisalama.babim.home.MainActivity
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.databinding.ActivityRegisterBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var filepath: Uri? = null
    var inputMail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        clikbtn()
        binding.nom.setText( intent.getStringExtra("postnom").toString()
        )
        binding.prenom.setText(intent.getStringExtra("nom").toString())
        binding.admeil.text = intent.getStringExtra("mail").toString()
        inputMail =intent.getStringExtra("mail").toString()
    }
    private fun clikbtn(){
        binding.promotionText.setOnClickListener {
            choixpromo()
        }
        binding.btnSignup.setOnClickListener {
            if (isValidSignUpDetails()){
                firebaseSignUp()
            }
        }
        binding.layoutImage.setOnClickListener {
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
                            this@RegisterActivity,
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
        binding.genre.setOnClickListener {
            choigenre()
        }
    }
    private fun getprofil_link(p:String){
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["profil"] = p
        database.collection(Constant.Etudiant)
            .document(inputMail)
            .set(infor_user, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    SavePrefData(p)
                }else{
                    showtoast(it.exception?.message.toString())
                }
            }
    }
    fun send_profil(){
        val pd = ProgressDialog(this)
        pd.setTitle("Creation du compte")
        pd.show()
        val name = "profil$inputMail"
        val reference = storageReference.child("photo_profil/$name.png")
        filepath?.let {
            reference.putFile(it)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                        getprofil_link(uri.toString())
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "creation du compte echouer", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                }
                .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val percent =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                    pd.setMessage("validation du compte: " + percent.toInt() + "%")
                    pd.setCancelable(false)
                }
        }
    }
    private fun getInfoUser(){
        loading(true)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["nom"] = binding.nom.text.toString()
        infor_user["date arriver"] = date_dins.toString()
        infor_user["post_nom"] = binding.postNom.text.toString()
        infor_user["prenom"] = binding.prenom.text.toString()
        infor_user["mail"] = inputMail
        infor_user["sexe"] = binding.genreChoice.text.toString()
        infor_user["Numero de telephone"] = binding.number.text.toString()
        infor_user["promotion"] = binding.promotionChoice.text.toString()
        infor_user["administrateur"] = "non"
        infor_user["admin_assistant"] = "non"
        infor_user["ouverture_application"] = 1
        infor_user["id_reserve1"] = ""
        infor_user["id_reserve2"] = ""
        infor_user["id_reserve3"] = ""
        infor_user["id_reserve4"] = ""
        database.collection(Constant.Etudiant)
            .document(inputMail)
            .set(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    loading(false)
                }else{
                    showtoast(it.exception?.message.toString())
                    loading(false)
                }
            }
    }
    private fun SavePrefData(imagelink:String){
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val date_dins = sdf.format(Date())
        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply(){
            putString("nom",binding.nom.text.toString())
            putString("date arriver",date_dins.toString())
            putString("post_nom",binding.postNom.text.toString())
            putString("prenom",binding.prenom.text.toString())
            putString("mail",inputMail)
            putString("sexe",binding.genreChoice.text.toString())
            putString("numero de telephone",binding.number.text.toString())
            putString("promotion",binding.promotionChoice.text.toString())
            putString("lien profil",imagelink)
            putString("administrateur","non")
            putString("admin_assistant","non")
            putInt("count",1)
            putInt("post_forum",0)
            putInt("syllabus_telecharger",0)
            putInt("commentaire_forum",0)
            putInt("commentaire_syllabus",0)
            putInt("profil_completed",1)
        }.apply()
        abonnement("syllabus")
        abonnement("forum")
        acti_sy("forum_state")
        acti_sy("syllabus_state")

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun pick_image() {
        ImagePicker.Companion.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                512,
                512
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start(101)
    }
    private fun firebaseSignUp() {
        getInfoUser()
        send_profil()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUri : Uri
        if (requestCode == 101 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            filepath = data.data!!
            binding.imageProfil.setImageURI(imageUri)
            binding.txtAddPhoto.visibility = View.GONE
        }
    }
    private fun showtoast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidSignUpDetails(): Boolean {
        return if (binding.genreChoice.text.toString() == "") {
            showtoast("Choississez votre genre")
            false
        } else if (filepath == null) {
            showtoast("chosissez une image")
            false
        }

        else if (binding.nom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre nom")
            false
        }
        else if (binding.nom.text.toString().trim().length<3) {
            binding.nom.error = "caratere minimun 3"
            false
        }
        else if (binding.postNom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre post nom")
            false
        }
        else if (binding.postNom.text.toString().trim().length<3) {
            binding.postNom.error = "nombre des caracteres minimum 3"
            false
        }
        else if (binding.prenom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre prenom")
            false
        }
        else if (binding.prenom.text.toString().trim().length<3) {
            binding.prenom.error = "minimun 3 caractere"
            false
        }
        else if (binding.number.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre numero de telephone")
            false
        }
        else if (binding.number.text.toString().length <9 ||binding.number.text.toString().length > 9 ) {
            showtoast("Entrer un numero a 9 chiffre")
            binding.number.error = "Ex: 975937553"
            false
        }
        else if (binding.promotionChoice.text.toString() == "") {
            showtoast("chossissez votre promotion")
            false
        }
        else {
            true
        }
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSignup.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.btnSignup.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
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
        )
        alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            val s = listItems[which]
           binding.promotionChoice.text = s
            dialog.dismiss()
        }
        alertDialog.setNegativeButton("Annuler") { dialog, which ->
            dialog.dismiss()
        }
        val customAlertDialog = alertDialog.create()
        customAlertDialog.show()
    }
    private fun choigenre(){
        val checkedItem = intArrayOf(-1)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.sexe)
        alertDialog.setTitle("genre")
        val listgenre = arrayOf(
            "Masculin","Feminin"
        )
        alertDialog.setSingleChoiceItems(listgenre,checkedItem[0]){
            alertDialog, which ->
            checkedItem[0] = which
            val s = listgenre[which]
            binding.genreChoice.text = s
            alertDialog.dismiss()
        }
        alertDialog.setNegativeButton("Annuler"){ dialog, wich ->
            dialog.dismiss()
        }
        val customeDialogue = alertDialog.create()
        customeDialogue.show()

    }
    private fun abonnement(nom:String){
        FirebaseMessaging.getInstance().subscribeToTopic(nom).addOnSuccessListener {

        }

    }
    fun acti_sy(name:String){
        val editor = getSharedPreferences("save", MODE_PRIVATE).edit()
        editor.putBoolean(name, true)
        editor.apply()
    }


}