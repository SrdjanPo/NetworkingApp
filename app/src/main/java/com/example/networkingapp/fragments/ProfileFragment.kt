package com.example.networkingapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.networkingapp.R
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.profile.*
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.fragment_profile.*



class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TinderCallback? = null

    val REQUEST_CODE = 11
    val RESULT_CODE = 12
    val EXTRA_KEY_TEST = "testKey"


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


        // Signout button
        signoutButton.setOnClickListener { callback?.onSignout() }

        // Open BasicInfoActivity
        basicInfoEdit.setOnClickListener {
            startBasicInfoActivity()
        }

        // Open InterestsActivity on click
        interestsContainer.setOnClickListener {
            val intentInterests = Intent(getActivity(), InterestsActivity::class.java)
            startActivity(intentInterests)
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
            val intentAbout = Intent(getActivity(), AboutActivity::class.java)
            startActivity(intentAbout)
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

    fun startBasicInfoActivity() {
        val intent = Intent(getActivity(), BasicInfoActivity::class.java)
        startActivityForResult(intent,REQUEST_CODE)
    }

    @Suppress("RedundantOverride")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val name = data?.getStringExtra(BasicInfoActivity.INPUT_NAME)
            profileName.text = name
        }
    }


}
