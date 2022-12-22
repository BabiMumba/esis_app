package com.BabiMumba.Esis_app.fragment.tab

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.BabiMumba.Esis_app.R
import com.BabiMumba.Esis_app.adapters.syllabus_adapters
import com.BabiMumba.Esis_app.home.PublicationSyllabus
import com.BabiMumba.Esis_app.model.syllabus_model
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_publication_syllabus.*
import kotlinx.android.synthetic.main.fragment_tous.*

class TousFragment : Fragment() {

    lateinit var myadaptes_syllabus: syllabus_adapters
    lateinit var linearLayoutManager: LinearLayoutManager
    var choice:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val v = inflater.inflate(R.layout.fragment_tous, container, false)
        if (isConnectedNetwork(requireActivity())){

        }else{
            v.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            v.findViewById<FloatingActionButton>(R.id.add_post).visibility = View.GONE
            Toast.makeText(requireActivity(), "verifier votre connection", Toast.LENGTH_SHORT).show()
        }
        v.findViewById<FloatingActionButton>(R.id.add_post).setOnClickListener {
            startActivity(Intent(requireActivity(), PublicationSyllabus::class.java))
        }

        val recp = v.findViewById<RecyclerView>(R.id.recycler_tous)
        val sort = v.findViewById<TextView>(R.id.sort_data)

        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.onSaveInstanceState()
        linearLayoutManager.stackFromEnd = true
        val ref = FirebaseDatabase.getInstance().reference.child("syllabus").child("Tous")
        sort.setOnClickListener {
            val checkedItem = intArrayOf(-1)
            val alertDialog = AlertDialog.Builder(requireActivity())
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
                choice = s
                dialog.dismiss()
            }
            alertDialog.setNegativeButton("Annuler") { dialog, which ->
                dialog.dismiss()
            }
            val customAlertDialog = alertDialog.create()
            customAlertDialog.show()
        }
        ref.orderByChild("nom_promotion").equalTo("$choice").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
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
                    Toast.makeText(requireActivity(), "il existe", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(requireActivity(), "donee no trouver", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), "$error", Toast.LENGTH_SHORT).show()
            }

        })
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

        return v
    }
    override fun onStart() {
        recycler_tous.recycledViewPool.clear()
        myadaptes_syllabus.notifyDataSetChanged()
        super.onStart()
    }
    fun isConnectedNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

    override fun onDestroy() {
        super.onDestroy()
        myadaptes_syllabus.stopListening()
    }
}