package com.Esisalama.babim.fragment

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
import androidx.cardview.widget.CardView
import com.Esisalama.babim.HoraireViewActivity
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.home.*
import com.Esisalama.babim.R
import com.Esisalama.babim.users.ProfilUser
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.ads.*
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private companion object{
        private const val TAG = "BANNER_AD_TAG"
    }
    private var adview: AdView? = null

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        val viewF = inflater.inflate(R.layout.fragment_home, container, false)

        MobileAds.initialize(requireActivity()){
            Log.d(TAG,"inias complet")
        }
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("f01a4b37-2568-4128-9894-6d6453fd67bb"))
                .build()
        )
        adview = viewF.findViewById(R.id.bannerAd)
        val adRequest = AdRequest.Builder().build()

        adview?.loadAd(adRequest)
        adview?.adListener = object : AdListener() {
            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked: ")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.d(TAG, "onAdClosed: ")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailedToLoad: $p0")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "onAdImpression: ")
                //Toast.makeText(requireActivity() , "impression recu", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "onAdLoaded: ")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.d(TAG, "onAdOpened: ")
            }
        }

        val imageList = ArrayList<SlideModel>() // Create image list
        /*
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_genie_log.png", "reseau informatique"))
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_design.png", "DEsign est multimedia"))
        imageList.add(SlideModel("https://www.esisalama.com/assets/img/carousel/banner_design.png", "DEsign est multimedia"))
         */
        //new image upluade for firebase database

        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/n1-1.jpg?alt=media&token=17862754-5cad-4b88-91e6-f6022de3e9cf", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/genie_logiciel.jpg?alt=media&token=ab621f1f-13e6-404f-8d97-4f201f507296", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/design_multi.jpg?alt=media&token=3c2b7c9c-47a2-4f41-9093-f009d295a057", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/reseau_info.jpg?alt=media&token=fd78e54c-ece4-474e-a09a-7b341ef10ad5", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/n2-1.jpg?alt=media&token=dea51bc4-4dd8-4483-9ee5-7b44f277e8af", ""))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/media-4dbe5.appspot.com/o/FILLE2.jpg?alt=media&token=3d6415b4-1fa5-4795-ab0f-2769f76bd036", ""))

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
            val intent = Intent(activity, SyllabusViewer::class.java)
            intent.putExtra("promotion","L2")
            startActivity(intent)
        }
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val pm = sharedPreferences.getString("promotion","")
        val admin = sharedPreferences.getString("administrateur","")
        if(admin !="non"){
            viewF.findViewById<CardView>(R.id.cat_tous).visibility = View.GONE

        }

        viewF.findViewById<CardView>(R.id.cat_tous).setOnClickListener {
            val intent = Intent(activity, SyllabusPromo::class.java)
           intent.putExtra("promotion",pm)
            startActivity(intent)
        }

        //si c'est un etudiant

        viewF.findViewById<TextView>(R.id.titre2).text = "Horaire\n$pm"
        viewF.findViewById<TextView>(R.id.promo_syl).text = "$pm"



            viewF.findViewById<CardView>(R.id.horaire_cat).setOnClickListener {
                if (admin=="non") {
                    //val actulaity_link = "https://www.esisalama.com/index.php?module=horaire"
                    val intent = Intent(activity, HoraireViewActivity::class.java)
                    intent.putExtra("promot_link",sharedPreferences.getString("promotion",null))
                    //intent.putExtra("url_link",actulaity_link)
                    startActivity(intent)
                }else{
                    val actulaity_link = "https://www.esisalama.com/index.php?module=horaire"
                    val intent = Intent(activity, ActualiteActivity::class.java)
                    // intent.putExtra("promot_link",sharedPreferences.getString("promotion",null))
                    intent.putExtra("url_link",actulaity_link)
                    startActivity(intent)
                }

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