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
import android.view.View
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


        var i = intent.getIntExtra("next", 2)

        if (i == 1) {

            nextButton.visibility = View.VISIBLE
            saveChanges.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            progressBar.progress = 15
        }


        populateBasicInfo()

        firstNameET.requestFocus()

        if(firstNameET.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        saveChanges.setOnClickListener {

            val string = firstNameET.text.toString()
            val string1 = lastNameET.text.toString()
            val string2 = professionET.text.toString()


            if (string.length == 0) {

                firstNameET.error = "Please enter your first firstName"

            } else if (string1.length == 0) {

                professionET.error = "Please enter your last firstName"

            } else if (string2.length == 0) {

                professionET.error = "Please enter your profession"
            }

            else {

                database.child(userId!!).child("firstName").setValue(string)
                database.child(userId!!).child("lastName").setValue(string1)
                database.child(userId!!).child("location").setValue(string2)

                closeActivity(string, string1, string2)
            }
        }

        nextButton.setOnClickListener {

            val string = firstNameET.text.toString()
            val string1 = lastNameET.text.toString()
            val string2 = professionET.text.toString()


            if (string.length == 0) {

                firstNameET.error = "Please enter your first firstName"

            } else if (string1.length == 0) {

                lastNameET.error = "Please enter your last firstName"

            } else if (string2.length == 0) {

                professionET.error = "Please enter your location"
            }

            else {

                database.child(userId!!).child("firstName").setValue(string)
                database.child(userId!!).child("lastName").setValue(string1)
                database.child(userId!!).child("profession").setValue(string2)

                val intent = Intent(this, InterestsActivity::class.java)
                intent.putExtra("next", 1)
                startActivity(intent)
            }
        }
    }

    fun populateBasicInfo() {

        database.child(userId!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val user = p0.getValue(User::class.java)

                firstNameET.setText(user?.firstName, TextView.BufferType.EDITABLE)
                lastNameET.setText(user?.lastName, TextView.BufferType.EDITABLE)
                professionET.setText(user?.profession, TextView.BufferType.EDITABLE)

                firstNameET.setSelection(firstNameET.length())
            }
        })
    }

    private fun closeActivity(name: String, lastName: String, profession: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_NAME, name)
        resultIntent.putExtra(INPUT_LASTNAME, lastName)
        resultIntent.putExtra(INPUT_PROFESSION, profession)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_NAME = "TEST"
        val INPUT_PROFESSION = "TESTTEST"
        val INPUT_LASTNAME = "TESTTESTTEST"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
