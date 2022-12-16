package com.BabiMumba.Esis_app.Authentification

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.home.MainActivity
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    var filepath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        clikbtn()
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
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Selectionner une image"
                ), 101
            )

        }

        binding.genre.setOnClickListener {
            choigenre()
        }
    }
    private fun getprofil_link(p:String){
        ///photo_profil
        val database = FirebaseFirestore.getInstance()
        val infor_user:MutableMap<String, Any> = HashMap()
        infor_user["profil"] = p
        database.collection("Utilisateurs")
            .document(binding.inputMail.text.toString())
            .set(infor_user, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showtoast("yes document creer")
                }else{
                    showtoast(it.exception?.message.toString())
                }
            }
    }
    fun send_profil(){
        val pd = ProgressDialog(this)
        pd.setTitle("Creation du compte")
        pd.show()
        val name = "profil${System.currentTimeMillis()}"
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
        infor_user["date d'inscription"] = date_dins.toString()
        infor_user["post-nom"] = binding.postNom.text.toString()
        infor_user["prenom"] = binding.prenom.text.toString()
        infor_user["mail"] = binding.inputMail.text.toString()
        infor_user["sexe"] = binding.genreChoice.text.toString()
        infor_user["Numero de telephone"] = binding.number.text.toString()
        infor_user["promotion"] = binding.promotionChoice.text.toString()
        infor_user["mot de passe"] = binding.inputPassword.text.toString()
        database.collection("Utilisateurs")
            .document(binding.inputMail.text.toString())
            .set(infor_user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showtoast("yes document creer")
                    loading(false)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    showtoast(it.exception?.message.toString())
                    loading(false)
                }
            }
    }
    private fun SavePrefData(){
        val sharedPreferences = getSharedPreferences("info_users",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply(){

        }.apply()
    }

    private fun firebaseSignUp() {
        loading(true)
        val mail = binding.inputMail.text.toString()
        val motdpasse = binding.inputPassword.text.toString()
        firebaseAuth.createUserWithEmailAndPassword(mail,motdpasse)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){
                    send_profil()
                    getInfoUser()
                    loading(false)
                }else{
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    loading(false)
                }
            }

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
        else if (binding.postNom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre post nom")
            false
        }
        else if (binding.prenom.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre prenom")
            false
        }
        else if (binding.number.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre numero de telephone")
            false
        }
        else if (binding.number.text.toString().length <10 ||binding.number.text.toString().length > 10 ) {
            showtoast("Entrer un numero a 10 chiffre")
            binding.number.error = "Ex: 0975937553"
            false
        }
        else if (binding.inputMail.text.toString().trim().isEmpty()) {
            showtoast("Entrer un mail")
            false
        }
        else if (binding.promotionChoice.text.toString() == "") {
            showtoast("chossissez votre promotion")
            false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputMail.text.toString())
                .matches()
        ) {
            showtoast("Enter valid mail")
            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            showtoast("Entrer votre mot de passe")
            false
        }
        else if (binding.inputPassword.text.toString().length < 6) {
            showtoast("Entrer un mot de passe fort")
            binding.inputPassword.error = "caractere recquise minimum 6"
            false
        }
        else if (binding.inputconfirmpassword.text.toString().trim().isEmpty()) {
            showtoast("confirmer votre mot de passe")
            false
        } else if (binding.inputPassword.text.toString() != binding.inputconfirmpassword.text.toString()
        ) {
            showtoast("mot de passe different")
            false
        } else {
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
            "Preparatoire",
            "L1",
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
        alertDialog.setIcon(R.drawable.developement_ic)
        alertDialog.setTitle("genre")
        val listgenre = arrayOf(
            "Homme","Femme"
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


}