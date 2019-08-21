package com.example.networkingapp.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        populateWeb()

        webET.requestFocus()

        if(webET.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        profileChanges.setOnClickListener {

            var string = webET.text.toString()

            database.child(userId!!).child("web").setValue(string)

            finish()

        }

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Your Website"
    }

    fun populateWeb() {

        database.child(userId!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                webET.setText(user?.web, TextView.BufferType.EDITABLE)

                webET.setSelection(webET.length())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
