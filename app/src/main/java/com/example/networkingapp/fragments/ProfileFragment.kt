package com.example.networkingapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.networkingapp.R
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.profile.*
import com.google.firebase.database.DatabaseReference
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

        /*
        val example = "jedan dva tri cetri pet"

        val ssb = SpannableStringBuilder(example)

        val bcsYellow = BackgroundColorSpan(Color.YELLOW)

        ssb.setSpan(bcsYellow, 1, example.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        test.setText(ssb)
        */

        // NAPRAVITI LISTU NA ITEMIMA IMPLEMENTIRATI DRAWABLE PA APPENDOVATI U TEXTVIEW



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
            test.text = interest

        }
        if (requestCode == REQUEST_CODE_ABOUT && resultCode == RESULT_OK) {

            val about = data?.getStringExtra(AboutActivity.INPUT_ABOUT)
            //val aboutQuoted = "''".plus(about).plus("''")
            //aboutProfile.setText(aboutQuoted)
            aboutProfile.setText(about)

        }
    }
}
