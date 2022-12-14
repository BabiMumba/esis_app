package com.BabiMumba.Esis_app.fragment

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.ActualiteActivity
import com.BabiMumba.Esis_app.home.LectureActivity_Pdf
import com.BabiMumba.Esis_app.home.ResultatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        val viewF = inflater.inflate(R.layout.fragment_home, container, false)

        viewF.findViewById<CardView>(R.id.syllabus_card).setOnClickListener {
            startActivity(Intent(activity, Syllabus_FragmentActivity::class.java))
        }
        viewF.findViewById<CardView>(R.id.horaire_cat).setOnClickListener {
            val actulaity_link = "https://www.esisalama.com/index.php?module=horaire"
            val intent = Intent(activity, ActualiteActivity::class.java)
            intent.putExtra("url_link",actulaity_link)
            startActivity(intent)
        }
        viewF.findViewById<CardView>(R.id.esis_web).setOnClickListener {
            val actulaity_link = "https://www.esisalama.com/index.php"
            val intent = Intent(activity, ActualiteActivity::class.java)
            intent.putExtra("url_link",actulaity_link)
            startActivity(intent)
        }
        viewF.findViewById<CardView>(R.id.news_card).setOnClickListener {
            val actulaity_link = "https://www.esisalama.com/index.php?module=actualite"
            val intent = Intent(activity, ActualiteActivity::class.java)
            intent.putExtra("url_link",actulaity_link)
            startActivity(intent)
        }
        viewF.findViewById<CardView>(R.id.resultat_cat).setOnClickListener {
            startActivity(Intent(activity, ResultatActivity::class.java))
        }
        viewF.findViewById<CardView>(R.id.lecture_cat).setOnClickListener {
            startActivity(Intent(activity, LectureActivity_Pdf::class.java))
        }
        readname(viewF)
        return viewF
    }
    fun readname(v:View){

        val firebaseUser = firebaseAuth.currentUser
        val mail = firebaseUser?.email.toString()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Utilisateurs").document(mail)
        docRef.get()
            .addOnSuccessListener {
                if (it!=null){
                    val pren = it.data?.getValue("prenom").toString()
                    v.findViewById<TextView>(R.id.prenom).text = pren

                }else{
                    Log.d(ContentValues.TAG,"no such document")
                }
            }

            .addOnFailureListener {
                Toast.makeText(requireActivity(), "${it}", Toast.LENGTH_SHORT).show()
            }

    }
}