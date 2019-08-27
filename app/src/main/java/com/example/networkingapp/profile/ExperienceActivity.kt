package com.example.networkingapp.profile

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.networkingapp.CurrentOrganization
import com.example.networkingapp.PreviousOrganization
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.row_listview.view.*

class ExperienceActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var currentOrgDB: DatabaseReference
    private lateinit var previousOrgDB: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    companion object {
        val company = arrayListOf<String?>()
        val companyTitle = arrayListOf<String?>()
        val date = arrayListOf<String?>()
        val prevCompany = arrayListOf<String?>()
        val prevTitle = arrayListOf<String?>()
        val prevStartDate = arrayListOf<String?>()
        val prevEndDate = arrayListOf<String?>()

        @JvmField
        val INPUT_EXPERIENCE = "TESTEXPERIENCE"
    }

    val REQUEST_CODE_CURRENTORG = 21
    val REQUEST_CODE_PREVIOUSORG = 22
    val REQUEST_CODE_CURRENTEDIT = 23
    val REQUEST_CODE_PREVIOUSEDIT = 24


    var arrayPos = 0

    var prevArrayPos = 0

    private val currentAdapter = MyCustomAdapter(this)
    private val previousAdapter = MyCustomAdapterPrevious(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        currentOrgDB = database.child(userId!!).child("currentOrg")

        previousOrgDB = database.child(userId!!).child("previousOrg")

        currentOrgDB.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                closeActivity("close")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                closeActivity("close")

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                closeActivity("close")

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                closeActivity("close")

            }
        })


        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Experience"



        currentOrgListView.adapter = currentAdapter

        previousOrgListView.adapter = previousAdapter

        populateCurrentOrg()

        populatePreviousOrg()


        addCurrentOrg.setOnClickListener {

            startCurrentOrganizationActivity()
        }

        addPreviousOrg.setOnClickListener {

            startPreviousOrganizationActivity()
        }

        currentOrgListView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, CurrentOrgEditActivity::class.java)

            var companyEdit = company[position]
            var titleEdit = companyTitle[position]
            var date = date[position]
            var position = position

            arrayPos = position

            intent.putExtra("companySent", companyEdit)
            intent.putExtra("titleSent", titleEdit)
            intent.putExtra("dateSent", date)
            intent.putExtra("position", position)


            startActivityForResult(intent, REQUEST_CODE_CURRENTEDIT)
        }

        previousOrgListView.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(this, PreviousOrgEditActivity::class.java)

            var companyEdit = prevCompany[position]
            var titleEdit = prevTitle[position]
            var startDate = prevStartDate[position]
            var endDate = prevEndDate[position]

            var position = position

            prevArrayPos = position

            intent.putExtra("companySent", companyEdit)
            intent.putExtra("titleSent", titleEdit)
            intent.putExtra("startDateSent", startDate)
            intent.putExtra("endDateSent", endDate)
            intent.putExtra("position", position)


            startActivityForResult(intent, REQUEST_CODE_PREVIOUSEDIT)
        }
    }

    fun populateCurrentOrg() {

        currentOrgDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                company.clear()
                companyTitle.clear()
                date.clear()

                for (snapshot in p0.children) {

                    val currentOrgFromDB = snapshot.getValue(CurrentOrganization::class.java)

                    company.add(currentOrgFromDB?.company)
                    companyTitle.add(currentOrgFromDB?.title)
                    date.add(currentOrgFromDB?.startDate)

                }

                currentAdapter.notifyDataSetChanged()
            }
        })
    }

    fun populatePreviousOrg() {

        previousOrgDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                prevCompany.clear()
                prevTitle.clear()
                prevStartDate.clear()
                prevEndDate.clear()

                for (snapshot in p0.children) {

                    val previousOrgFromDB = snapshot.getValue(PreviousOrganization::class.java)

                    prevCompany.add(previousOrgFromDB?.company)
                    prevTitle.add(previousOrgFromDB?.title)
                    prevStartDate.add(previousOrgFromDB?.startDate)
                    prevEndDate.add(previousOrgFromDB?.endDate)
                }

                previousAdapter.notifyDataSetChanged()
            }
        })
    }


    private class MyCustomAdapter(context: Context) : BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getCount(): Int {
            return company.size
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getItem(position: Int): Any {

            return "GET STRING"
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val rowListView = layoutInflater.inflate(R.layout.row_listview, parent, false)

            val companyTextView = rowListView.rowCompany
            val titleTextView = rowListView.rowTitle
            val dateTextView = rowListView.rowStartingDate

            companyTextView.text = company.get(position)
            titleTextView.text = companyTitle.get(position)
            dateTextView.text = date.get(position)

            return rowListView

        }

    }

    private class MyCustomAdapterPrevious(context: Context) : BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getCount(): Int {
            return prevCompany.size
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getItem(position: Int): Any {

            return "GET STRING"
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val layoutInflater = LayoutInflater.from(mContext)
            val rowListView = layoutInflater.inflate(R.layout.prevrow_listview, parent, false)

            val prevCompanyTextView = rowListView.rowCompany
            val prevTitleTextView = rowListView.rowTitle
            val prevStartDateTextView = rowListView.rowStartingDate
            val prevEndDateTextView = rowListView.rowEndingDate

            prevCompanyTextView.text = prevCompany.get(position)
            prevTitleTextView.text = prevTitle.get(position)
            prevStartDateTextView.text = prevStartDate.get(position)
            prevEndDateTextView.text = prevEndDate.get(position)

            return rowListView

        }

    }

    @Suppress("RedundantOverride")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CURRENTORG && resultCode == RESULT_OK) {
            val currentCompany = data?.getStringExtra(CurrentOrganizationActivity.INPUT_COMPANY)
            val currentTitle = data?.getStringExtra(CurrentOrganizationActivity.INPUT_TITLE)
            val currentStartDate = data?.getStringExtra(CurrentOrganizationActivity.INPUT_STARTDATE)
            company.add(currentCompany)
            companyTitle.add(currentTitle)
            date.add(currentStartDate)

            currentAdapter.notifyDataSetChanged()
        }

        if (requestCode == REQUEST_CODE_PREVIOUSORG && resultCode == RESULT_OK) {
            val previousCompany = data?.getStringExtra(PreviousOrganizationActivity.INPUT_PREVCOMPANY)
            val previousTitle = data?.getStringExtra(PreviousOrganizationActivity.INPUT_PREVTITLE)
            val previousStartDate = data?.getStringExtra(PreviousOrganizationActivity.INPUT_PREVSTARTDATE)
            val previousEndDate = data?.getStringExtra(PreviousOrganizationActivity.INPUT_PREVENDDATE)
            prevCompany.add(previousCompany)
            prevTitle.add(previousTitle)
            prevStartDate.add(previousStartDate)
            prevEndDate.add(previousEndDate)

            previousAdapter.notifyDataSetChanged()

        }

        if (requestCode == REQUEST_CODE_CURRENTEDIT && resultCode == RESULT_OK) {

            val currentCompanyEdit = data?.getStringExtra(CurrentOrgEditActivity.INPUT_COMPANY)
            val currentTitleEdit = data?.getStringExtra(CurrentOrgEditActivity.INPUT_TITLE)
            val currentStartDateEdit = data?.getStringExtra(CurrentOrgEditActivity.INPUT_STARTDATE)
            val deletedValue = data?.getIntExtra(CurrentOrgEditActivity.INPUT_DELETED, -1)


            if (deletedValue == 2) {
                company.removeAt(arrayPos)
                companyTitle.removeAt(arrayPos)
                date.removeAt(arrayPos)

                currentAdapter.notifyDataSetChanged()
            } else {

                company[arrayPos] = currentCompanyEdit
                companyTitle[arrayPos] = currentTitleEdit
                date[arrayPos] = currentStartDateEdit


                currentAdapter.notifyDataSetChanged()
            }
        }

        if (requestCode == REQUEST_CODE_PREVIOUSEDIT && resultCode == RESULT_OK) {

            val previousCompanyEdit = data?.getStringExtra(PreviousOrgEditActivity.INPUT_COMPANY)
            val previousTitleEdit = data?.getStringExtra(PreviousOrgEditActivity.INPUT_TITLE)
            val previousStartDateEdit = data?.getStringExtra(PreviousOrgEditActivity.INPUT_STARTDATE)
            val previousEndDateEdit = data?.getStringExtra(PreviousOrgEditActivity.INPUT_ENDDATE)
            val deletedValue = data?.getIntExtra(PreviousOrgEditActivity.INPUT_DELETED, -1)


            if (deletedValue == 2) {

                prevCompany.removeAt(prevArrayPos)
                prevTitle.removeAt(prevArrayPos)
                prevStartDate.removeAt(prevArrayPos)
                prevEndDate.removeAt(prevArrayPos)


                previousAdapter.notifyDataSetChanged()
            } else {

                prevCompany[prevArrayPos] = previousCompanyEdit
                prevTitle[prevArrayPos] = previousTitleEdit
                prevStartDate[prevArrayPos] = previousStartDateEdit
                prevEndDate[prevArrayPos] = previousEndDateEdit

                previousAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun closeActivity(interest: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_EXPERIENCE, interest)

        setResult(Activity.RESULT_OK, resultIntent)
        //finish()
    }

    fun startCurrentOrganizationActivity() {
        val intent = Intent(this, CurrentOrganizationActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CURRENTORG)
    }

    fun startPreviousOrganizationActivity() {
        val intent = Intent(this, PreviousOrganizationActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_PREVIOUSORG)
    }


    override fun onSupportNavigateUp(): Boolean {
        closeActivity("close")
        onBackPressed()
        return true
    }
}
