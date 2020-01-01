package com.example.networkingapp.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.networkingapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_startup.*
import android.content.DialogInterface
import android.Manifest.permission
import android.util.Log
import androidx.appcompat.app.AlertDialog


class StartupActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null) {
            startActivity(TinderActivity.newIntent(this))
            finish()

        }
    }

    private val FINE_LOCATION_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Log.d("tag", "peremission granted")

        } else {

            requestStoragePermission()
        }

        //imageView.animation = from_center_logo
    }

    private fun requestStoragePermission () {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.ACCESS_FINE_LOCATION)) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of this and that")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(permission.ACCESS_FINE_LOCATION), FINE_LOCATION_CODE
                    )
                })
                .setNegativeButton("cancel",
                     { dialog, which -> dialog.dismiss() })
                .create().show()
        } else {

            ActivityCompat.requestPermissions(this,  Array(1) {permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FINE_LOCATION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Permission GRANTED")
            } else {
                Log.d("TAG", "Permission DENIED")
            }
        }
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
