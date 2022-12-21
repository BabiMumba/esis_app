package com.BabiMumba.Esis_app.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.home.PublishPost
import com.BabiMumba.Esis_app.model.commnunique_model
import com.BabiMumba.Esis_app.adapters.communique_adapters
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_forum.*


class ForumFragment : Fragment() {

    lateinit var communiqueAdapters: communique_adapters
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewf = inflater.inflate(R.layout.fragment_forum, container, false)

        if (isConnectedNetwork(requireActivity().applicationContext)){
            Toast.makeText(activity, "connecter", Toast.LENGTH_SHORT).show()
        }else{
            viewf.findViewById<FrameLayout>(R.id.frame_id).visibility = View.GONE
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Verifier votre connexion puis reesayer", Snackbar.LENGTH_SHORT).show();
        }

        val recyclerv = viewf.findViewById<RecyclerView>(R.id.recycler_frg)
        val add_btn = viewf.findViewById<FloatingActionButton>(R.id.add_post)

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
                FirebaseDatabase.getInstance().reference.child("forum"),
                commnunique_model::class.java
            )
            .build()
        communiqueAdapters = communique_adapters(options)
        recyclerv.adapter = communiqueAdapters
        communiqueAdapters.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        communiqueAdapters.startListening()
        return viewf   }

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