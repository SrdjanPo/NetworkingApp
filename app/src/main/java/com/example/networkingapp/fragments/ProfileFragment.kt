package com.example.networkingapp.fragments


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


    //fun setCallback(callback: TinderCallback) {
      //  this.callback = callback
    //}

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

        // Open BasicInfoActivity on click
        basicInfoEdit.setOnClickListener {
            val intentBasicInfo = Intent (getActivity(), BasicInfoActivity::class.java)
            startActivity(intentBasicInfo)
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


    }


}
