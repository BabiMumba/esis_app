package com.BabiMumba.Esis_app.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.*
import com.BabiMumba.Esis_app.users.ProfilUser
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
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
        val imageList = ArrayList<SlideModel>() // Create image list
        /*
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_genie_log.png", "reseau informatique"))
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_design.png", "DEsign est multimedia"))
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_design.png", "DEsign est multimedia"))
         */
        //new image upluade for firebase database
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/idee.png?alt=media&token=7cdc2d1d-45c5-464a-8413-c0523835ed45", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/affich_1.png?alt=media&token=2e477ae3-c4fc-4377-87d7-a8935ce3e6b4", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/gerer%20syllabus.png?alt=media&token=525fd196-63c3-4379-bb82-d9fd4eb8ca4a", "Gestion des syllabus"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/resultat_affiche.png?alt=media&token=f5f6c8df-d22a-4e7e-bd8a-0617a6a74cf2", "Sauvegarder vos resultat en pdf"))

        val imageSlider = viewF.findViewById<ImageSlider>(R.id.image_slider)
        /*
         imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                Toast.makeText(requireActivity(), "n $position", Toast.LENGTH_SHORT).show()
            }
        })
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here.
            }
        })
         */

        imageSlider.setImageList(imageList)

        viewF.findViewById<CardView>(R.id.catL1).setOnClickListener {
            val intent = Intent(activity, SyllabusPromo::class.java)
            intent.putExtra("promotion","L1")
            startActivity(intent)
        }
        viewF.findViewById<CardView>(R.id.catL2).setOnClickListener {
            val intent = Intent(activity, SyllabusPromo::class.java)
            intent.putExtra("promotion","L2")
            startActivity(intent)
        }
        viewF.findViewById<CardView>(R.id.cat_tous).setOnClickListener {
            val intent = Intent(activity, SyllabusPromo::class.java)
            intent.putExtra("promotion","Tous")
            startActivity(intent)
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
        viewF.findViewById<CardView>(R.id.lecture).setOnClickListener {
            startActivity(Intent(activity, LectureActivity_Pdf::class.java))
        }
        val sharedPreferences = requireActivity().getSharedPreferences("info_users", Context.MODE_PRIVATE)
        viewF.findViewById<TextView>(R.id.prenom).text = sharedPreferences.getString("prenom",null)
        viewF.findViewById<ImageView>(R.id.profile_image).setOnClickListener {
            startActivity(Intent(activity, ProfilUser::class.java))
        }

        Glide
            .with(requireActivity())
            .load(sharedPreferences.getString("lien profil",null))
            // .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_baseline_person_pin_24)
            .into(viewF.findViewById(R.id.profile_image))

        return viewF
    }

}