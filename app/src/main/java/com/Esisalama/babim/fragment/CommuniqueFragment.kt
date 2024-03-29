package com.Esisalama.babim.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.adapters.NewsAdapters
import com.Esisalama.babim.model.news_model
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_communique.*

class CommuniqueFragment : Fragment() {
    lateinit var layoutManager: LinearLayoutManager
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var newsAda: NewsAdapters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        layoutManager = LinearLayoutManager(requireActivity())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        val view = inflater.inflate(R.layout.fragment_communique, container, false)
        val recy = view.findViewById<RecyclerView>(R.id.news_recyler)
        recy.layoutManager = layoutManager

        val sharedPreferences = requireActivity().getSharedPreferences(Constant.Save_to_sharep,Context.MODE_PRIVATE)
        val promot = sharedPreferences.getString("promotion","")

        val ref = FirebaseFirestore.getInstance().collection("communique")
        val option = FirestoreRecyclerOptions.Builder<news_model>()
            .setQuery(
                ref,
                news_model::class.java
            )
            .build()
        newsAda = NewsAdapters(option)
        recy.adapter = newsAda
        return view
    }

    override fun onStart() {
        newsAda.startListening()
        news_recyler.recycledViewPool.clear()
        newsAda.notifyDataSetChanged()
        super.onStart()
    }
    override fun onStop() {
        newsAda.stopListening()
        super.onStop()
    }


}