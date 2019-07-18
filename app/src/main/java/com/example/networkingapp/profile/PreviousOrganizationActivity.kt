package com.example.networkingapp.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.networkingapp.R
import kotlinx.android.synthetic.main.activity_current_organization.*
import kotlinx.android.synthetic.main.activity_previous_organization.*
import java.util.*

class PreviousOrganizationActivity : AppCompatActivity() {

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_organization)

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
                val date = "" + MONTHS[monthOfYear] + " - " + year
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
                val date = "" + MONTHS[monthOfYear] + " - " + year
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}