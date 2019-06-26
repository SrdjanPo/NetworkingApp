package com.example.networkingapp.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_instagram.*
import kotlinx.android.synthetic.main.activity_interests.*




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

        addExperience.setOnClickListener {
            addInterest()
        }



    }

    fun addInterest() {


        if( viewCounter >=10){

            Toast.makeText(this, "You've reached maximum number of interests", Toast.LENGTH_SHORT).show()
            return
        }

        else {

            var interestStr = interestsET.text.toString().plus("  X")
            //tempContainer.setText(interestStr)

            val textView = TextView(this, null, 0, R.style.Interest)

            textView.layoutParams =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val param = textView.layoutParams as LinearLayout.LayoutParams
            textView.gravity = Gravity.CENTER
            param.setMargins(70, 35, 0, 35)
            textView.layoutParams = param
            textView.setText(interestStr)
            interestsContainerLinear.addView(textView)
            viewCounter++
            interestsET.getText()?.clear()
            textView.setOnClickListener {
                interestsContainerLinear.removeView(textView)
                viewCounter--
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
