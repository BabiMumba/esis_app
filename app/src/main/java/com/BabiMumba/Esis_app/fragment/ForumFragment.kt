package com.BabiMumba.Esis_app.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.PublishPost
import com.BabiMumba.Esis_app.model.commnunique_model
import com.BabiMumba.Esis_app.adapters.post_adapters
import com.airbnb.lottie.LottieAnimationView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_forum.*


class ForumFragment : Fragment() {

    lateinit var communiqueAdapters: post_adapters
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewf = inflater.inflate(R.layout.fragment_forum, container, false)

        if (isConnectedNetwork(requireActivity().applicationContext)){
            //Toast.makeText(activity, "connecter", Toast.LENGTH_SHORT).show()
        }else{
            viewf.findViewById<RelativeLayout>(R.id.r_all).visibility = View.GONE

            viewf.findViewById<LottieAnimationView>(R.id.non_internet).visibility = View.VISIBLE

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Verifier votre connexion puis reesayer", Snackbar.LENGTH_SHORT).show();
        }

        val recyclerv = viewf.findViewById<RecyclerView>(R.id.recycler_frg)
        val add_btn = viewf.findViewById<RelativeLayout>(R.id.r1)
        val txt = viewf.findViewById<TextView>(R.id.txt1)
        val sharedPreferences = requireActivity().getSharedPreferences("info_users", Context.MODE_PRIVATE)
        val prenom = sharedPreferences.getString("prenom",null)

        txt.text = "Quoi de neuf $prenom ?"

        add_btn.setOnClickListener {
            startActivity(Intent(activity, PublishPost::class.java))
        }
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true

        recyclerv.layoutManager = linearLayoutManager
        val options = FirebaseRecyclerOptions.Builder<commnunique_model>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("forum_discussion"),
                commnunique_model::class.java
            )
            .build()
        communiqueAdapters = post_adapters(options)
        recyclerv.adapter = communiqueAdapters
        communiqueAdapters.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        communiqueAdapters.startListening()
        return viewf
    }

    override fun onStart() {
        super.onStart()
        recycler_frg.recycledViewPool.clear()
        communiqueAdapters.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        communiqueAdapters.stopListening()
    }

    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}