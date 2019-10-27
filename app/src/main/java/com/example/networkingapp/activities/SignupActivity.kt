package com.example.networkingapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.profile.BasicInfoActivity
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        /*if (user != null) {
            startActivity(TinderActivity.newIntent(this))
            finish()
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun onSignup(v: View) {
        if(!emailET.text.toString().isNullOrEmpty() && !passwordET.text.toString().isNullOrEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful) {
                        Toast.makeText(this, "Signup error ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } else {
                        val email = emailET.text.toString()
                        val userId = firebaseAuth.currentUser?.uid ?: ""
                        val user = User(userId, "","", "", "", "","","", "", "","default","default","")

                        var updateObj = HashMap<String, Any>()
                        updateObj.put("design", 0)
                        updateObj.put("hobby", 0)
                        updateObj.put("science", 0)
                        updateObj.put("sport", 0)
                        updateObj.put("technology", 0)

                        firebaseDatabase.child(DATA_USERS).child(userId).setValue(user)
                        firebaseDatabase.child(DATA_USERS).child(userId).child("profile").setValue(updateObj)
                        //firebaseDatabase.child("Users").child(userId).child("email").setValue(email)

                        val intent = Intent(this, BasicInfoActivity::class.java)
                        intent.putExtra("next", 1)
                        startActivity(intent)
                    }
                }
        }
    }

    companion object {
        fun newIntent(context: Context?) = Intent(context, SignupActivity::class.java)
    }
}
