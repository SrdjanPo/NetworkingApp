package com.example.networkingapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.networkingapp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(v: View) {
        startActivity(MainActivity.newIntent(this))

    }

    companion object {
        fun newIntent(context: Context?) = Intent(context, LoginActivity::class.java)
    }
}
