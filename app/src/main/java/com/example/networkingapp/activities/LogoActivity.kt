package com.example.networkingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.networkingapp.R
import com.google.firebase.auth.FirebaseAuth

class LogoActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null) {
            startActivity(TinderActivity.newIntent(this))
            finish()

        } else {

            startActivity(StartupActivity.newIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed( {

            firebaseAuth.addAuthStateListener(firebaseAuthListener)

        }, 2000)

    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}
