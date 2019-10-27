package com.example.networkingapp.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.activities.setup.PhotoSetupActivity
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About"

        var i = intent.getIntExtra("next", 2)

        if (i == 1) {

            nextButton.visibility = View.VISIBLE
            aboutChanges.visibility = View.GONE

            progressBar.visibility = View.VISIBLE

            progressBar.progress = 75
        }

        populateAbout()

        aboutET.requestFocus()

        if(aboutET.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        aboutChanges.setOnClickListener {

            var aboutString = aboutET.text.toString()

            if (aboutString.length == 0) {

                aboutET.error = "Please enter a short biography"
            }

            else {

                database.child(userId!!).child("about").setValue(aboutString)
                closeActivity(aboutString)

            }
        }

        nextButton.setOnClickListener {

            var aboutString = aboutET.text.toString()

            if (aboutString.length == 0) {

                aboutET.error = "Please enter a short biography"
            }

            else {

                database.child(userId!!).child("about").setValue(aboutString)

                val intent = Intent(this, PhotoSetupActivity::class.java)
                intent.putExtra("nextAbout", 1)
                startActivity(intent)

            }
        }
    }

    fun populateAbout() {

        database.child(userId!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                aboutET.setText(user?.about, TextView.BufferType.EDITABLE)

                aboutET.setSelection(aboutET.length())

            }

        })
    }

    private fun closeActivity(name: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_ABOUT, name)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_ABOUT = "TESTABOUT"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
