package com.BabiMumba.Esis_app.fragment.tab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.syllabus_adapters
import com.BabiMumba.Esis_app.home.PublicationSyllabus
import com.BabiMumba.Esis_app.model.syllabus_model
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_prepa.*

class PrepaFragment : Fragment() {

    lateinit var myadaptes_syllabus: syllabus_adapters
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_prepa, container, false)
        val recp = v.findViewById<RecyclerView>(R.id.recycler_prepa)
        v.findViewById<FloatingActionButton>(R.id.add_post).setOnClickListener {
            startActivity(Intent(requireActivity(), PublicationSyllabus::class.java))
        }

        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true

        recp.layoutManager = linearLayoutManager
        val options = FirebaseRecyclerOptions.Builder<syllabus_model>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("syllabus").child("Preparatoire"),
                syllabus_model::class.java
            )
            .build()
        myadaptes_syllabus = syllabus_adapters(options)
        recp.adapter = myadaptes_syllabus
        myadaptes_syllabus.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        myadaptes_syllabus.startListening()

        return v
    }

    override fun onStart() {
        recycler_prepa.recycledViewPool.clear()
        myadaptes_syllabus.notifyDataSetChanged()
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        myadaptes_syllabus.stopListening()
    }
}