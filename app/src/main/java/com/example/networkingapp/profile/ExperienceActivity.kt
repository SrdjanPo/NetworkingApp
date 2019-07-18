package com.example.networkingapp.profile

import android.app.PendingIntent.getActivity
import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_experience.*

class ExperienceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)


        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Experience"

        addCurrentOrg.setOnClickListener {

            val intentCurrentOrg = Intent(this, CurrentOrganizationActivity::class.java)
            startActivity(intentCurrentOrg)
        }

        addPreviousOrg.setOnClickListener {

            val intentPreviousOrg = Intent(this, PreviousOrganizationActivity::class.java)
            startActivity(intentPreviousOrg)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
