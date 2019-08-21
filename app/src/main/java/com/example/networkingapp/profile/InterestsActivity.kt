package com.example.networkingapp.profile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_instagram.*
import kotlinx.android.synthetic.main.activity_interests.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.networkingapp.R
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import android.view.KeyEvent.KEYCODE_BACK
import java.util.*
import kotlin.collections.ArrayList
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.fragment.app.Fragment
import com.example.networkingapp.fragments.ProfileFragment


class InterestsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var interestDatabase: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid


    var viewCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        interestDatabase = database.child(userId!!).child("interestedIn")

        populateInterests()

        interestDatabase.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {


            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Interests"

        //AutoComplete

        val interests = getResources().getStringArray(R.array.interests)

        val wordList = ArrayList<String>()
        wordList.addAll(interests)
        wordList.sort()
        Collections.addAll(wordList)

        val adapter = AutoCompleteAdapter(this, R.layout.custom_list_item, R.id.text_view_list_item, wordList)
        interestsET.setAdapter(adapter)


        addExperience.setOnClickListener {
            addInterest()
        }

        interestsET.setOnItemClickListener { parent, view, position, id ->

            addInterest()
        }

        interestsET.addTextChangedListener(loginTextWatcher)

    }

    private val loginTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val stringInput = interestsET.getText().toString().trim()

            if (stringInput.isBlank()) {
                viewVisible(interestsContainerLinear)
                NumberViewVisible(numberOfIntTextView)
            } else {
                viewInvisible(interestsContainerLinear)
                NumberViewInvisible(numberOfIntTextView)
            }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }


    fun viewInvisible(v: View) {
        interestsContainerLinear.visibility = View.INVISIBLE
    }

    fun viewVisible(v: View) {
        interestsContainerLinear.visibility = View.VISIBLE
    }

    fun NumberViewInvisible(v: View) {
        numberOfIntTextView.visibility = View.INVISIBLE
    }

    fun NumberViewVisible(v: View) {
        numberOfIntTextView.visibility = View.VISIBLE
    }


    fun addInterest() {

        // checking the number of added interests
        if (viewCounter == 10) {

            // set views to visible when there are 10 interests
            viewVisible(interestsContainerLinear)
            NumberViewVisible(numberOfIntTextView)

            Toast.makeText(this, "You've reached maximum number of interests", Toast.LENGTH_LONG).show()
            return
        } else {

            // adding X to the string
            val interestToDB = interestsET.text.toString()

            var interestStr = interestsET.text.toString().plus("  X")
            val textView = TextView(this, null, 0, R.style.Interest)

            // checking the length of the string
            if (interestStr.length >= 33) {

                Toast.makeText(this, "Interest you've entered is too long", Toast.LENGTH_SHORT).show()
                return
            }

            if (interestStr.length < 5) {

                Toast.makeText(this, "Interest you've entered is too short", Toast.LENGTH_SHORT).show()
                return
            }

            // add interest to database
            interestDatabase.push().setValue(interestToDB)

            // parameters for TextView
            textView.layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val param = textView.layoutParams as LinearLayout.LayoutParams
            textView.gravity = Gravity.CENTER
            param.setMargins(70, 35, 0, 35)
            textView.layoutParams = param
            textView.setText(interestStr)
            interestsContainerLinear.addView(textView)
            // send string to ProfileFragment
            //closeActivity(interestsET.text.toString())
            ++viewCounter

            // changing the color of the number of views (from black to orange) when there are 10/10 views
            if (viewCounter == 10) {
                numberOfIntTextView.setTextColor(Color.parseColor("#f47742"))
            }

            var interestCounter = viewCounter.toString().plus("/10")

            numberOfIntTextView.setText(interestCounter)

            interestsET.getText()?.clear()

            // delete interest on touch
            textView.setOnClickListener {
                interestsContainerLinear.removeView(textView)
                --viewCounter
                if (viewCounter != 10) {
                    numberOfIntTextView.setTextColor(Color.parseColor("#808080"))
                }
                var interestCounter = viewCounter.toString().plus("/10")
                numberOfIntTextView.setText(interestCounter)
            }
        }
    }

    fun populateInterests() {

        interestDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {

                    var interestsFromDB = snapshot.getValue(String::class.java)

                    // added X
                    var interestsFromDBX = interestsFromDB.plus("  X")

                    val textView = TextView(this@InterestsActivity, null, 0, R.style.Interest)

                    textView.layoutParams =
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    val param = textView.layoutParams as LinearLayout.LayoutParams
                    textView.gravity = Gravity.CENTER
                    param.setMargins(70, 35, 0, 35)
                    textView.layoutParams = param
                    textView.setText(interestsFromDBX)
                    interestsContainerLinear.addView(textView)

                    ++viewCounter

                    if (viewCounter == 10) {
                        numberOfIntTextView.setTextColor(Color.parseColor("#f47742"))
                    }

                    var interestCounter = viewCounter.toString().plus("/10")

                    numberOfIntTextView.setText(interestCounter)

                    textView.setOnClickListener {
                        interestsContainerLinear.removeView(textView)
                        --viewCounter
                        if (viewCounter != 10) {
                            numberOfIntTextView.setTextColor(Color.parseColor("#808080"))
                        }
                        var interestCounter = viewCounter.toString().plus("/10")
                        numberOfIntTextView.setText(interestCounter)

                        var key = snapshot.key.toString()

                        interestDatabase.child(key).removeValue()

                        Log.d("TAG", key)

                    }
                }
            }
        })
    }

    private fun closeActivity(interest: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_INTEREST, interest)

        setResult(Activity.RESULT_OK, resultIntent)
        //finish()
    }


    companion object {
        @JvmField
        val INPUT_INTEREST = "TESTINTEREST"
    }


    override fun onSupportNavigateUp(): Boolean {

        closeActivity("closed")
        onBackPressed()
        return true
    }
}
