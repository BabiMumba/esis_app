package com.Esisalama.babim.fragment

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
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.home.PublishPost
import com.Esisalama.babim.model.post_model
import com.Esisalama.babim.adapters.post_adapters
import com.airbnb.lottie.LottieAnimationView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
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
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep, Context.MODE_PRIVATE)
        val prenom = sharedPreferences.getString("prenom",null)

        txt.text = "Quoi de neuf $prenom ?"

        add_btn.setOnClickListener {
            startActivity(Intent(activity, PublishPost::class.java))
        }
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true

        val ref = FirebaseFirestore.getInstance().collection("poste_forum")
        recyclerv.layoutManager = linearLayoutManager
        val options = FirestoreRecyclerOptions.Builder<post_model>()
            .setQuery(
                ref,
                post_model::class.java
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