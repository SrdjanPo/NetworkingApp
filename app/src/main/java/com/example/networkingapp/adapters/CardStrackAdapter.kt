package com.example.networkingapp.adapters

import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.networkingapp.CurrentOrganization
import com.example.networkingapp.PreviousOrganization
import com.example.networkingapp.R
import com.example.networkingapp.Spot
import com.example.networkingapp.util.*
import org.apmem.tools.layouts.FlowLayout

class CardStackAdapter(

    private var spots: List<Spot> = emptyList(),
    private var currentHelper: Int = 1,
    private var previousHelper: Int = 1
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.scroll.fullScroll(ScrollView.FOCUS_UP)
        val spot = spots[position]
        holder.name.text = spot.name
        holder.profession.text = spot.profession
        Glide.with(holder.image)
            .load(spot.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.profile_pic)
            )
            .into(holder.image)
        holder.location.text = spot.location
        holder.about.text = spot.about

        holder.interestsView.removeAllViews()

        for (interest in spot.interests) {

            val textView = TextView(holder.interestsView.context, null, 0, R.style.interestsProfile)

            var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
            )

            params.setMargins(0, 20, 20, 0)
            textView.layoutParams = params
            textView.gravity = Gravity.CENTER
            textView.layoutParams = params
            textView.setText(interest)

            holder.interestsView.addView(textView)
        }

        holder.goalsView.removeAllViews()

        for (goal in spot.goals) {

            val textView = TextView(holder.goalsView.context, null, 0, R.style.goalsProfile)

            var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
            )

            params.setMargins(0, 20, 20, 0)
            textView.layoutParams = params
            textView.gravity = Gravity.CENTER
            textView.layoutParams = params
            textView.setText(goal.value)

            addImageToGoal(goal.value, textView)
            textView.compoundDrawablePadding = 15

            holder.goalsView.addView(textView)

        }

        holder.experienceView.removeAllViews()

        if (spot.currentOrgList.isEmpty() && spot.previousOrgList.isEmpty()) {

            holder.experienceRelativeView.visibility = View.GONE

        } else {

            for (current in spot.currentOrgList) {

                if (spot.countPreviousChildren == 0) {

                    populateCurrentOrgIfNoPreviousOrg(
                        current,
                        spot.countCurrentChildren,
                        holder.experienceView
                    )

                } else {

                    populateCurrentOrg(current, holder.experienceView)
                }
            }

            for (previous in spot.previousOrgList) {

                populatePreviousOrg(previous, spot.countPreviousChildren, holder.experienceView)
            }
        }
    }

    fun populateCurrentOrgIfNoPreviousOrg(
        currentOrganization: CurrentOrganization,
        childrenCounter: Int,
        experienceView: LinearLayout?
    ) {


        if (currentHelper != childrenCounter) {

            companyNameCurrent(currentOrganization, experienceView)
            jobTitleCurrent(currentOrganization, experienceView)
            startingDateCurrent(currentOrganization, experienceView)
            horizontalSeparator(experienceView)

            currentHelper++

        } else {

            companyNameCurrent(currentOrganization, experienceView)
            jobTitleCurrent(currentOrganization, experienceView)
            startingDateCurrent(currentOrganization, experienceView)

            currentHelper = 1
        }
    }

    fun populateCurrentOrg(
        currentOrganization: CurrentOrganization,
        experienceView: LinearLayout?
    ) {

        companyNameCurrent(currentOrganization, experienceView)
        jobTitleCurrent(currentOrganization, experienceView)
        startingDateCurrent(currentOrganization, experienceView)
        horizontalSeparator(experienceView)
    }

    fun populatePreviousOrg(
        previousOrganization: PreviousOrganization,
        countChildren: Int,
        experienceView: LinearLayout?
    ) {

        if (previousHelper != countChildren) {

            companyNamePrevious(previousOrganization, experienceView)
            jobTitlePrevious(previousOrganization, experienceView)
            datePrevious(previousOrganization, experienceView)
            horizontalSeparator(experienceView)

            previousHelper++

        } else {

            companyNamePrevious(previousOrganization, experienceView)
            jobTitlePrevious(previousOrganization, experienceView)
            datePrevious(previousOrganization, experienceView)

            previousHelper = 1
        }
    }

    fun companyNameCurrent(
        currentOrganization: CurrentOrganization,
        experienceView: LinearLayout?
    ) {

        val companyName = TextView(experienceView!!.context, null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(currentOrganization.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        experienceView.addView(companyName)
    }

    fun jobTitleCurrent(
        currentOrganization: CurrentOrganization,
        experienceView: LinearLayout?
    ) {

        val jobTitle = TextView(experienceView!!.context, null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(currentOrganization.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        experienceView!!.addView(jobTitle)
    }

    fun startingDateCurrent(
        currentOrganization: CurrentOrganization,
        experienceView: LinearLayout?
    ) {

        val startingDate = TextView(experienceView!!.context, null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        startingDate.layoutParams = paramsDate
        startingDate.gravity = Gravity.CENTER
        startingDate.setText(currentOrganization.startDate.toString().plus(" - Present"))
        startingDate.setTextColor(Color.parseColor("#545454"))
        startingDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        experienceView!!.addView(startingDate)
    }

    fun companyNamePrevious(
        previousOrganization: PreviousOrganization,
        experienceView: LinearLayout?
    ) {

        val companyName = TextView(experienceView!!.context, null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(previousOrganization.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        experienceView!!.addView(companyName)
    }

    fun jobTitlePrevious(
        previousOrganization: PreviousOrganization,
        experienceView: LinearLayout?
    ) {

        val jobTitle = TextView(experienceView!!.context, null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(previousOrganization.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        experienceView!!.addView(jobTitle)
    }

    fun datePrevious(
        previousOrganization: PreviousOrganization,
        experienceView: LinearLayout?
    ) {

        val textViewDate = TextView(experienceView!!.context, null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        textViewDate.layoutParams = paramsDate
        textViewDate.gravity = Gravity.CENTER
        textViewDate.setText(
            previousOrganization.startDate.plus(" - ").plus(
                previousOrganization.endDate
            )
        )
        textViewDate.setTextColor(Color.parseColor("#545454"))
        textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        experienceView.addView(textViewDate)
    }

    fun horizontalSeparator(experienceView: LinearLayout?) {

        val horizontalSeparator = View(experienceView!!.context, null, 0)

        var paramsSeparator: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            2
        )

        paramsSeparator.setMargins(0, 10, 0, 10)
        horizontalSeparator.layoutParams = paramsSeparator
        horizontalSeparator.setBackgroundColor(Color.parseColor("#c0c0c0"))
        experienceView.addView(horizontalSeparator)
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    fun setSpots(spots: List<Spot>) {
        this.spots = spots
    }

    fun getSpots(): List<Spot> {
        return spots
    }

    fun addImageToGoal(goalName: String?, textView: TextView) {

        when (goalName) {
            GOALS_HIRE_EMPLOYEES -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_hireemployeesicon,
                0,
                0,
                0
            )

            GOALS_LOOKING_FOR_A_JOB -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lookingforajobicon,
                0,
                0,
                0
            )

            GOALS_FIND_COFOUNDERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_findcofoundersicon,
                0,
                0,
                0
            )

            GOALS_INVEST_IN_PROJECTS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_investinprojectsicon,
                0,
                0,
                0
            )

            GOALS_FIND_INVESTORS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_suit1,
                0,
                0,
                0
            )

            GOALS_GROW_MY_BUSINESS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_plant1,
                0,
                0,
                0
            )

            GOALS_HIRE_FREELANCERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_hirefreelancersicon,
                0,
                0,
                0
            )

            GOALS_FIND_FREELANCE_JOBS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_findfreelancejobsicon,
                0,
                0,
                0
            )

            GOALS_FIND_MENTORS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_owl2,
                0,
                0,
                0
            )

            GOALS_MENTOR_OTHERS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_mentorothersicon,
                0,
                0,
                0
            )

            GOALS_MAKE_NEW_FRIENDS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_makenewfriendsicon,
                0,
                0,
                0
            )

            GOALS_EXPLORE_IDEAS -> textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_explorenewideas,
                0,
                0,
                0
            )

            else -> { // Note the block
                Log.d("error", "error occurred")
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.profileName)
        var profession: TextView = view.findViewById(R.id.profileProfession)
        var location: TextView = view.findViewById(R.id.profileLocation)
        var image: ImageView = view.findViewById(R.id.photoIV)
        var interestsView: FlowLayout = view.findViewById(R.id.flowInterests)
        var goalsView: FlowLayout = view.findViewById(R.id.flowGoals)
        var experienceView: LinearLayout = view.findViewById(R.id.expContainer)
        var experienceRelativeView: RelativeLayout = view.findViewById(R.id.relativeexp)
        var about: TextView = view.findViewById(R.id.aboutProfile)
        var scroll: NestedScrollView = view.findViewById(R.id.scrollView)
    }

}