package com.example.networkingapp.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.util.Patterns
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.activities.TinderActivity
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.fragments.ProfileFragment
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.activity_basic_info.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

class BasicInfoActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)


        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Basic Info"


        populateBasicInfo()

        nameET.requestFocus()

        if(nameET.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        profileChanges.setOnClickListener {

            var string = nameET.text.toString()
            var string1 = professionET.text.toString()
            var string2 = locationET.text.toString()


            if (string.length == 0) {

                nameET.error = "Please enter your name"

            } else if (string1.length == 0) {

                professionET.error = "Please enter your profession"

            } else if (string2.length == 0) {
                locationET.error = "Please enter your location"
            }

            else {

                database.child(userId!!).child("name").setValue(string)
                database.child(userId!!).child("profession").setValue(string1)
                database.child(userId!!).child("location").setValue(string2)


                closeActivity(string, string1, string2)

            }


        }

    }

    fun populateBasicInfo() {

        database.child(userId!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                nameET.setText(user?.name, TextView.BufferType.EDITABLE)
                professionET.setText(user?.profession, TextView.BufferType.EDITABLE)
                locationET.setText(user?.location, TextView.BufferType.EDITABLE)

                nameET.setSelection(nameET.length())
            }

        })

    }

    private fun closeActivity(name: String, profession: String, location: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_NAME, name)
        resultIntent.putExtra(INPUT_PROFESSION, profession)
        resultIntent.putExtra(INPUT_LOCATION, location)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_NAME = "TEST"
        val INPUT_PROFESSION = "TESTTEST"
        val INPUT_LOCATION = "TESTTESTTEST"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
