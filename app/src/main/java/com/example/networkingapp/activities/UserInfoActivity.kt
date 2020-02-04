package com.example.networkingapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.networkingapp.CurrentOrganization
import com.example.networkingapp.PreviousOrganization
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.util.*
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_info.*
import org.apmem.tools.layouts.FlowLayout

class UserInfoActivity : AppCompatActivity() {

    private lateinit var userDatabase: DatabaseReference

    var counterSnapshotCurrent = 1
    var counterSnapshotPrevious = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "User Info"

        val userId = intent.extras.getString(PARAM_USER_ID, "")
        if (userId.isNullOrEmpty()) {
            finish()
        }


        userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)
        /*userDatabase.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                profileName.text = user?.firstName.plus(" ").plus(user?.lastName)
                profileProfession.text = user?.profession
                profileLocation.text = user?.location
                if(user?.thumb_image != null) {
                    Glide.with(this@UserInfoActivity)
                        .load(user.thumb_image)
                        .into(photoIV)
                }
                else {

                    photoIV.setImageResource(R.drawable.profile_pic)
                }
            }
        })*/

        populateInfo()
    }

    fun populateInfo() {

        progressLayout.visibility = View.VISIBLE
        profileLayout.visibility = View.GONE

        userDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                progressLayout.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {


                // Removing views before each load, so the views won't duplicate
                flowInterests.removeAllViews()
                flowGoals.removeAllViews()
                expContainer.removeAllViews()


                val user = p0.getValue(User::class.java)

                // Basic info
                profileName.setText(
                    user?.firstName.plus(" ").plus(user?.lastName),
                    TextView.BufferType.NORMAL
                )

                profileProfession.setText(user?.profession, TextView.BufferType.NORMAL)
                //profileLocation.setText(user?.location, TextView.BufferType.NORMAL)

                var image = p0.child("image").value.toString()
                var thumbnail = p0.child("thumb_image").value.toString()

                var countPreviousChildren = p0.child("previousOrg").childrenCount.toInt()
                var countCurrentChildren = p0.child("currentOrg").childrenCount.toInt()

                if (!thumbnail!!.equals("default")) {
                    Picasso.with(applicationContext).load(thumbnail).placeholder(R.drawable.profile_pic)
                        .into(photoIV, object : Callback {
                            override fun onError() {
                                Log.d("TAG", "error")
                            }

                            override fun onSuccess() {

                                Log.d("TAG", "success")
                            }
                        })
                }

                // Interests
                for (snapshotInterest in p0.child("interestedIn").children) {

                    var interestsFromDB = snapshotInterest.getValue(String::class.java)

                    populateInterests(interestsFromDB)
                }

                // Goals
                for (snapshotGoal in p0.child("goals").children) {

                    var goalFromDB = snapshotGoal.getValue(String::class.java)

                    populateGoals(goalFromDB)
                }

                // Current Organization
                for (snapshotCurrentOrg in p0.child("currentOrg").children) {

                    var currentFromDB =
                        snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                    if (countPreviousChildren == 0) {

                        populateCurrentOrgIfNoPreviousOrg(currentFromDB, countCurrentChildren)
                    } else {

                        populateCurrentOrg(currentFromDB)
                    }
                }

                for (snapshotPreviousOrg in p0.child("previousOrg").children) {

                    var previousFromDB =
                        snapshotPreviousOrg.getValue(PreviousOrganization::class.java)

                    populatePreviousOrg(previousFromDB, countPreviousChildren)

                }

                aboutProfile.setText(user?.about, TextView.BufferType.NORMAL)


                progressLayout.visibility = View.GONE
                profileLayout.visibility = View.VISIBLE
            }
        })
    }

    fun populateInterests(interestName: String?) {

        val textView = TextView(this, null, 0, R.style.interestsProfile)

        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        params.setMargins(0, 20, 20, 0)
        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.layoutParams = params
        textView.setText(interestName)
        flowInterests.addView(textView)
    }

    fun populateGoals(goalName: String?) {

        val textView = TextView(this, null, 0, R.style.goalsProfile)

        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define view height
        )

        params.setMargins(0, 30, 20, 0)

        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.setText(goalName)

        addImageToGoal(goalName, textView)
        textView.compoundDrawablePadding = 15

        flowGoals.addView(textView)
    }

    fun addImageToGoal(goalName: String?, textView: TextView) {

        when (goalName) {
            GOALS_HIRE_EMPLOYEES -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_hireemployeesicon,
                0,
                0,
                0
            )

            GOALS_LOOKING_FOR_A_JOB -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lookingforajobicon,
                0,
                0,
                0
            )

            GOALS_FIND_COFOUNDERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_findcofoundersicon,
                0,
                0,
                0
            )

            GOALS_INVEST_IN_PROJECTS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_investinprojectsicon,
                0,
                0,
                0
            )

            GOALS_FIND_INVESTORS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_suit1,
                0,
                0,
                0
            )

            GOALS_GROW_MY_BUSINESS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_plant1,
                0,
                0,
                0
            )

            GOALS_HIRE_FREELANCERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_hirefreelancersicon,
                0,
                0,
                0
            )

            GOALS_FIND_FREELANCE_JOBS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_findfreelancejobsicon,
                0,
                0,
                0
            )

            GOALS_FIND_MENTORS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_owl2,
                0,
                0,
                0
            )

            GOALS_MENTOR_OTHERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_mentorothersicon,
                0,
                0,
                0
            )

            GOALS_MAKE_NEW_FRIENDS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_makenewfriendsicon,
                0,
                0,
                0
            )

            GOALS_EXPLORE_IDEAS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_explorenewideas,
                0,
                0,
                0
            )

            else -> { // Note the block
                Log.d("error", "error occurred")
            }
        }
    }

    fun populateCurrentOrgIfNoPreviousOrg(
        currentOrganization: CurrentOrganization?,
        childrenCounter: Int
    ) {


        if (counterSnapshotCurrent != childrenCounter) {

            companyNameCurrent(currentOrganization)
            jobTitleCurrent(currentOrganization)
            startingDateCurrent(currentOrganization)
            horizontalSeparator()

            counterSnapshotCurrent++

        } else {

            companyNameCurrent(currentOrganization)
            jobTitleCurrent(currentOrganization)
            startingDateCurrent(currentOrganization)

            counterSnapshotCurrent = 1
        }
    }

    fun populateCurrentOrg(currentOrganization: CurrentOrganization?) {

        companyNameCurrent(currentOrganization)
        jobTitleCurrent(currentOrganization)
        startingDateCurrent(currentOrganization)
        horizontalSeparator()
    }

    fun populatePreviousOrg(previousOrganization: PreviousOrganization?, countChildren: Int) {

        if (counterSnapshotPrevious != countChildren) {

            companyNamePrevious(previousOrganization)
            jobTitlePrevious(previousOrganization)
            datePrevious(previousOrganization)
            horizontalSeparator()

            counterSnapshotPrevious++

        } else {

            companyNamePrevious(previousOrganization)
            jobTitlePrevious(previousOrganization)
            datePrevious(previousOrganization)

            counterSnapshotPrevious = 1
        }
    }

    fun companyNameCurrent(currentOrganization: CurrentOrganization?) {

        val companyName = TextView(this, null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(currentOrganization!!.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        expContainer.addView(companyName)
    }

    fun jobTitleCurrent(currentOrganization: CurrentOrganization?) {

        val jobTitle = TextView(this, null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(currentOrganization!!.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        expContainer.addView(jobTitle)
    }

    fun startingDateCurrent(currentOrganization: CurrentOrganization?) {

        val startingDate = TextView(this, null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        startingDate.layoutParams = paramsDate
        startingDate.gravity = Gravity.CENTER
        startingDate.setText(currentOrganization!!.startDate.toString().plus(" - Present"))
        startingDate.setTextColor(Color.parseColor("#545454"))
        startingDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        expContainer.addView(startingDate)
    }

    fun companyNamePrevious(previousOrganization: PreviousOrganization?) {

        val companyName = TextView(this, null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(previousOrganization!!.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        expContainer.addView(companyName)
    }

    fun jobTitlePrevious(previousOrganization: PreviousOrganization?) {

        val jobTitle = TextView(this, null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(previousOrganization!!.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        expContainer.addView(jobTitle)
    }

    fun datePrevious(previousOrganization: PreviousOrganization?) {

        val textViewDate = TextView(this, null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        textViewDate.layoutParams = paramsDate
        textViewDate.gravity = Gravity.CENTER
        textViewDate.setText(
            previousOrganization!!.startDate.toString().plus(" - ").plus(
                previousOrganization!!.endDate
            )
        )
        textViewDate.setTextColor(Color.parseColor("#545454"))
        textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        expContainer.addView(textViewDate)
    }

    fun horizontalSeparator() {

        val horizontalSeparator = View(this, null, 0)

        var paramsSeparator: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            2
        )

        paramsSeparator.setMargins(0, 10, 0, 10)
        horizontalSeparator.layoutParams = paramsSeparator
        horizontalSeparator.setBackgroundColor(Color.parseColor("#c0c0c0"))
        expContainer.addView(horizontalSeparator)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private val PARAM_USER_ID = "User id"

        fun newIntent(context: Context, userId: String?): Intent {
            val intent = Intent(context, UserInfoActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            return intent
        }
    }
}
