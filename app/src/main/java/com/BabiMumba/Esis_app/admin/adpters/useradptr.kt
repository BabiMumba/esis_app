package com.BabiMumba.Esis_app.admin.adpters


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.Utils.Constant
import com.BabiMumba.Esis_app.admin.model.modeluser
import com.BabiMumba.Esis_app.home.InfosActivity
import com.BabiMumba.Esis_app.home.PosteDetaille
import com.BabiMumba.Esis_app.model.newsyllabus_model
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.hdodenhof.circleimageview.CircleImageView

class useradptr (options: FirestoreRecyclerOptions<modeluser>):
    FirestoreRecyclerAdapter<modeluser,useradptr.viewholder>(options) {
    inner  class  viewholder(item: View):RecyclerView.ViewHolder(item){

        var image:CircleImageView
        var name:TextView
        var mail:TextView
        var admin:TextView
        var rela_id:RelativeLayout

        init {
            image = item.findViewById(R.id.image_profil_rod)
            name = item.findViewById(R.id.name_user)
            mail = item.findViewById(R.id.mail_user)
            admin = item.findViewById(R.id.admin_v)
            rela_id = item.findViewById(R.id.rela_id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): useradptr.viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_rod,parent, false)
        return viewholder(v)
    }


    override fun onBindViewHolder(holder: useradptr.viewholder, position: Int, model: modeluser) {
        val sharedPreferences = holder.image.context.getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        //val mail_cach = sharedPreferences.getString("mail",null)
        val admin = sharedPreferences.getString("administrateur",null)

        holder.name.text = model.prenom
        holder.mail.text = model.mail

        //holder.itemView.setBackgroundColor(Color.parseColor("#00000"))
        //holder.itemView.visibility = if (model.mail==mail_cach) View.GONE else  View.VISIBLE
        holder.admin.visibility = if (model.admin_assistant == "oui") View.VISIBLE else View.GONE

        val circularProgressDrawable = CircularProgressDrawable(holder.image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(holder.itemView.context)
            .load(model.profil)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            //.apply(RequestOptions.overrideOf(300,600))
            .centerInside()
            .placeholder(circularProgressDrawable)
            .into(holder.image)

        //boite de dialogue

        holder.itemView.setOnClickListener {
            if (admin == "oui"){
                val dialog = BottomSheetDialog(holder.image.context)
                dialog.setContentView(R.layout.bottom_sheet)
                val show_profil = dialog.findViewById<LinearLayout>(R.id.profile_user)
                val add_admin = dialog.findViewById<LinearLayout>(R.id.admin_add)
                val text_ad = dialog.findViewById<TextView>(R.id.text_add)
                if (model.admin_assistant == "oui"){
                    text_ad!!.text = "Retirer en tant qu'administrateur ass."
                }
                add_admin?.setOnClickListener {
                    if (model.admin_assistant == "oui"){
                        //Toast.makeText(holder.image.context, "il est deja administateur", Toast.LENGTH_SHORT).show()
                        val database = FirebaseFirestore.getInstance()
                        val infor_user:MutableMap<String, Any> = HashMap()
                        infor_user["admin_assistant"] = "non"
                        database.collection(Constant.Etudiant)
                            .document(model.mail)
                            .set(infor_user, SetOptions.merge())
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(holder.image.context, "modification reussi", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(holder.image.context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        val nom = model.prenom
                        val alertDialog = AlertDialog.Builder(holder.image.context).create()
                        alertDialog.setTitle("Administrateur assistant")
                        alertDialog.setMessage("""
                            en ajoutant $nom en tant qu'administrateur assistant il aura la possibilite de \n
                            a)publier des syllabus
                            b)publier un communique
                            c)Supprimer un syllabus
                      
                        """.trimIndent())
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"ajouter"){
                                d: DialogInterface, _:Int ->
                            val database = FirebaseFirestore.getInstance()
                            val infor_user:MutableMap<String, Any> = HashMap()
                            infor_user["admin_assistant"] = "oui"
                            database.collection(Constant.Etudiant)
                                .document(model.mail)
                                .set(infor_user, SetOptions.merge())
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        Toast.makeText(holder.image.context, "modification reussi", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(holder.image.context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            d.dismiss()
                        }
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"annuler"){
                                d: DialogInterface, _:Int ->
                            d.dismiss()
                        }

                        alertDialog.show()

                    }

                    dialog.dismiss()
                }
                show_profil!!.setOnClickListener {
                    /*
                      val dialog_profi = Dialog(holder.image.context)
                    dialog_profi.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog_profi.setContentView(R.layout.inforamtion_user_dialgue)
                    dialog_profi.setCancelable(true)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog_profi.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    val profil_us = dialog_profi.findViewById<ImageView>(R.id.pro_i)
                    val promotion = dialog_profi.findViewById<TextView>(R.id.pm)
                    val genre = dialog_profi.findViewById<TextView>(R.id.sx)
                    val nom = dialog_profi.findViewById<TextView>(R.id.nm)
                    nom.text = model.mail
                    promotion.text = model.promotion
                    genre.text = model.sexe
                    Glide
                        .with(holder.itemView.context)
                        .load(model.profil)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        //.apply(RequestOptions.overrideOf(300,600))
                        .centerInside()
                        .placeholder(circularProgressDrawable)
                        .into(profil_us)
                    dialog_profi.show()
                    dialog_profi.window!!.attributes = lp
                    dialog.dismiss()
                     */
                    val intent = Intent(holder.itemView.context, InfosActivity::class.java)
                    intent.putExtra("mail",model.mail)
                    intent.putExtra("admin",model.administrateur)
                    holder.itemView.context.startActivity(intent)

                }
                dialog.show()
            }else{
                val intent = Intent(holder.itemView.context, InfosActivity::class.java)
                intent.putExtra("mail",model.mail)
                holder.itemView.context.startActivity(intent)

            }

        }

    }


}