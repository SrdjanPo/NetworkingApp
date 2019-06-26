package com.example.networkingapp.profile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Patterns
import com.example.networkingapp.R
import com.example.networkingapp.activities.TinderActivity
import com.example.networkingapp.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.activity_basic_info.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

class BasicInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_info)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Basic Info"


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

                closeActivity(string, string1, string2)

            }


        }

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
