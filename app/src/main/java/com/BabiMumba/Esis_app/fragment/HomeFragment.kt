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
        imageList.add(SlideModel(R.drawable.banner_principale, "teste 1"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/Sans%20titre-1.png?alt=media&token=6ffdd7f1-7bf3-4e3c-8e49-18906af24c65", "teste 2"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/5317030.png?alt=media&token=5e274f2e-95a9-4ea4-bff2-ea3d4e7530cc", "teste 3"))

        val imageSlider = viewF.findViewById<ImageSlider>(R.id.image_slider)
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