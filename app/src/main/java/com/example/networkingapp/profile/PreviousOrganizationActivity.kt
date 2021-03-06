package com.example.networkingapp.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_current_organization.*
import kotlinx.android.synthetic.main.activity_previous_organization.*
import java.util.*

class PreviousOrganizationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var previousOrgDB: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_organization)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        previousOrgDB = database.child(userId!!).child("previousOrg")

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Previous"

        previousStartDateTIL.setOnClickListener {
            showCalendar()
        }

        previousStartDateET.setOnClickListener {
            showCalendar()
        }

        previousEndDateTIL.setOnClickListener {
            showCalendarEnd()
        }

        previousEndDateET.setOnClickListener {
            showCalendarEnd()
        }

        profileChangess.setOnClickListener {

            val string1 = prevCompanyET.text.toString()
            val string2 = prevTitleET.text.toString()
            val string3 = previousStartDateET.text.toString()
            val string4 = previousEndDateET.text.toString()

            if (string1.length == 0) {

                companyET.error = "Please enter company firstName"

            } else if (string2.length == 0) {

                titleET.error = "Please enter your job title"

            } else if (string3.length == 0) {
                currentStartDateET.error = "Please enter starting date"

            } else if (string4.length == 0) {
                currentStartDateET.error = "Please enter ending date"
            }

            else
            {
                var keyPreviousOrg = previousOrgDB.push().key

                previousOrgDB.child(keyPreviousOrg!!).child("company").setValue(string1)
                previousOrgDB.child(keyPreviousOrg!!).child("title").setValue(string2)
                previousOrgDB.child(keyPreviousOrg!!).child("startDate").setValue(string3)
                previousOrgDB.child(keyPreviousOrg!!).child("endDate").setValue(string4)

                closeActivity(string1,string2,string3,string4)
            }
        }


    }

    fun showCalendar() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            AlertDialog.THEME_HOLO_LIGHT,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val date = "" + MONTHS[monthOfYear] + " " + year
                previousStartDateET.setText(date)
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().findViewById<View>(getResources().getIdentifier("day", "id", "android"))
            .setVisibility(View.GONE);

        dpd.datePicker.maxDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }

    fun showCalendarEnd() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            AlertDialog.THEME_HOLO_LIGHT,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val date = "" + MONTHS[monthOfYear] + " " + year
                previousEndDateET.setText(date)
            },
            year,
            month,
            day
        )
        dpd.getDatePicker().findViewById<View>(getResources().getIdentifier("day", "id", "android"))
            .setVisibility(View.GONE);

        dpd.datePicker.maxDate = Calendar.getInstance().timeInMillis
        dpd.show()
    }

    private fun closeActivity(company: String, title: String, startdate: String, enddate: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_PREVCOMPANY, company)
        resultIntent.putExtra(INPUT_PREVTITLE, title)
        resultIntent.putExtra(INPUT_PREVSTARTDATE, startdate)
        resultIntent.putExtra(INPUT_PREVENDDATE, enddate)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_PREVCOMPANY = "TESTCOMPANY"
        val INPUT_PREVTITLE = "TESTTITLE"
        val INPUT_PREVSTARTDATE = "TESTSTARTDATE"
        val INPUT_PREVENDDATE = "TESTPREVDATE"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}