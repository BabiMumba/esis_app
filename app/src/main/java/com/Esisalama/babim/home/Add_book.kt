package com.Esisalama.babim.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.fcm.FcmNotificationsSender
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
import java.text.SimpleDateFormat
import java.util.*

class Add_book : AppCompatActivity() {

    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var mon_nom:String = ""
    var token_id:String = ""
    var lien_image:String = ""
    lateinit var filepath: Uri
    lateinit var collection_name:String
    var cover_path: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication_syllabus)

        firebaseAuth = FirebaseAuth.getInstance()
        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)
        collection_name = if (adm == "oui"){
           Constant.Admin
        }else{
            Constant.Etudiant
        }
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
                            this@Add_book,
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
            }else if (nom_du_syllabus.text.toString().trim().isEmpty())
            {
               nom_du_syllabus.error = "obligatoire"
            }else if (nom_du_prof.text.toString().trim().isEmpty()){
                nom_du_prof.error = "obligatoire"
            }else{
                //Toast.makeText(this, "tres bien", Toast.LENGTH_SHORT).show()
                processupload(filepath)
            }

        }

        val checkedItem = intArrayOf(-1)
        promotion_choice.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.pdf_file)
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
                promotion_text.text = s
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            val customAlertDialog = alertDialog.create()
            customAlertDialog.show()
        }
        cober_btn.setOnClickListener {
            pick_image()
        }
    }
    fun pick_image() {
        ImagePicker.Companion.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 8 MB(Optional)
            .maxResultSize(
                400,
                400
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start(102)
    }
    @SuppressLint("Range")
    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK)
        {
            filepath = data!!.data!!
            val filename = filepath.toString().substringAfter(".","")
            if (filename == "pdf"|| filename.contains("documents")) {
                icone_failed.visibility = View.GONE
                icone_succes.visibility = View.VISIBLE
                publish_file.isEnabled = true
                val filename = getFileNameFromUri(this, filepath)
                original_title.setText(filename)

            }else
            {
                icone_failed.visibility = View.VISIBLE
                icone_succes.visibility = View.GONE
                Toast.makeText(this, "Selectionner un fichier pdf ", Toast.LENGTH_SHORT).show()
                publish_file.isEnabled = false
            }

        }
        val imageUri:Uri
        if (requestCode == 102 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            syllabus_icone.setImageURI(imageUri)
            cober_btn.visibility = View.GONE
            fcl.visibility = View.GONE
            syllabus_icone.visibility = View.VISIBLE
            cover_path = data.data!!

        }
    }

    fun processupload(filepath: Uri?) {
        val sharedPreferences = getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
        val admin_as = sharedPreferences.getString("admin_assistant",null).toString()
        val admin = sharedPreferences.getString("administrateur",null).toString()
        val firebaseUser = firebaseAuth.currentUser
        val id_users = firebaseUser?.uid.toString()
        val mail_users = firebaseUser?.email.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
        val tsp = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(Date())
        val timespam = tsp.format(Date())
        val document = "$timespam-${System.currentTimeMillis()}"
        val nn = promotion_text.text.toString()
       val pd = ProgressDialog(this)
        pd.setTitle("Importation du fichier $nn")
        pd.show()
        val name = nom_du_syllabus.text.toString()
        val descp = description.text.toString()
        val nameProf = nom_du_prof.text.toString()
        val sys = System.currentTimeMillis()
        val name_save_sta = "livres/$nn/${name}$sys.pdf"
        val name_cover = "couverture/$nn/$name$sys.png"
        val link_cover = "https://firebasestorage.googleapis.com/v0/b/e-learning-e8097.appspot.com/o/pdf_file_esis.png?alt=media&token=4b010801-6e61-4420-8359-a4f0e8d12a21"
        val reference = storageReference.child(name_save_sta)
       // val id_poste = databaseReference.push().key!!.toString()
        reference.putFile(filepath!!)
            .addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                    if (cover_path != null){
                        val ref = storageReference.child(name_cover)
                            ref.putFile(cover_path!!)
                                .addOnCompleteListener{ it ->
                                    if (it.isSuccessful){
                                        ref.downloadUrl.addOnSuccessListener { lien:Uri ->
                                            //donnee a recuperer
                                            val book = hashMapOf<String,Any>(
                                                "nom_syllabus" to name,
                                                "admin_assistant" to admin_as,
                                                "administrateur" to admin,
                                                "pochette" to lien.toString(),
                                                "mail_users" to mail_users,
                                                "id_user" to id_users,
                                                "lien_pdf" to name_save_sta,
                                                "token_users" to token_id,
                                                "nom_promotion" to promotion_text.text.toString(),
                                                "description" to descp,
                                                "nom_prof" to nameProf,
                                                "lien_du_livre" to uri.toString(),
                                                "nom_user" to mon_nom,
                                                "date_heure" to currentDate,
                                                "lien_profil" to lien_image,
                                                "id_reserve1" to "",
                                                "id_reserve2" to "",
                                                "id_reserve3" to "",
                                                "id_reserve4" to "",
                                                "like" to 0,
                                                "comment" to 0,
                                                "download" to 0,
                                                "id_book" to document,
                                            )
                                            val db = Firebase.firestore
                                            db.collection("syllabus").document(document).set(book).addOnCompleteListener {task->
                                                if (task.isSuccessful){
                                                    // Toast.makeText(this, "compte creer", Toast.LENGTH_SHORT).show()
                                                    pd.dismiss()
                                                    _increment_data()
                                                    Toast.makeText(applicationContext, "Syllabus publier", Toast.LENGTH_LONG).show()
                                                    sendnotif(promotion_text.text.toString())
                                                }else{
                                                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                        }
                                    }else{
                                        Toast.makeText(this, "erreur: ${it.exception}", Toast.LENGTH_SHORT).show()

                                    }
                                }
                    }else{
                        //donnee a recuperer

                        val book = hashMapOf<String,Any>(
                            "nom_syllabus" to name,
                            "admin_assistant" to admin_as,
                            "administrateur" to admin,
                            "pochette" to link_cover,
                            "mail_users" to mail_users,
                            "id_user" to id_users,
                            "lien_pdf" to name_save_sta,
                            "token_users" to token_id,
                            "nom_promotion" to promotion_text.text.toString(),
                            "description" to descp,
                            "nom_prof" to nameProf,
                            "lien_du_livre" to uri.toString(),
                            "nom_user" to mon_nom,
                            "date_heure" to currentDate,
                            "lien_profil" to lien_image,
                            "id_reserve1" to "",
                            "id_reserve2" to "",
                            "id_reserve3" to "",
                            "id_reserve4" to "",
                            "like" to 0,
                            "comment" to 0,
                            "download" to 0,
                            "id_book" to document,
                        )
                        val db = Firebase.firestore
                        db.collection("syllabus").document(document).set(book).addOnCompleteListener {
                            if (it.isSuccessful){
                                pd.dismiss()
                                _increment_data()
                                Toast.makeText(applicationContext, "Syllabus publier", Toast.LENGTH_LONG).show()
                                sendnotif(promotion_text.text.toString())
                                // save_syllabus_mprfl(uri.toString(),name,id_poste,promotion_text.text.toString())
                            }else{
                                Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                            }
                        }


                    }


                }
            }
            .addOnFailureListener{
                Log.d("erreur importation","${it.message}")
                Toast.makeText(this, "importation echouer ${it.message}", Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val percent =
                    (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                pd.setMessage("Importation: " + percent.toInt() + "%")
                pourc.text = "${percent.toInt()}%"
            }

    }

    fun _increment_data(){
        val db = FirebaseFirestore.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val increment = hashMapOf<String,Any>(
            "add_syllabus_count" to FieldValue.increment(1),
        )
        db.collection(collection_name).document(mail)
            .set(increment, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "nombre incrementer", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Add_book","erreur:${it.exception}")
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
    fun sendnotif(msg:String) {
        FcmNotificationsSender.pushNotification(
            this,
            "/topics/syllabus",
            getString(R.string.app_name),
            "${mon_nom} a publier un syllabus de $msg",
        )
    }
    /*
    fun save_syllabus_mprfl(lien:String, nom_livre:String, id_post:String,promo:String){
        val firebaseUser = firebaseAuth.currentUser
        var mail = firebaseUser?.email.toString().replaceAfter("@"," ")
        if (mail.contains(".")){
            mail =  mail.replace(".","")
        }
        val data = save_profil_syllabus(lien,nom_livre,id_post,promo,"","","")
        databaseReference = FirebaseDatabase.getInstance().getReference("syllabus_poste_save")
        databaseReference.child(mail).child(id_post).setValue(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }
    */
    fun save_toke(){
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("token_id", token_id)
        }.apply()

    }
}