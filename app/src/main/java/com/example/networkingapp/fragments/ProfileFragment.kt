package com.example.networkingapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
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
import kotlinx.android.synthetic.main.fragment_profile.*
import org.apmem.tools.layouts.FlowLayout
import android.util.Log




class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TinderCallback? = null

    val REQUEST_CODE_BASICINFO = 11
    val REQUEST_CODE_INTERESTS = 12
    val REQUEST_CODE_ABOUT = 13
    val REQUEST_CODE_GOALS = 14


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

            /*val intentInterest = Intent(getActivity(), InterestsActivity::class.java)
            startActivity(intentInterest)*/
        }

        // Open GoalsActivity on click
        relativegoals.setOnClickListener {
            startGoalsActivity()
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

                    // Basic info
                    profileName.setText(user?.name, TextView.BufferType.NORMAL)
                    profileProfession.setText(user?.profession, TextView.BufferType.NORMAL)
                    profileLocation.setText(user?.location, TextView.BufferType.NORMAL)

                    for (snapshot in p0.child("interestedIn").children) {

                        var interestsFromDB = snapshot.getValue(String::class.java)

                        val textView = TextView(getActivity(), null, 0, R.style.interestsProfile)

                        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params.setMargins(0, 20, 20, 0)

                        textView.layoutParams = params

                        textView.gravity = Gravity.CENTER

                        textView.layoutParams = params

                        textView.setText(interestsFromDB)

                        flowInterests.addView(textView)
                    }

                    for (snapshotGoal in p0.child("goals").children) {

                        var goalFromDB = snapshotGoal.getValue(String::class.java)

                        val textView = TextView(getActivity(), null, 0, R.style.goalsProfile)

                        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params.setMargins(0, 30, 20, 0)

                        textView.layoutParams = params
                        textView.gravity = Gravity.CENTER
                        textView.layoutParams = params
                        textView.setText(goalFromDB)

                        if(goalFromDB == "Hire employees"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hireemployeesicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Looking for a job"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lookingforajobicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Find Co-Founders"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_findcofoundersicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Invest in projects"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_investinprojectsicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Find investors"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_suit1, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Grow my business"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_plant1, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Hire freelancers"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hirefreelancersicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Find freelance jobs"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_findfreelancejobsicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Find mentors"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_owl2, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Mentor others"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mentorothersicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Make new friends"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_makenewfriendsicon, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        else if(goalFromDB == "Explore ideas"){

                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_explorenewideas, 0,0,0)
                            textView.compoundDrawablePadding = 15
                        }

                        flowGoals.addView(textView)

                    }

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

    fun startGoalsActivity() {
        val intent = Intent(getActivity(), GoalsActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_GOALS)
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


            val t = activity!!.supportFragmentManager.beginTransaction()
            t.setReorderingAllowed(false)
            t.detach(this).attach(this).commitAllowingStateLoss()

        }

        if (requestCode == REQUEST_CODE_GOALS && resultCode == RESULT_OK) {

            val t = activity!!.supportFragmentManager.beginTransaction()
            t.setReorderingAllowed(false)
            t.detach(this).attach(this).commitAllowingStateLoss()

        }

        if (requestCode == REQUEST_CODE_ABOUT && resultCode == RESULT_OK) {

            val about = data?.getStringExtra(AboutActivity.INPUT_ABOUT)
            //val aboutQuoted = "''".plus(about).plus("''")
            //aboutProfile.setText(aboutQuoted)
            aboutProfile.setText(about)

        }
    }
}
