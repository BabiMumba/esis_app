package com.BabiMumba.Esis_app.fragment

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
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.admin.AllusersActivity
import com.BabiMumba.Esis_app.admin.SaleControleActivity
import com.BabiMumba.Esis_app.home.AboutDeveloppeur
import com.BabiMumba.Esis_app.home.FeedBack
import com.BabiMumba.Esis_app.home.InfosActivity
import com.BabiMumba.Esis_app.home.ThankActivity
import com.BabiMumba.Esis_app.users.DeleteCount
import com.BabiMumba.Esis_app.users.ProfilUser
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging


class ProfilFragment : Fragment() {

    var mm = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profil, container, false)
            readData(v)
            checkstate(v)
            clickmethode(v)
        return v
    }
    private fun readData(view: View){
        val sharedPreferences = requireActivity().getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("post-nom",null)
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
            startActivity(Intent(requireActivity(), ThankActivity::class.java))

        }

        view.findViewById<RelativeLayout>(R.id.rr4).setOnClickListener {
            if (isConnectedNetwork(requireActivity())){
                val intent = Intent(requireActivity(), InfosActivity::class.java)
                intent.putExtra("mail",mm)
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
    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

}