package com.example.networkingapp.profile

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import com.example.networkingapp.CurrentOrganization
import com.example.networkingapp.R
import com.example.networkingapp.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_current_org_edit.*
import java.util.*

class CurrentOrgEditActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var currentOrgDB: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    private var checkDelete = 1

    val MONTHS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_org_edit)

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        currentOrgDB = database.child(userId!!).child("currentOrg")

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Experience"

        // Setting EndDate to Present
        currentEndDateET.setText("Present")

        val editCompany = intent.getStringExtra("companySent")
        val editCompanyTitle = intent.getStringExtra("titleSent")
        val editStartDate = intent.getStringExtra("dateSent")
        val position = intent.getIntExtra("position", -1)

        companyET.setText(editCompany, TextView.BufferType.EDITABLE)
        titleET.setText(editCompanyTitle, TextView.BufferType.EDITABLE)
        currentStartDateET.setText(editStartDate, TextView.BufferType.NORMAL)

        currentStartDateTIL.setOnClickListener {
            showCalendar()
        }

        currentStartDateET.setOnClickListener {
            showCalendar()
        }

        deleteExperience.setOnClickListener {

            checkDelete = 2

            currentOrgDB.addListenerForSingleValueEvent(object: ValueEventListener {
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

            closeActivity("company","title","startDate", checkDelete)
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

                currentOrgDB.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        var counter = 0

                        for (snapshot in p0.children) {

                            if (counter == position) {

                                snapshot.child("company").ref.setValue(string1)
                                snapshot.child("title").ref.setValue(string2)
                                snapshot.child("startDate").ref.setValue(string3)

                            }

                            counter++
                        }
                    }
                })

                closeActivity(string1,string2,string3,checkDelete)

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

    private fun closeActivity(company: String, title: String, startdate: String, deleted: Int) {

        val resultIntent = Intent()
        resultIntent.putExtra(INPUT_COMPANY, company)
        resultIntent.putExtra(INPUT_TITLE, title)
        resultIntent.putExtra(INPUT_STARTDATE, startdate)
        resultIntent.putExtra(INPUT_DELETED, deleted)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val INPUT_COMPANY = "TESTCOMPANY"
        val INPUT_TITLE = "TESTTITLE"
        val INPUT_STARTDATE = "TESTSTARTDATEEEE"
        val INPUT_DELETED = "TESTSTARTDATE"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
