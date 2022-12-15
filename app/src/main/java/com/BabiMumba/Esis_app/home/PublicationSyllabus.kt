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
import androidx.appcompat.app.AlertDialog
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.fcm.FcmNotificationsSender
import com.BabiMumba.Esis_app.model.save_profil_syllabus
import com.BabiMumba.Esis_app.model.syllabus_model
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
import kotlinx.android.synthetic.main.activity_publication_syllabus.*
import java.text.SimpleDateFormat
import java.util.*

class PublicationSyllabus : AppCompatActivity() {

    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var mon_nom:String = ""
    var token_id:String = ""
    var lien_image:String = ""
    lateinit var filepath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication_syllabus)

        firebaseAuth = FirebaseAuth.getInstance()


        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().getReference("syllabus")

        read_name()
        get_token()

        publish_file.isEnabled = false
        upload_file.setOnClickListener { view: View? ->
            Dexter.withContext(
                applicationContext
            )
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val intent = Intent()
                        intent.type = "application/pdf"
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(
                                intent,
                                "Selectionner un fichier"
                            ), 101
                        )
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                        Toast.makeText(
                            this@PublicationSyllabus,
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
        publish_file.setOnClickListener {
            if (promotion_text.text.toString() == ""){
                Toast.makeText(this, "choicissez une promotion", Toast.LENGTH_SHORT).show()
            }else{
                processupload(filepath)
            }

        }
        val checkedItem = intArrayOf(-1)
        promotion_choice.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.pdf_file)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {

            filepath = data!!.data!!
            val filename = filepath.toString().substringAfter(".","")

            fil_path.text = filename

            if (filename == "pdf") {
                //
                icone_failed.visibility = View.GONE
                icone_succes.visibility = View.VISIBLE
                publish_file.isEnabled = true
                Toast.makeText(this, "fichier pdf ", Toast.LENGTH_SHORT).show()

            }else
            {
                icone_failed.visibility = View.VISIBLE
                icone_succes.visibility = View.GONE
                Toast.makeText(this, "Selectionner un fichier pdf ", Toast.LENGTH_SHORT).show()
                publish_file.isEnabled = false
            }

        }
    }

    fun processupload(filepath: Uri?) {

        val firebaseUser = firebaseAuth.currentUser
        val id_users = firebaseUser?.uid.toString()
        val mail_users = firebaseUser?.email.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")

        val currentDate = sdf.format(Date())
        val pd = ProgressDialog(this)
        pd.setTitle("File Uploading....!!!")
        pd.show()
        val name = nom_du_syllabus.text.toString()
        val descp = description.text.toString()
        val nameProf = nom_du_prof.text.toString()
        var nn = promotion_text.text.toString()


        if (nn!="Preparatoire" && nn!= "L1"){
            nn = "Tous"
        }

        val name_save_sta = "livres/$nn/$name.pdf"

        val reference = storageReference.child(name_save_sta)
        val id_poste = databaseReference.push().key!!.toString()
        reference.putFile(filepath!!)
            .addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                    val obj = syllabus_model(
                        name.toUpperCase(),
                        mail_users,
                        id_users,
                        name_save_sta,
                        token_id,
                        promotion_text.text.toString(),
                        descp,
                        nameProf,
                        uri.toString()
                        ,mon_nom,
                        currentDate,
                        lien_image, 0, 0, 0)
                    databaseReference.child(nn).child(id_poste).setValue(obj)
                    pd.dismiss()
                    Toast.makeText(applicationContext, "Fichier publier", Toast.LENGTH_LONG).show()
                    icone_failed.visibility = View.VISIBLE
                    icone_succes.visibility = View.GONE
                    publish_file.isEnabled = false
                    sendnotif(promotion_text.text.toString())
                    save_syllabus_mprfl(uri.toString(),name,id_poste)
                    nom_du_syllabus.setText("")
                    nom_du_prof.setText("")
                    description.setText("")
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
    fun sendnotif(msg:String) {
        FcmNotificationsSender.pushNotification(
            this,
            "/topics/syllabus",
            getString(R.string.app_name),
            "${mon_nom} a publier un syllabus de $msg",
        )
    }

    fun save_syllabus_mprfl(lien:String, nom_livre:String, id_post:String){
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString().replaceAfter("@"," ")

        val data = save_profil_syllabus(lien,nom_livre,id_post)
        databaseReference = FirebaseDatabase.getInstance().getReference("syllabus_poste_save")
        databaseReference.child(mail).child(id_post).setValue(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "save poste to my profil syllabus ", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }
    fun save_toke(){
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("token_id", token_id)
        }.apply()

    }
}