package com.example.networkingapp.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_instagram.*

class InstagramActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Instagram"

        profileChanges.setOnClickListener {

            var string = instagramET.text.toString()
            instagramET.setText(string)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
