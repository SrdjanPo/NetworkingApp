package com.example.networkingapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.example.networkingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_startup.*

class StartupActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null) {
            startActivity(TinderActivity.newIntent(this))
            finish()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)


        //imageView.animation = from_center_logo
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed( {

            firebaseAuth.addAuthStateListener(firebaseAuthListener)

        }, 1000)

        val from_bottom_login = AnimationUtils.loadAnimation(this,R.anim.from_bottom_login)
        val from_bottom_signup = AnimationUtils.loadAnimation(this,R.anim.from_bottom_signup)
        val from_center_logo = AnimationUtils.loadAnimation(this,R.anim.from_center_logo)

        loginButton.animation = from_bottom_login
        signupButton.animation = from_bottom_signup
        //imageView.animation = from_center_logo

    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun onLoginStart(v: View) {
        startActivity(LoginActivity.newIntent(this))
    }

    fun onSignupStart(v: View) {
        startActivity(SignupActivity.newIntent(this))
    }

    companion object {
        fun newIntent(context: Context?) = Intent(context, StartupActivity::class.java)
    }
}
