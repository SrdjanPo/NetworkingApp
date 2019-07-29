package com.example.networkingapp.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_current_organization.*
import java.util.*

class CurrentOrganizationActivity : AppCompatActivity() {

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_organization)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Current"

        // Setting EndDate to Present
        currentEndDateET.setText("Present")

        currentStartDateTIL.setOnClickListener {
            showCalendar()
        }

        currentStartDateET.setOnClickListener {
            showCalendar()
        }

        profileChanges.setOnClickListener {

            val string1 = companyET.text.toString()
            val string2 = titleET.text.toString()
            val string3 = currentStartDateET.text.toString()

            if (string1.length == 0) {

                companyET.error = "Please enter company name"

            } else if (string2.length == 0) {

                titleET.error = "Please enter your job title"

            } else if (string3.length == 0) {
                currentStartDateET.error = "Please enter starting date"
            }

            else
            {
                closeActivity(string1,string2,string3)
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
            DatePickerDialog.OnDateSetListener { view, pickedYear, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val date = "" + MONTHS[monthOfYear] + " " + pickedYear.toString()
                currentStartDateET.setText(date)
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

    private fun closeActivity(company: String, title: String, startdate: String) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_COMPANY, company)
        resultIntent.putExtra(INPUT_TITLE, title)
        resultIntent.putExtra(INPUT_STARTDATE, startdate)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_COMPANY = "TESTCOMPANY"
        val INPUT_TITLE = "TESTTITLE"
        val INPUT_STARTDATE = "TESTSTARTDATE"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
