package com.example.networkingapp.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_goals.*
import kotlinx.android.synthetic.main.row_listview.view.*

class GoalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Goals"

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
