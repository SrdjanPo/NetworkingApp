package com.example.networkingapp.profile

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_experience.*
import kotlinx.android.synthetic.main.row_listview.view.*

class ExperienceActivity : AppCompatActivity() {

    companion object {
        val company = arrayListOf<String?>()
        val companyTitle = arrayListOf<String?>()
        val date = arrayListOf<String?>()
        val prevCompany = arrayListOf<String?>()
        val prevTitle = arrayListOf<String?>()
        val prevStartDate = arrayListOf<String?>()
        val prevEndDate = arrayListOf<String?>()

    }

    val REQUEST_CODE_CURRENTORG = 21
    val REQUEST_CODE_PREVIOUSORG = 22

    private val currentAdapter = MyCustomAdapter(this)
    private val previousAdapter = MyCustomAdapterPrevious(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Experience"



        currentOrgListView.adapter = currentAdapter


        //currentOrgListView.adapter = MyCustomAdapter(this)


        //previousOrgListView.adapter = MyCustomAdapterPrevious(this)

        previousOrgListView.adapter = previousAdapter



        addCurrentOrg.setOnClickListener {

            startCurrentOrganizationActivity()
        }

        addPreviousOrg.setOnClickListener {

            startPreviousOrganizationActivity()
        }

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
        onBackPressed()
        return true
    }
}
