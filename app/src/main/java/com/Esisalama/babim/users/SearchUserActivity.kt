package com.Esisalama.babim.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.Esisalama.babim.R
import com.Esisalama.babim.Utils.Constant
import com.Esisalama.babim.admin.adpters.All_User_adapter
import com.Esisalama.babim.admin.model.modeluser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.activity_syllabus_promo.*

class SearchUserActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var currentuser: FirebaseUser? = null

    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        auth = Firebase.auth
        db = Firebase.firestore
        currentuser = auth.currentUser

        val user_adapter = All_User_adapter()

        users_recycl.apply {
            linearLayoutManager = LinearLayoutManager(this@SearchUserActivity)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.onSaveInstanceState()
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            adapter = user_adapter
        }

        val users = mutableListOf<modeluser>()
        val query = db.collection(Constant.Etudiant)
        query.addSnapshotListener { value, error ->
            if (error!= null){
                return@addSnapshotListener
            }
            for (document in value!!.documents){


                val nom_user = document.getString("nom_user").toString()
                val date_push = document.getString("date_heure").toString()


            }
            user_adapter.items = users

        }

        search_user.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //syllabusAdaptersNew.filter.filter(s.toString())
                user_adapter.getFilter().filter(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }
}