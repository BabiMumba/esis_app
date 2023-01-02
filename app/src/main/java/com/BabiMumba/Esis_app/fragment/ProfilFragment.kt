package com.BabiMumba.Esis_app.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.BabiMumba.Esis_app.R
import com.bumptech.glide.Glide


class ProfilFragment : Fragment() {

    var mm = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profil, container, false)

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

}