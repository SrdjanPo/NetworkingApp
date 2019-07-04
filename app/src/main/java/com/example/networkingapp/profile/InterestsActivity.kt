package com.example.networkingapp.profile

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
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
import android.view.View
import android.support.v4.content.ContextCompat.getSystemService
import android.view.KeyEvent.KEYCODE_BACK
import java.util.*
import kotlin.collections.ArrayList
import android.text.Editable
import android.text.TextWatcher



class InterestsActivity : AppCompatActivity() {

    var viewCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

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

            if (stringInput.isBlank())
            {
                viewVisible(interestsContainerLinear)
            }

            else
            {
                viewInvisible(interestsContainerLinear)
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


    fun addInterest() {


        if( viewCounter == 10){

            viewVisible(interestsContainerLinear)

            Toast.makeText(this, "You've reached maximum number of interests", Toast.LENGTH_SHORT).show()
            return
        }

        else {

            var interestStr = interestsET.text.toString().plus("  X")
            val textView = TextView(this, null, 0, R.style.Interest)

            textView.layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val param = textView.layoutParams as LinearLayout.LayoutParams
            textView.gravity = Gravity.CENTER
            param.setMargins(70, 35, 0, 35)
            textView.layoutParams = param
            textView.setText(interestStr)
            interestsContainerLinear.addView(textView)
            ++viewCounter

            if(viewCounter == 10)
            {
                numberOfIntTextView.setTextColor(Color.parseColor("#f47742"))
            }

            var interestCounter = viewCounter.toString().plus("/10")

            numberOfIntTextView.setText(interestCounter)

            interestsET.getText()?.clear()
            textView.setOnClickListener {
                interestsContainerLinear.removeView(textView)
                --viewCounter
                if(viewCounter != 10)
                {
                    numberOfIntTextView.setTextColor(Color.parseColor("#808080"))
                }
                var interestCounter = viewCounter.toString().plus("/10")
                numberOfIntTextView.setText(interestCounter)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
