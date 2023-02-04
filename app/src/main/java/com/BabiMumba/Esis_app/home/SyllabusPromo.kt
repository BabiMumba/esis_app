package com.BabiMumba.Esis_app.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.syllabus_adapters
import com.BabiMumba.Esis_app.model.syllabus_model
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
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
        val sort = findViewById<TextView>(R.id.sort_data)

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

        sort.setOnClickListener {
            val checkedItem = intArrayOf(-1)
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.drawable.ic_baseline_sort_24)
            alertDialog.setTitle("trier par")
            val listItems = arrayOf(
                "G2 ",
                "GL",
                "G2 MSI",
                "G2 DSG",
                "G2 AS",
                "G2 TLC",
                "G3 GL",
                "G3 MSI",
                "G3 DSG",
                "G3 AS",
                "G3 TLC",
                " M1 AS-TLC",
                "M1 DESIGN",
                "M1 MIAGE"
            )
            alertDialog.setSingleChoiceItems(listItems, checkedItem[0]) { dialog, which ->
                checkedItem[0] = which
                val s = listItems[which]
                val query: Query = ref.orderByChild("nom_promotion").equalTo("$s")
                ref.orderByChild("nom_promotion").equalTo("$s").addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val t = snapshot.childrenCount
                            recp.layoutManager = linearLayoutManager
                            val options = FirebaseRecyclerOptions.Builder<syllabus_model>()
                                .setQuery(
                                    query,
                                    syllabus_model::class.java
                                )
                                .build()
                            myadaptes_syllabus = syllabus_adapters(options)
                            recp.adapter = myadaptes_syllabus
                            myadaptes_syllabus.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            myadaptes_syllabus.startListening()
                            Toast.makeText(this@SyllabusPromo, "il ya $t syllabus de $s", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this@SyllabusPromo, "donee no trouver", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SyllabusPromo, "$error", Toast.LENGTH_SHORT).show()
                    }

                })
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            val customAlertDialog = alertDialog.create()
            customAlertDialog.show()
        }


        add_post.setOnClickListener {
            startActivity(Intent(this,PublicationSyllabus::class.java))
        }
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