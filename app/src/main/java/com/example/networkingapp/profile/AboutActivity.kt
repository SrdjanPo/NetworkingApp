package com.example.networkingapp.profile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About"

        aboutChanges.setOnClickListener {

            var aboutString = aboutET.text.toString()


            if (aboutString.length == 0) {

                aboutET.error = "Please enter a short biography"

            }

            else {

                closeActivity(aboutString)

            }


        }
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
