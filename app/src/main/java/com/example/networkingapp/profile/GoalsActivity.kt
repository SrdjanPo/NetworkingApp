package com.example.networkingapp.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_goals.*

class GoalsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var interestDatabase: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid


    var checkClickedHireEmployees = 1
    var checkClickedLookingJob = 1
    var checkClickedFindCoFounders = 1
    var checkClickedInvestInProjects = 1
    var checkClickedFindInvestors = 1
    var checkClickedGrowMyBusiness = 1
    var checkClickedHireFreelancers = 1
    var checkClickedFindFreelanceJobs = 1
    var checkClickedFindMentors = 1
    var checkClickedMentorOthers = 1
    var checkClickedMakeNewFriends = 1
    var checkClickedExploreIdeas = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        interestDatabase = database.child(userId!!).child("goals")

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Goals"

        populateGoals()

        interestDatabase.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                closeActivity("closed")

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                closeActivity("closed")

            }

            override fun onChildRemoved(p0: DataSnapshot) {

                closeActivity("closed")

            }

        })

        hireEmployees.setOnClickListener {

            if (checkClickedHireEmployees == 1) {

                hireEmployees.setBackgroundResource(R.drawable.standard_button_interest)
                hireEmployees.setTextColor(Color.WHITE)
                interestDatabase.child("Hire employees").setValue("Hire employees")
                checkClickedHireEmployees = 2

            } else if (checkClickedHireEmployees == 2) {

                hireEmployees.setBackgroundResource(R.drawable.standard_button_goals)
                hireEmployees.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Hire employees").removeValue()
                checkClickedHireEmployees = 1
            }
        }

        lookingJob.setOnClickListener {

            if (checkClickedLookingJob == 1) {

                lookingJob.setBackgroundResource(R.drawable.standard_button_interest)
                lookingJob.setTextColor(Color.WHITE)
                interestDatabase.child("Looking for a job").setValue("Looking for a job")
                checkClickedLookingJob = 2

            } else if (checkClickedLookingJob == 2) {

                lookingJob.setBackgroundResource(R.drawable.standard_button_goals)
                lookingJob.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Looking for a job").removeValue()
                checkClickedLookingJob = 1
            }
        }

        findCoFounders.setOnClickListener {

            if (checkClickedFindCoFounders == 1) {

                findCoFounders.setBackgroundResource(R.drawable.standard_button_interest)
                findCoFounders.setTextColor(Color.WHITE)
                interestDatabase.child("Find Co-Founders").setValue("Find Co-Founders")
                checkClickedFindCoFounders = 2

            } else if (checkClickedFindCoFounders == 2) {

                findCoFounders.setBackgroundResource(R.drawable.standard_button_goals)
                findCoFounders.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Find Co-Founders").removeValue()
                checkClickedFindCoFounders = 1
            }
        }

        investInProjects.setOnClickListener {

            if (checkClickedInvestInProjects == 1) {

                investInProjects.setBackgroundResource(R.drawable.standard_button_interest)
                investInProjects.setTextColor(Color.WHITE)
                interestDatabase.child("Invest in projects").setValue("Invest in projects")
                checkClickedInvestInProjects = 2

            } else if (checkClickedInvestInProjects == 2) {

                investInProjects.setBackgroundResource(R.drawable.standard_button_goals)
                investInProjects.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Invest in projects").removeValue()
                checkClickedInvestInProjects = 1
            }
        }

        findInvestors.setOnClickListener {

            if (checkClickedFindInvestors == 1) {

                findInvestors.setBackgroundResource(R.drawable.standard_button_interest)
                findInvestors.setTextColor(Color.WHITE)
                interestDatabase.child("Find investors").setValue("Find investors")
                checkClickedFindInvestors = 2

            } else if (checkClickedFindInvestors == 2) {

                findInvestors.setBackgroundResource(R.drawable.standard_button_goals)
                findInvestors.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Find investors").removeValue()
                checkClickedFindInvestors = 1
            }
        }

        growMyBusiness.setOnClickListener {

            if (checkClickedGrowMyBusiness == 1) {

                growMyBusiness.setBackgroundResource(R.drawable.standard_button_interest)
                growMyBusiness.setTextColor(Color.WHITE)
                interestDatabase.child("Grow my business").setValue("Grow my business")
                checkClickedGrowMyBusiness = 2

            } else if (checkClickedGrowMyBusiness == 2) {

                growMyBusiness.setBackgroundResource(R.drawable.standard_button_goals)
                growMyBusiness.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Grow my business").removeValue()
                checkClickedGrowMyBusiness = 1
            }
        }

        hireFreelancers.setOnClickListener {

            if (checkClickedHireFreelancers == 1) {

                hireFreelancers.setBackgroundResource(R.drawable.standard_button_interest)
                hireFreelancers.setTextColor(Color.WHITE)
                interestDatabase.child("Hire freelancers").setValue("Hire freelancers")
                checkClickedHireFreelancers = 2

            } else if (checkClickedHireFreelancers == 2) {

                hireFreelancers.setBackgroundResource(R.drawable.standard_button_goals)
                hireFreelancers.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Hire freelancers").removeValue()
                checkClickedHireFreelancers = 1
            }
        }

        findFreelanceJobs.setOnClickListener {

            if (checkClickedFindFreelanceJobs == 1) {

                findFreelanceJobs.setBackgroundResource(R.drawable.standard_button_interest)
                findFreelanceJobs.setTextColor(Color.WHITE)
                interestDatabase.child("Find freelance jobs").setValue("Find freelance jobs")
                checkClickedFindFreelanceJobs = 2

            } else if (checkClickedFindFreelanceJobs == 2) {

                findFreelanceJobs.setBackgroundResource(R.drawable.standard_button_goals)
                findFreelanceJobs.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Find freelance jobs").removeValue()
                checkClickedFindFreelanceJobs = 1
            }
        }

        findMentors.setOnClickListener {

            if (checkClickedFindMentors == 1) {

                findMentors.setBackgroundResource(R.drawable.standard_button_interest)
                findMentors.setTextColor(Color.WHITE)
                interestDatabase.child("Find mentors").setValue("Find mentors")
                checkClickedFindMentors = 2

            } else if (checkClickedFindMentors == 2) {

                findMentors.setBackgroundResource(R.drawable.standard_button_goals)
                findMentors.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Find mentors").removeValue()
                checkClickedFindMentors = 1
            }
        }

        mentorOthers.setOnClickListener {

            if (checkClickedMentorOthers == 1) {

                mentorOthers.setBackgroundResource(R.drawable.standard_button_interest)
                mentorOthers.setTextColor(Color.WHITE)
                interestDatabase.child("Mentor others").setValue("Mentor others")
                checkClickedMentorOthers = 2

            } else if (checkClickedMentorOthers == 2) {

                mentorOthers.setBackgroundResource(R.drawable.standard_button_goals)
                mentorOthers.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Mentor others").removeValue()
                checkClickedMentorOthers = 1
            }
        }

        makeNewFriends.setOnClickListener {

            if (checkClickedMakeNewFriends == 1) {

                makeNewFriends.setBackgroundResource(R.drawable.standard_button_interest)
                makeNewFriends.setTextColor(Color.WHITE)
                interestDatabase.child("Make new friends").setValue("Make new friends")
                checkClickedMakeNewFriends = 2

            } else if (checkClickedMakeNewFriends == 2) {

                makeNewFriends.setBackgroundResource(R.drawable.standard_button_goals)
                makeNewFriends.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Make new friends").removeValue()
                checkClickedMakeNewFriends = 1
            }
        }

        exploreIdeas.setOnClickListener {

            if (checkClickedExploreIdeas == 1) {

                exploreIdeas.setBackgroundResource(R.drawable.standard_button_interest)
                exploreIdeas.setTextColor(Color.WHITE)
                interestDatabase.child("Explore ideas").setValue("Explore ideas")
                checkClickedExploreIdeas = 2

            } else if (checkClickedExploreIdeas == 2) {

                exploreIdeas.setBackgroundResource(R.drawable.standard_button_goals)
                exploreIdeas.setTextColor(Color.parseColor("#47becd"))
                interestDatabase.child("Explore ideas").removeValue()
                checkClickedExploreIdeas = 1
            }
        }
    }

    fun populateGoals() {

        interestDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {

                    var goalsFromDB = snapshot.getValue(String::class.java)


                    if (goalsFromDB == "Hire employees") {

                        hireEmployees.setBackgroundResource(R.drawable.standard_button_interest)
                        hireEmployees.setTextColor(Color.WHITE)
                        checkClickedHireEmployees = 2
                    } else if (goalsFromDB == "Looking for a job") {

                        lookingJob.setBackgroundResource(R.drawable.standard_button_interest)
                        lookingJob.setTextColor(Color.WHITE)
                        checkClickedLookingJob = 2
                    } else if (goalsFromDB == "Find Co-Founders") {

                        findCoFounders.setBackgroundResource(R.drawable.standard_button_interest)
                        findCoFounders.setTextColor(Color.WHITE)
                        checkClickedFindCoFounders = 2
                    } else if (goalsFromDB == "Invest in projects") {

                        investInProjects.setBackgroundResource(R.drawable.standard_button_interest)
                        investInProjects.setTextColor(Color.WHITE)
                        checkClickedInvestInProjects = 2
                    } else if (goalsFromDB == "Find investors") {

                        findInvestors.setBackgroundResource(R.drawable.standard_button_interest)
                        findInvestors.setTextColor(Color.WHITE)
                        checkClickedFindInvestors = 2
                    } else if (goalsFromDB == "Grow my business") {

                        growMyBusiness.setBackgroundResource(R.drawable.standard_button_interest)
                        growMyBusiness.setTextColor(Color.WHITE)
                        checkClickedGrowMyBusiness = 2
                    } else if (goalsFromDB == "Hire freelancers") {

                        hireFreelancers.setBackgroundResource(R.drawable.standard_button_interest)
                        hireFreelancers.setTextColor(Color.WHITE)
                        checkClickedHireFreelancers = 2
                    } else if (goalsFromDB == "Find freelance jobs") {

                        findFreelanceJobs.setBackgroundResource(R.drawable.standard_button_interest)
                        findFreelanceJobs.setTextColor(Color.WHITE)
                        checkClickedFindFreelanceJobs = 2
                    } else if (goalsFromDB == "Find mentors") {

                        findMentors.setBackgroundResource(R.drawable.standard_button_interest)
                        findMentors.setTextColor(Color.WHITE)
                        checkClickedFindMentors = 2
                    } else if (goalsFromDB == "Mentor others") {

                        mentorOthers.setBackgroundResource(R.drawable.standard_button_interest)
                        mentorOthers.setTextColor(Color.WHITE)
                        checkClickedMentorOthers = 2
                    } else if (goalsFromDB == "Make new friends") {

                        makeNewFriends.setBackgroundResource(R.drawable.standard_button_interest)
                        makeNewFriends.setTextColor(Color.WHITE)
                        checkClickedMakeNewFriends = 2
                    } else if (goalsFromDB == "Explore ideas") {

                        exploreIdeas.setBackgroundResource(R.drawable.standard_button_interest)
                        exploreIdeas.setTextColor(Color.WHITE)
                        checkClickedExploreIdeas = 2
                    }
                }
            }
        })
    }

    private fun closeActivity(goal: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_GOAL, goal)

        setResult(Activity.RESULT_OK, resultIntent)
        //finish()
    }


    companion object {
        @JvmField
        val INPUT_GOAL = "TESTINTERESTTT"
    }

    override fun onSupportNavigateUp(): Boolean {
        closeActivity("closed")
        onBackPressed()
        return true
    }
}
