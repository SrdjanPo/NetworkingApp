package com.example.networkingapp.profile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_interests.*
import com.example.networkingapp.R
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.View
import java.util.*
import kotlin.collections.ArrayList
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.HashMap


class InterestsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var interestDatabase: DatabaseReference
    private lateinit var profileDatabase: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    var map = HashMap<String, Int?>()

    var maxValue: String? = "testing"
    var maxValueCopy: String? = "testing"
    var maxNumber: Int? = 0

    var viewCounter = 0

    var technologyAL = ArrayList<String>()
    var designAL = ArrayList<String>()
    var hobbyAL = ArrayList<String>()
    var sportAL = ArrayList<String>()
    var scienceAL = ArrayList<String>()

    var interestedInAL = ArrayList<String>() // list of items stored in insterestedIn

    var suggestionsList = ArrayList<String>() // list of items in suggestions view


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        // intent on profile setup (certain things need to be shown/hidden depending on from which activity you've entereted)

        var i = intent.getIntExtra("next", 2)

        if (i == 1) {

            nextButton.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            progressBar.progress = 30

            emptyInterests()
        }

        //button that shows up on profile setup

        nextButton.setOnClickListener {

            if (viewCounter < 3) {

                Toast.makeText(this, "Enter at least 3 interests", Toast.LENGTH_LONG).show()

            }

            else {

                val intent = Intent(this, GoalsActivity::class.java)
                intent.putExtra("next", 1)
                startActivity(intent)
            }
        }

        //firebase child instances

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)
        interestDatabase = database.child(userId!!).child("interestedIn")
        profileDatabase = database.child(userId!!).child("profile")

        //populate interests from firebase
        populateInterests()

        //emptyInterests()

        //populate profile counters so "profiled" value can be extracted
        populateCounters()

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Interests"


        var technologyArray = getResources().getStringArray(R.array.technology)
        var sportArray = getResources().getStringArray(R.array.sport)
        var scienceArray = getResources().getStringArray(R.array.science)
        var hobbyArray = getResources().getStringArray(R.array.hobby)
        var designArray = getResources().getStringArray(R.array.design)

        technologyAL.addAll(technologyArray)
        sportAL.addAll(sportArray)
        scienceAL.addAll(scienceArray)
        hobbyAL.addAll(hobbyArray)
        designAL.addAll(designArray)

        // list of items shown in AutoCompleteTextView dropdown list

        val wordList = ArrayList<String>()
        wordList.addAll(technologyArray)
        wordList.addAll(sportArray)
        wordList.addAll(scienceArray)
        wordList.addAll(hobbyArray)
        wordList.addAll(designArray)

        wordList.sort()
        Collections.addAll(wordList)

        // adapter for AutoCompleteTextView

        val adapter =
            AutoCompleteAdapter(this, R.layout.custom_list_item, R.id.text_view_list_item, wordList)
        interestsET.setAdapter(adapter)

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
                interestsContainerLinear.visibility = View.VISIBLE
                numberOfIntTextView.visibility = View.VISIBLE
            } else {
                interestsContainerLinear.visibility = View.GONE
                numberOfIntTextView.visibility = View.GONE
            }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    private fun maxNumberOfInterests () {

        // set views to visible when there are 10 interests
        interestsContainerLinear.visibility = View.VISIBLE
        numberOfIntTextView.visibility = View.VISIBLE

        Toast.makeText(this, "You've reached maximum number of interests", Toast.LENGTH_LONG)
            .show()
    }

    private fun interestActions (interest: String, textView: TextView) {

        val interestStr = interest.plus("  X")

        addProfile(interest) // incrementing proper profile value
        checkMaxValue() // checking max value of the HashMap so we can extract "profiled" value
        addSuggestions(maxValue!!) // Initializing addSuggestions function with proper value (maxValue is one of the array strings technology,design,hobby,sport, etc...)

        // parameters for TextView

        parametersForTextView(textView, interestStr)

        ++viewCounter
    }

    private fun parametersForTextView (textView: TextView, interestStr: String) {

        textView.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val param = textView.layoutParams as LinearLayout.LayoutParams
        textView.gravity = Gravity.CENTER
        param.setMargins(70, 20, 0, 20)
        textView.layoutParams = param
        textView.setText(interestStr)
        interestsContainerLinear.addView(textView)
    }

    private fun settingUpInterestCounter () {

        var interestCounter = viewCounter.toString().plus("/10")

        numberOfIntTextView.setText(interestCounter)
    }

    private fun removeInterest (textView: TextView) {

        interestsContainerLinear.removeView(textView)
        var textFromTV = textView.text.toString()
        deleteProfile(textFromTV)  // decrementing proper value
        checkMaxValue() // checking max value of the HashMap so we can extract "profiled" value
        --viewCounter

        // if there are less than 10 interests, interests counter remains grey

        if (viewCounter != 10) {
            numberOfIntTextView.setTextColor(Color.parseColor("#808080"))
        }

        var interestCounter = viewCounter.toString().plus("/10")
        numberOfIntTextView.setText(interestCounter)


        var interestText = textFromTV.substring(0, textFromTV.length - 3) // because we added "  X" to the interest firstName, now we have to trim it
        interestedInAL.remove(interestText) // removing item form the interestedInAL
        addSuggestions(maxValue!!) // initializing addSuggestins with a proper value

        emptyInterests()
    }


    fun addInterest() {

        // checking the number of added interests

        if (viewCounter == 10) {

            maxNumberOfInterests()

            return

        } else {

            val interestToDB = interestsET.text.toString()

            val textView = TextView(this, null, 0, R.style.Interest)

            val key = interestDatabase.push().key
            interestDatabase.child(key!!).setValue(interestToDB) // adding a interest to database
            interestedInAL.add(interestToDB) // adding it to the interestedIn ArrayList

            interestActions(interestToDB, textView)

            // changing the color of the number of views (from black to orange) when there are 10/10 views
            if (viewCounter == 10) {

                numberOfIntTextView.setTextColor(Color.parseColor("#f47742"))
            }

            settingUpInterestCounter()

            interestsET.getText()?.clear()

            // delete interest on click

            textView.setOnClickListener {

                interestDatabase.child(key).removeValue()
                removeInterest(textView)
            }
        }
    }

    fun addSuggestionOnClick(suggestion: String) {

        // checking the number of added interests
        if (viewCounter == 10) {

            maxNumberOfInterests()
            return

        } else {

            // adding X to the string

            val textView = TextView(this, null, 0, R.style.Interest)

            var keyCurrent = interestDatabase.push().key
            interestDatabase.child(keyCurrent!!).setValue(suggestion) // Add interest to database
            interestedInAL.add(suggestion)

            interestActions(suggestion, textView)

            // changing the color of the number of views (from black to orange) when there are 10/10 views
            if (viewCounter == 10) {

                numberOfIntTextView.setTextColor(Color.parseColor("#f47742"))
            }

            settingUpInterestCounter()

            interestsET.getText()?.clear()


            // delete interest on click
            textView.setOnClickListener {
                interestDatabase.child(keyCurrent).removeValue()
                removeInterest(textView)
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

                    interestedInAL.add(interestsFromDB!!) // adding interest to interestedIn ArrayList

                    var interestsFromDBX = interestsFromDB.plus("  X") // adding "  X" to the interest

                    //parameters for TextView
                    val textView = TextView(this@InterestsActivity, null, 0, R.style.Interest)

                    textView.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    val param = textView.layoutParams as LinearLayout.LayoutParams
                    textView.gravity = Gravity.CENTER
                    param.setMargins(70, 20, 0, 20)
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
                        deleteProfile(interestsFromDBX)
                        checkMaxValue()

                        if (viewCounter != 10) {
                            numberOfIntTextView.setTextColor(Color.parseColor("#808080"))
                        }

                        var interestCounter = viewCounter.toString().plus("/10")
                        numberOfIntTextView.setText(interestCounter)
                        var key = snapshot.key.toString()
                        interestDatabase.child(key).removeValue()

                        if (interestsContainerLinear.childCount != 0) {

                            addSuggestions(maxValue!!)

                        } else {

                            emptyInterests()
                        }
                    }
                }
            }
        })
    }

    fun populateCounters() {


        profileDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {


                map.put("technology", p0.child("technology").getValue(Int::class.java))
                map.put("design", p0.child("design").getValue(Int::class.java))
                map.put("hobby", p0.child("hobby").getValue(Int::class.java))
                map.put("science", p0.child("science").getValue(Int::class.java))
                map.put("sport", p0.child("sport").getValue(Int::class.java))

                if (interestsContainerLinear.childCount == 0) {

                    emptyInterests()
                }

                else {

                    maxValueCopy = map.maxBy { it.value!! }!!.key
                    maxValue = maxValueCopy
                    addSuggestions(maxValueCopy!!)

                }
            }
        })
    }

    fun addSuggestions(profiled: String) {

        when (profiled) {

            "technology" -> addRandomSuggestion(technologyAL)
            "sport" -> addRandomSuggestion(sportAL)
            "design" -> addRandomSuggestion(designAL)
            "hobby" -> addRandomSuggestion(hobbyAL)
            "science" -> addRandomSuggestion(scienceAL)
        }
    }

    fun addRandomSuggestion(profiledArray: ArrayList<String>) {

        var suggestedHelper = 0 // suggestions counter

        // removing all suggestions from view, and clearing suggestionsList

        suggestedInterests.removeAllViews()
        suggestionsList.clear()

        // after every interest is added or removed, there is 1 sec of spinning circle so the user is informed that the changes are made

        Handler().postDelayed( {

            suggestionsProgressBar.visibility = View.GONE
            suggestedInterests.visibility = View.VISIBLE

        }, 500)

        suggestedInterests.visibility = View.GONE
        suggestionsProgressBar.visibility = View.VISIBLE

        // generate 8 random suggestions

        while (suggestedHelper < 8) {

            var randomNumber = (0 until profiledArray.size - 1).random()

            // check if the random suggestion

            if (profiledArray[randomNumber] in interestedInAL || profiledArray[randomNumber] in suggestionsList) {
                Log.d("ITEM", profiledArray[randomNumber])
            } else {

                addToScroll(profiledArray[randomNumber]) // adding suggestion to the HorizontalScrollView
                suggestionsList.add(profiledArray[randomNumber]) // adding suggestion to the suggestionsList
                ++suggestedHelper // incrementing suggestions counter
            }
        }
    }

    fun addToScroll(item: String) {

        // if the suggestion added is the first one, left margin is increased to 70 instead of 20

        if (suggestedInterests.childCount == 0) {

            var textView = TextView(this, null, 0, R.style.interestsProfile)

            textView.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            val param = textView.layoutParams as LinearLayout.LayoutParams
            textView.gravity = Gravity.CENTER
            param.setMargins(70, 20, 20, 20)
            textView.layoutParams = param
            textView.setText(item)
            suggestedInterests.addView(textView)

            textView.setOnClickListener {

                addSuggestionOnClick(item)
            }

        } else {

            var textView = TextView(this, null, 0, R.style.interestsProfile)

            textView.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            val param = textView.layoutParams as LinearLayout.LayoutParams
            textView.gravity = Gravity.CENTER
            param.setMargins(0, 20, 20, 20)
            textView.layoutParams = param
            textView.setText(item)
            suggestedInterests.addView(textView)

            textView.setOnClickListener {

                addSuggestionOnClick(item)
            }
        }
    }

    fun addProfile(profile: String) {


        for (t in technologyAL) {
            if (profile == t) {

                profileDatabase.child("technology").setValue(map["technology"]!!.plus(1))

                map.put("technology", map["technology"]!!.plus(1))

                return
            }
        }

        for (s in sportAL) {
            if (profile == s) {
                profileDatabase.child("sport").setValue(map["sport"]!!.plus(1))

                map.put("sport", map["sport"]!!.plus(1))

                return
            }
        }

        for (sc in scienceAL) {
            if (profile == sc) {
                profileDatabase.child("science").setValue(map["science"]!!.plus(1))

                map.put("science", map["science"]!!.plus(1))

                return
            }
        }

        for (h in hobbyAL) {
            if (profile == h) {
                profileDatabase.child("hobby").setValue(map["hobby"]!!.plus(1))

                map.put("hobby", map["hobby"]!!.plus(1))

                return
            }
        }

        for (d in designAL) {
            if (profile == d) {
                profileDatabase.child("design").setValue(map["design"]!!.plus(1))

                map.put("design", map["design"]!!.plus(1))

                return
            }
        }

    }

    fun deleteProfile(profile: String) {


        for (t in technologyAL) {
            if (profile == t.plus("  X")) {
                profileDatabase.child("technology").setValue(map["technology"]!!.minus(1))
                map.put("technology", map["technology"]!!.minus(1))

                Log.d("TechDelete", profile)
                Log.d("HashMap", map.toString())

                return
            }
        }

        for (s in sportAL) {
            if (profile == s.plus("  X")) {
                profileDatabase.child("sport").setValue(map["sport"]!!.minus(1))

                map.put("sport", map["sport"]!!.minus(1))

                return
            }
        }

        for (sc in scienceAL) {
            if (profile == sc.plus("  X")) {
                profileDatabase.child("science").setValue(map["science"]!!.minus(1))

                map.put("science", map["science"]!!.minus(1))

                return
            }
        }

        for (h in hobbyAL) {
            if (profile == h.plus("  X")) {
                profileDatabase.child("hobby").setValue(map["hobby"]!!.minus(1))

                map.put("hobby", map["hobby"]!!.minus(1))

                return
            }
        }

        for (d in designAL) {
            if (profile == d.plus("  X")) {
                profileDatabase.child("design").setValue(map["design"]!!.minus(1))

                map.put("design", map["design"]!!.minus(1))

                return
            }
        }
    }

    private fun checkMaxValue() {

        maxValue = map.maxBy { it.value!! }!!.key
        maxNumber = map.maxBy { it.value!! }!!.value


        if (maxNumber == 0) {

            maxValue = ""
        }

        if (maxValueCopy !== maxValue) {

            database.child(userId!!).child("profiled").setValue(maxValue)

            maxValueCopy = maxValue

        }
    }

    private fun closeActivity(interest: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_INTEREST, interest)

        setResult(Activity.RESULT_OK, resultIntent)
        //finish()
    }

    private fun emptyInterests() {

        if (interestsContainerLinear.childCount == 0)

        {

            suggestedInterests.removeAllViews()

            Handler().postDelayed( {

                suggestionsProgressBar.visibility = View.GONE
                suggestedInterests.visibility = View.VISIBLE

            }, 500)

            suggestedInterests.visibility = View.GONE
            suggestionsProgressBar.visibility = View.VISIBLE

            addToScroll("Technology")
            addToScroll("Design")
            addToScroll("Sport")
            addToScroll("Science")
            addToScroll("Hobby")

        }
    }


    companion object {
        @JvmField
        val INPUT_INTEREST = "TESTINTEREST"
    }


    override fun onSupportNavigateUp(): Boolean {

        if (viewCounter < 3) {

            Toast.makeText(this, "Enter at least 3 interests", Toast.LENGTH_LONG).show()
            return false

        } else {

            closeActivity("interest")
            onBackPressed()
            return true

        }
    }
}