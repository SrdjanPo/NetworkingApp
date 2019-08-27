package com.example.networkingapp.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_previous_org_edit.*
import java.util.*

class PreviousOrgEditActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var previousOrgDB: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    private var checkDelete = 1

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_org_edit)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        previousOrgDB = database.child(userId!!).child("previousOrg")

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Experience"

        val editCompany = intent.getStringExtra("companySent")
        val editCompanyTitle = intent.getStringExtra("titleSent")
        val editStartDate = intent.getStringExtra("startDateSent")
        val editEndDate = intent.getStringExtra("endDateSent")
        val position = intent.getIntExtra("position", -1)

        companyET.setText(editCompany, TextView.BufferType.EDITABLE)
        titleET.setText(editCompanyTitle, TextView.BufferType.EDITABLE)
        previousStartDateET.setText(editStartDate, TextView.BufferType.NORMAL)
        previousEndDateET.setText(editEndDate, TextView.BufferType.NORMAL)

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

        deleteExperience.setOnClickListener {

            checkDelete = 2

            previousOrgDB.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    var counter = 0

                    for (snapshot in p0.children) {

                        if (counter == position) {

                            snapshot.ref.removeValue()

                        }

                        counter++
                    }
                }
            })

            closeActivity("company", "title", "startDate", "endDate", checkDelete)
        }

        profileChanges.setOnClickListener {

            val string1 = companyET.text.toString()
            val string2 = titleET.text.toString()
            val string3 = previousStartDateET.text.toString()
            val string4 = previousEndDateET.text.toString()


            if (string1.length == 0) {

                companyET.error = "Please enter company name"

            } else if (string2.length == 0) {

                titleET.error = "Please enter your job title"

            } else if (string3.length == 0) {
                previousStartDateET.error = "Please enter start date"

            } else if (string4.length == 0) {
                previousEndDateET.error = "Please enter end date"

            } else {

                previousOrgDB.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        var counter = 0

                        for (snapshot in p0.children) {

                            if (counter == position) {

                                snapshot.child("company").ref.setValue(string1)
                                snapshot.child("title").ref.setValue(string2)
                                snapshot.child("startDate").ref.setValue(string3)
                                snapshot.child("endDate").ref.setValue(string4)

                            }

                            counter++
                        }
                    }
                })

                closeActivity(string1, string2, string3, string4, checkDelete)

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

    private fun closeActivity(company: String, title: String, startdate: String, enddate: String, deleted: Int) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_COMPANY, company)
        resultIntent.putExtra(INPUT_TITLE, title)
        resultIntent.putExtra(INPUT_STARTDATE, startdate)
        resultIntent.putExtra(INPUT_ENDDATE, enddate)
        resultIntent.putExtra(INPUT_DELETED, deleted)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_COMPANY = "TESTCOMPANY"
        val INPUT_TITLE = "TESTTITLE"
        val INPUT_STARTDATE = "TESTSTARTDATE"
        val INPUT_ENDDATE = "TESTENDDATE"
        val INPUT_DELETED = "TESTDELETED"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
