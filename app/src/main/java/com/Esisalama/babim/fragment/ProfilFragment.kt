package com.Esisalama.babim.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.admin.AllusersActivity
import com.Esisalama.babim.admin.SaleControleActivity
import com.Esisalama.babim.home.*
import com.Esisalama.babim.users.DeleteCount
import com.Esisalama.babim.users.ProfilUser
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging


class ProfilFragment : Fragment() {

    var mm = ""
    lateinit var collection_name:String
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profil, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
            readData(v)
            checkstate(v)
            clickmethode(v)
        return v
    }
    private fun readData(view: View){
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("post_nom",null)
        val num = sharedPreferences.getString("numero de telephone",null)
        val prenoms = sharedPreferences.getString("prenom",null)
        val mail = sharedPreferences.getString("mail",null)
        val imgetxt = sharedPreferences.getString("lien profil",null)
        view.findViewById<TextView>(R.id.txt2).text = "+243 $num"
        view.findViewById<TextView>(R.id.txt1).text = "$prenoms $name"
        mm=mail.toString()

        val circularProgressDrawable = CircularProgressDrawable(requireActivity())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(this)
            .load(imgetxt)
            // .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(circularProgressDrawable)
            .into(view.findViewById(R.id.p1))

    }
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun clickmethode(view: View){

        view.findViewById<RelativeLayout>(R.id.rr9).setOnClickListener {
            startActivity(Intent(requireActivity(), SaleControleActivity::class.java))
        }
        view.findViewById<RelativeLayout>(R.id.user_get).setOnClickListener {
            startActivity(Intent(requireActivity(), AllusersActivity::class.java))
        }
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
        val adm = sharedPreferences.getString("administrateur",null)
        val adm2 = sharedPreferences.getString("admin_assistant",null)
        collection_name = if (adm == "oui"){
            Constant.Admin
        }else{
            Constant.Etudiant
        }

        if (adm2 == "oui"){
            view.findViewById<RelativeLayout>(R.id.rr9).visibility = View.VISIBLE
        }
        view.findViewById<RelativeLayout>(R.id.actualise_count).setOnClickListener {
            val fireuser= firebaseAuth.currentUser
            val mail = fireuser?.email.toString()
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection(collection_name)
                .document(mail)
            docRef.get()
                .addOnSuccessListener {
                    if (it!= null){
                        val admin = it.data?.getValue("admin_assistant").toString()
                        if (admin=="oui"){
                            val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.apply() {
                                putString("admin_assistant", "oui")
                            }.apply()
                            Toast.makeText(requireActivity(), "vous êtes désormais un administrateur", Toast.LENGTH_SHORT).show()
                        }else{
                            val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.apply() {
                                putString("admin_assistant", "non")
                            }.apply()
                        }
                    }
                    Toast.makeText(requireActivity(), "compte actualiser", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        view.findViewById<RelativeLayout>(R.id.rr7).setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
                .setTitle("suppression de compte")
                .setMessage("Vous etes sur le point de supprimer toute vos donnez y compris vos syllabus telecharger")
                .setPositiveButton("supprimer") { dialogInterface, i ->
                    startActivity(Intent(requireActivity(), DeleteCount::class.java))
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Annuler") { dialogInterface, i ->
                    Toast.makeText(requireActivity(), "annuler", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                }
            val customAlertDialog = builder.create()
            customAlertDialog.show()
        }
        view.findViewById<TextView>(R.id.md).setOnClickListener {
            startActivity(Intent(requireActivity(), ProfilUser::class.java))
        }
        view.findViewById<RelativeLayout>(R.id.rr5).setOnClickListener {
            startActivity(Intent(requireActivity(), AboutDeveloppeur::class.java))
        }
        view.findViewById<RelativeLayout>(R.id.rr6).setOnClickListener {
            startActivity(Intent(requireActivity(), FeedBack::class.java).putExtra("mail",mm))

        }
        view.findViewById<RelativeLayout>(R.id.rr10).setOnClickListener {
            startActivity(Intent(requireActivity(), PublishPost::class.java))

        }

        view.findViewById<RelativeLayout>(R.id.rr4).setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
            val admin = sharedPreferences.getString("administrateur",null)
            if (isConnectedNetwork(requireActivity())){
                val intent = Intent(requireActivity(), InfosActivity::class.java)
                intent.putExtra("mail",mm)
                intent.putExtra("admin",admin)
                startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), "probleme de connexion ", Toast.LENGTH_SHORT).show()
            }

        }
        val horaire = view.findViewById<Switch>(R.id.s1)
        horaire.setOnClickListener {
            if (horaire.isChecked) {
                // When switch checked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("value", true)
                editor.apply()
                horaire.isChecked = true
                abonnement("horaire")
            } else {
                // When switch unchecked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("value", false)
                editor.apply()
                horaire.isChecked = false
                desabonnement("horaire")
            }
        }
        val syllabus = view.findViewById<Switch>(R.id.syllabus)

        syllabus.setOnClickListener {
            if (syllabus.isChecked) {
                // When switch checked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("syllabus_state", true)
                editor.apply()
                syllabus.isChecked = true
                abonnement("syllabus")
            } else {
                // When switch unchecked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("syllabus_state", false)
                editor.apply()
                syllabus.isChecked = false
                desabonnement("syllabus")
            }

        }
        val resultat_id = view.findViewById<Switch>(R.id.resultat_id)
        resultat_id.setOnClickListener {
            if (resultat_id.isChecked) {
                // When switch checked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("resultat_state", true)
                editor.apply()
                resultat_id.isChecked = true
                abonnement("resultat")
            } else {
                // When switch unchecked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("resultat_state", false)
                editor.apply()
                resultat_id.isChecked = false
                desabonnement("resultat")
            }

        }
        val forum_notif = view.findViewById<Switch>(R.id.forum_notif)
        forum_notif.setOnClickListener {
            if (forum_notif.isChecked) {
                // When switch checked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("forum_state", true)
                editor.apply()
                forum_notif.isChecked = true
                abonnement("forum")
            } else {
                // When switch unchecked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("forum_state", false)
                editor.apply()
                forum_notif.isChecked = false
                desabonnement("forum")
            }

        }
        val commique_nof = view.findViewById<Switch>(R.id.commiq_notif)
        commique_nof.setOnClickListener {
            if (commique_nof.isChecked) {
                // When switch checked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("cmmq_state", true)
                editor.apply()
                commique_nof.isChecked = true
                abonnement("communique")
            } else {
                // When switch unchecked
                val editor = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE).edit()
                editor.putBoolean("cmmq_state", false)
                editor.apply()
                commique_nof.isChecked = false
                desabonnement("communique")
            }

        }


    }
    private fun abonnement(nom:String){
        //  FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic(nom).addOnSuccessListener {
            Toast.makeText(
                requireActivity(),
                "notification $nom active",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun desabonnement(nom:String){
        //  FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().unsubscribeFromTopic(nom).addOnSuccessListener {
            Toast.makeText(
                requireActivity(),
                "notification $nom desactiver",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    private fun checkstate(view: View){
        val sharedPreferences = requireActivity().getSharedPreferences("save", AppCompatActivity.MODE_PRIVATE)
        view.findViewById<Switch>(R.id.s1).isChecked = sharedPreferences.getBoolean("value", false)
        view.findViewById<Switch>(R.id.syllabus).isChecked = sharedPreferences.getBoolean("syllabus_state",false)
        view.findViewById<Switch>(R.id.resultat_id).isChecked = sharedPreferences.getBoolean("resultat_state",false)
        view.findViewById<Switch>(R.id.forum_notif).isChecked = sharedPreferences.getBoolean("forum_state",false)
        view.findViewById<Switch>(R.id.commiq_notif).isChecked = sharedPreferences.getBoolean("cmmq_state",false)
    }

    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

}