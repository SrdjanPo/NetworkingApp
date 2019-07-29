package com.example.networkingapp.profile

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color

import android.support.v7.app.AppCompatActivity
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
    }

    val REQUEST_CODE_CURRENTORG = 21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Experience"

        currentOrgListView.adapter = MyCustomAdapter(this)

        addCurrentOrg.setOnClickListener {

            startCurrentOrganizationActivity()
        }

        addPreviousOrg.setOnClickListener {

            val intentPreviousOrg = Intent(this, PreviousOrganizationActivity::class.java)
            startActivity(intentPreviousOrg)
        }

    }

    fun startCurrentOrganizationActivity() {
        val intent = Intent(this, CurrentOrganizationActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CURRENTORG)
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

        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
