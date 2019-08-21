package com.example.networkingapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.profile.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TinderCallback? = null

    val REQUEST_CODE_BASICINFO = 11
    val REQUEST_CODE_INTERESTS = 12
    val REQUEST_CODE_ABOUT = 13


    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase().child(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressLayout.setOnTouchListener { view, event -> true }

        populateInfo()

        // Signout button
        signoutButton.setOnClickListener { callback?.onSignout() }

        // Open BasicInfoActivity
        basicInfoEdit.setOnClickListener {
            startBasicInfoActivity()
        }

        // Open InterestsActivity on click
        interestsContainer.setOnClickListener {
            startInterestsActivity()
        }


        // Open InstagramActivity on click
        instagramicon.setOnClickListener {
            val intentInstagram = Intent(getActivity(), InstagramActivity::class.java)
            startActivity(intentInstagram)
        }


        // Open LinkedinActivity on click
        linkedinicon.setOnClickListener {
            val intentLinkedin = Intent(getActivity(), LinkedinActivity::class.java)
            startActivity(intentLinkedin)
        }

        // Open WebActivity on click
        wwwicon.setOnClickListener {
            val intentWeb = Intent(getActivity(), WebActivity::class.java)
            startActivity(intentWeb)
        }

        // Open AboutActivity on click
        relativeabout.setOnClickListener {
            startAboutActivity()
        }

        // Open GoalsActivity on click
        relativegoals.setOnClickListener {
            val intentGoals = Intent(getActivity(), GoalsActivity::class.java)
            startActivity(intentGoals)
        }

        // Open ExperienceActivity on click
        relativeexp.setOnClickListener {
            val intentExperience = Intent(getActivity(), ExperienceActivity::class.java)
            startActivity(intentExperience)
        }


    }

    fun populateInfo() {

        progressLayout.visibility = View.VISIBLE

        userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                progressLayout.visibility = View.GONE


            }

            override fun onDataChange(p0: DataSnapshot) {

                if (isAdded) {

                    val user = p0.getValue(User::class.java)
                    profileName.setText(user?.name, TextView.BufferType.NORMAL)
                    profileProfession.setText(user?.profession, TextView.BufferType.NORMAL)
                    profileLocation.setText(user?.location, TextView.BufferType.NORMAL)

                    aboutProfile.setText(user?.about, TextView.BufferType.NORMAL)

                    progressLayout.visibility = View.GONE
                }
            }

        })

    }

    fun startBasicInfoActivity() {
        val intent = Intent(getActivity(), BasicInfoActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_BASICINFO)
    }

    fun startInterestsActivity() {
        val intent = Intent(getActivity(), InterestsActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_INTERESTS)
    }

    fun startAboutActivity() {
        val intent = Intent(getActivity(), AboutActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ABOUT)
    }


    @Suppress("RedundantOverride")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_BASICINFO && resultCode == RESULT_OK) {
            val name = data?.getStringExtra(BasicInfoActivity.INPUT_NAME)
            val profession = data?.getStringExtra(BasicInfoActivity.INPUT_PROFESSION)
            val location = data?.getStringExtra(BasicInfoActivity.INPUT_LOCATION)
            profileName.text = name
            profileProfession.text = profession
            profileLocation.text = location
        }
        if (requestCode == REQUEST_CODE_INTERESTS && resultCode == RESULT_OK) {

            val interest = data?.getStringExtra(InterestsActivity.INPUT_INTEREST)

            // OVDE SETOVATI PRIMLJENE PODATKE

            //test.text = interest

        }
        if (requestCode == REQUEST_CODE_ABOUT && resultCode == RESULT_OK) {

            val about = data?.getStringExtra(AboutActivity.INPUT_ABOUT)
            //val aboutQuoted = "''".plus(about).plus("''")
            //aboutProfile.setText(aboutQuoted)
            aboutProfile.setText(about)

        }
    }
}
