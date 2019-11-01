package com.example.networkingapp.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_goals.*
import org.w3c.dom.Text

class GoalsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var goalsDatabase: DatabaseReference
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

    var goalsCounter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        goalsDatabase = database.child(userId!!).child("goals")

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Goals"

        var i = intent.getIntExtra("next", 2)

        if (i == 1) {

            nextButton.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            progressBar.progress = 45
        }

        populateGoals()

        nextButton.setOnClickListener {

            if (goalsCounter < 1) {

                Toast.makeText(this, "Enter at least 1 goal", Toast.LENGTH_LONG).show()

            }

            else {

                val intent = Intent(this, ExperienceActivity::class.java)
                intent.putExtra("next", 1)
                startActivity(intent)
            }
        }

        fun addGoals(textView: TextView, child: String) {

            textView.setBackgroundResource(R.drawable.standard_button_interest)
            textView.setTextColor(Color.WHITE)
            goalsDatabase.child(child).setValue(child)
            ++goalsCounter
            displayTextCounter()
        }

        fun removeGoals(textView: TextView, child: String) {

            textView.setBackgroundResource(R.drawable.standard_button_goals)
            textView.setTextColor(Color.parseColor("#47becd"))
            goalsDatabase.child(child).removeValue()
            --goalsCounter
            displayTextCounter()
        }

        hireEmployees.setOnClickListener {

            if (checkClickedHireEmployees == 1 && goalsCounter <= 2) {

                addGoals(hireEmployees,"Hire employees")
                checkClickedHireEmployees = 2


            } else if (checkClickedHireEmployees == 2) {

                removeGoals(hireEmployees, "Hire employees")
                checkClickedHireEmployees = 1
            }

            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        lookingJob.setOnClickListener {

            if (checkClickedLookingJob == 1 && goalsCounter <= 2) {

                addGoals(lookingJob, "Looking for a job")
                checkClickedLookingJob = 2

            } else if (checkClickedLookingJob == 2) {

                removeGoals(lookingJob, "Looking for a job")
                checkClickedLookingJob = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        findCoFounders.setOnClickListener {

            if (checkClickedFindCoFounders == 1 && goalsCounter <= 2) {

                addGoals(findCoFounders, "Find Co-Founders")
                checkClickedFindCoFounders = 2

            } else if (checkClickedFindCoFounders == 2) {

                removeGoals(findCoFounders, "Find Co-Founders")
                checkClickedFindCoFounders = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        investInProjects.setOnClickListener {

            if (checkClickedInvestInProjects == 1 && goalsCounter <= 2) {

                addGoals(investInProjects, "Invest in projects")
                checkClickedInvestInProjects = 2
            }

            else if (checkClickedInvestInProjects == 2) {

                removeGoals(investInProjects, "Invest in projects")
                checkClickedInvestInProjects = 1

            }

            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        findInvestors.setOnClickListener {

            if (checkClickedFindInvestors == 1 && goalsCounter <= 2) {

                addGoals(findInvestors, "Find investors")
                checkClickedFindInvestors = 2

            } else if (checkClickedFindInvestors == 2) {

                removeGoals(findInvestors, "Find investors")
                checkClickedFindInvestors = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        growMyBusiness.setOnClickListener {

            if (checkClickedGrowMyBusiness == 1 && goalsCounter <= 2) {

                addGoals(growMyBusiness, "Grow my business")
                checkClickedGrowMyBusiness = 2

            } else if (checkClickedGrowMyBusiness == 2) {

                removeGoals(growMyBusiness, "Grow my business")
                checkClickedGrowMyBusiness = 1
            }

            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        hireFreelancers.setOnClickListener {

            if (checkClickedHireFreelancers == 1 && goalsCounter <= 2) {

                addGoals(hireFreelancers, "Hire freelancers")
                checkClickedHireFreelancers = 2

            } else if (checkClickedHireFreelancers == 2) {

                removeGoals(hireFreelancers, "Hire freelancers")
                checkClickedHireFreelancers = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        findFreelanceJobs.setOnClickListener {

            if (checkClickedFindFreelanceJobs == 1 && goalsCounter <= 2) {

                addGoals(findFreelanceJobs, "Find freelance jobs")
                checkClickedFindFreelanceJobs = 2

            } else if (checkClickedFindFreelanceJobs == 2) {

                removeGoals(findFreelanceJobs, "Find freelance jobs")
                checkClickedFindFreelanceJobs = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        findMentors.setOnClickListener {

            if (checkClickedFindMentors == 1 && goalsCounter <= 2) {

                addGoals(findMentors, "Find mentors")
                checkClickedFindMentors = 2

            } else if (checkClickedFindMentors == 2) {

                removeGoals(findMentors, "Find mentors")
                checkClickedFindMentors = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        mentorOthers.setOnClickListener {

            if (checkClickedMentorOthers == 1 && goalsCounter <= 2) {

                addGoals(mentorOthers, "Mentor others")
                checkClickedMentorOthers = 2

            } else if (checkClickedMentorOthers == 2) {

                removeGoals(mentorOthers, "Mentor others")
                checkClickedMentorOthers = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        makeNewFriends.setOnClickListener {

            if (checkClickedMakeNewFriends == 1 && goalsCounter <= 2) {

                addGoals(makeNewFriends, "Make new friends")
                checkClickedMakeNewFriends = 2

            } else if (checkClickedMakeNewFriends == 2) {

                removeGoals(makeNewFriends, "Make new friends")
                checkClickedMakeNewFriends = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }

        exploreIdeas.setOnClickListener {

            if (checkClickedExploreIdeas == 1 && goalsCounter <= 2) {

                addGoals(exploreIdeas, "Explore ideas")
                checkClickedExploreIdeas = 2

            } else if (checkClickedExploreIdeas == 2) {

                removeGoals(exploreIdeas, "Explore ideas")
                checkClickedExploreIdeas = 1
            }
            else {

                Toast.makeText(this, "You can select 3 goals maximum", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun populateGoals() {

        goalsDatabase.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {

                    var goalsFromDB = snapshot.getValue(String::class.java)


                    Log.d("GOALS", goalsCounter.toString())

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

                    ++goalsCounter
                }

                displayTextCounter()
            }
        })
    }

    private fun closeActivity(goal: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_GOAL, goal)

        setResult(Activity.RESULT_OK, resultIntent)
        //finish()
    }

    fun displayTextCounter () {

        var goalsCounterText = goalsCounter.toString().plus("/3")
        numberOfIntTextView.setText(goalsCounterText)

        Log.d("counter", goalsCounterText)
    }


    companion object {
        @JvmField
        val INPUT_GOAL = "TESTINTERESTTT"
    }

    override fun onSupportNavigateUp(): Boolean {

        if (goalsCounter < 1) {

            Toast.makeText(this, "Enter at least 1 goal", Toast.LENGTH_LONG).show()
            return false
        }

        closeActivity("closed")
        onBackPressed()
        return true
    }
}
