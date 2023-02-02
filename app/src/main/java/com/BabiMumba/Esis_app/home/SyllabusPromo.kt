package com.BabiMumba.Esis_app.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.syllabus_adapters
import com.BabiMumba.Esis_app.model.syllabus_model
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_syllabus_promo.*

class SyllabusPromo : AppCompatActivity() {

    lateinit var myadaptes_syllabus: syllabus_adapters
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus_promo)

        val recp = findViewById<RecyclerView>(R.id.recycler_promo)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true
        val pm = intent.getStringExtra("promotion").toString()

        val ref = FirebaseDatabase.getInstance().reference.child("syllabus").child(pm)

        recp.layoutManager = linearLayoutManager
        val options = FirebaseRecyclerOptions.Builder<syllabus_model>()
            .setQuery(
                ref,
                syllabus_model::class.java
            )
            .build()
        myadaptes_syllabus = syllabus_adapters(options)
        recp.adapter = myadaptes_syllabus
        myadaptes_syllabus.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        myadaptes_syllabus.startListening()
    }
    override fun onStart() {
        recycler_promo.recycledViewPool.clear()
        myadaptes_syllabus.notifyDataSetChanged()
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        myadaptes_syllabus.stopListening()
    }
}