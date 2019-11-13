package com.example.networkingapp.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.networkingapp.R
import com.example.networkingapp.User
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.apmem.tools.layouts.FlowLayout
import android.util.Log
import android.util.TypedValue
import android.widget.*
import com.example.networkingapp.CurrentOrganization
import com.example.networkingapp.PreviousOrganization
import com.example.networkingapp.util.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TinderCallback? = null
    private var mStorageRef: StorageReference? = null

    val REQUEST_CODE_BASICINFO = 11
    val REQUEST_CODE_INTERESTS = 12
    val REQUEST_CODE_ABOUT = 13
    val REQUEST_CODE_GOALS = 14
    val REQUEST_CODE_EXPERIENCE = 15
    val GALLERY_ID: Int = 16

    var counterSnapshotCurrent = 1
    var counterSnapshotPrevious = 1


    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase().child(userId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Firebase storage
        mStorageRef = FirebaseStorage.getInstance().reference

        progressLayout.setOnTouchListener { view, event -> true }

        // Populate profile
        populateInfo()

        // Signout
        signoutButton.setOnClickListener { callback?.onSignout() }

        //Open Gallery
        photoIV.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }

        // Open BasicInfoActivity when edit icon clicked
        basicInfoEdit.setOnClickListener {
            startBasicInfoActivity()
        }

        // Open BasicInfoActivity when view clicked
        relativeBasicInfo.setOnClickListener {
            startBasicInfoActivity()
        }

        // Open InterestsActivity on click
        interestsContainer.setOnClickListener {
            startInterestsActivity()

        }

        // Open GoalsActivity on click
        relativegoals.setOnClickListener {
            startGoalsActivity()
        }

        // Open ExperienceActivity on click
        relativeexp.setOnClickListener {
            startExperienceActivity()
        }

        // Open AboutActivity on click
        relativeabout.setOnClickListener {
            startAboutActivity()
        }

        // Open InstagramActivity on click
        instagramicon.setOnClickListener {
            val intentInstagram = Intent(getActivity(), InstagramActivity::class.java)
            startActivity(intentInstagram)
        }

        // Open LinkedinActivity on click
        linkedinicon.setOnClickListener {
            val intentLinkedin = Intent(getActivity(), LinkedinActivity::class.java)
            startActivity(intentLinkedin)
        }

        // Open WebActivity on click
        wwwicon.setOnClickListener {
            val intentWeb = Intent(getActivity(), WebActivity::class.java)
            startActivity(intentWeb)
        }
    }

    fun populateInfo() {

        progressLayout.visibility = View.VISIBLE
        profileLayout.visibility = View.GONE

        userDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                progressLayout.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (isAdded) {


                    // Removing views before each load, so the views won't duplicate
                    flowInterests.removeAllViews()
                    flowGoals.removeAllViews()
                    expContainer.removeAllViews()


                    val user = p0.getValue(User::class.java)

                    Log.d("UserObj", user.toString())

                    // Basic info
                    profileName.setText(user?.name, TextView.BufferType.NORMAL)
                    profileProfession.setText(user?.profession, TextView.BufferType.NORMAL)
                    profileLocation.setText(user?.location, TextView.BufferType.NORMAL)

                    var image = p0.child("image").value.toString()
                    var thumbnail = p0.child("thumb_image").value.toString()

                    var countPreviousChildren = p0.child("previousOrg").childrenCount.toInt()
                    var countCurrentChildren = p0.child("currentOrg").childrenCount.toInt()

                    if (!thumbnail!!.equals("default")) {
                        Picasso.with(context).load(thumbnail).placeholder(R.drawable.profile_pic)
                            .into(photoIV, object : Callback {
                                override fun onError() {
                                    Log.d("TAG", "error")
                                }

                                override fun onSuccess() {

                                    Log.d("TAG", "success")
                                }
                            })
                    }

                    // Interests
                    for (snapshotInterest in p0.child("interestedIn").children) {

                        var interestsFromDB = snapshotInterest.getValue(String::class.java)

                        populateInterests(interestsFromDB)
                    }

                    // Goals
                    for (snapshotGoal in p0.child("goals").children) {

                        var goalFromDB = snapshotGoal.getValue(String::class.java)

                        populateGoals(goalFromDB)
                    }

                    // Current Organization
                    for (snapshotCurrentOrg in p0.child("currentOrg").children) {

                        var currentFromDB =
                            snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                        if (countPreviousChildren == 0) {

                            populateCurrentOrgIfNoPreviousOrg(currentFromDB, countCurrentChildren)
                        } else {

                            populateCurrentOrg(currentFromDB)
                        }
                    }

                    for (snapshotPreviousOrg in p0.child("previousOrg").children) {

                        var previousFromDB =
                            snapshotPreviousOrg.getValue(PreviousOrganization::class.java)

                        populatePreviousOrg(previousFromDB, countPreviousChildren)

                    }

                    aboutProfile.setText(user?.about, TextView.BufferType.NORMAL)


                    progressLayout.visibility = View.GONE
                    profileLayout.visibility = View.VISIBLE
                }
            }
        })
    }


    fun startBasicInfoActivity() {
        val intent = Intent(getActivity(), BasicInfoActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_BASICINFO)
    }

    fun startInterestsActivity() {
        val intent = Intent(getActivity(), InterestsActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_INTERESTS)
    }

    fun startGoalsActivity() {
        val intent = Intent(getActivity(), GoalsActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_GOALS)
    }

    fun startAboutActivity() {
        val intent = Intent(getActivity(), AboutActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ABOUT)
    }

    fun startExperienceActivity() {
        val intent = Intent(getActivity(), ExperienceActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_EXPERIENCE)
    }

    fun populateInterests(interestName: String?) {

        val textView = TextView(getActivity(), null, 0, R.style.interestsProfile)

        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        params.setMargins(0, 20, 20, 0)
        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.layoutParams = params
        textView.setText(interestName)
        flowInterests.addView(textView)
    }

    fun populateGoals(goalName: String?) {

        val textView = TextView(getActivity(), null, 0, R.style.goalsProfile)

        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define view height
        )

        params.setMargins(0, 30, 20, 0)

        textView.layoutParams = params
        textView.gravity = Gravity.CENTER
        textView.setText(goalName)

        addImageToGoal(goalName, textView)
        textView.compoundDrawablePadding = 15

        flowGoals.addView(textView)
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

    fun populateCurrentOrgIfNoPreviousOrg(
        currentOrganization: CurrentOrganization?,
        childrenCounter: Int
    ) {


        if (counterSnapshotCurrent != childrenCounter) {

            companyNameCurrent(currentOrganization)
            jobTitleCurrent(currentOrganization)
            startingDateCurrent(currentOrganization)
            horizontalSeparator()

            counterSnapshotCurrent++

        } else {

            companyNameCurrent(currentOrganization)
            jobTitleCurrent(currentOrganization)
            startingDateCurrent(currentOrganization)

            counterSnapshotCurrent = 1
        }
    }

    fun populateCurrentOrg(currentOrganization: CurrentOrganization?) {

        companyNameCurrent(currentOrganization)
        jobTitleCurrent(currentOrganization)
        startingDateCurrent(currentOrganization)
        horizontalSeparator()
    }

    fun populatePreviousOrg(previousOrganization: PreviousOrganization?, countChildren: Int) {

        if (counterSnapshotPrevious != countChildren) {

            companyNamePrevious(previousOrganization)
            jobTitlePrevious(previousOrganization)
            datePrevious(previousOrganization)
            horizontalSeparator()

            counterSnapshotPrevious++

        } else {

            companyNamePrevious(previousOrganization)
            jobTitlePrevious(previousOrganization)
            datePrevious(previousOrganization)

            counterSnapshotPrevious = 1
        }
    }

    fun companyNameCurrent(currentOrganization: CurrentOrganization?) {

        val companyName = TextView(getActivity(), null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(currentOrganization!!.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        expContainer.addView(companyName)
    }

    fun jobTitleCurrent(currentOrganization: CurrentOrganization?) {

        val jobTitle = TextView(getActivity(), null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(currentOrganization!!.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        expContainer.addView(jobTitle)
    }

    fun startingDateCurrent(currentOrganization: CurrentOrganization?) {

        val startingDate = TextView(getActivity(), null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        startingDate.layoutParams = paramsDate
        startingDate.gravity = Gravity.CENTER
        startingDate.setText(currentOrganization!!.startDate.toString().plus(" - Present"))
        startingDate.setTextColor(Color.parseColor("#545454"))
        startingDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        expContainer.addView(startingDate)
    }

    fun companyNamePrevious(previousOrganization: PreviousOrganization?) {

        val companyName = TextView(getActivity(), null, 0)


        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 10, 0, 10)
        companyName.layoutParams = params
        companyName.gravity = Gravity.CENTER
        companyName.setText(previousOrganization!!.company.toString())
        companyName.setTextColor(Color.parseColor("#545454"))
        companyName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        companyName.setTypeface(null, Typeface.BOLD)

        expContainer.addView(companyName)
    }

    fun jobTitlePrevious(previousOrganization: PreviousOrganization?) {

        val jobTitle = TextView(getActivity(), null, 0)

        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        paramsTitle.setMargins(0, 10, 0, 10)
        jobTitle.layoutParams = paramsTitle
        jobTitle.gravity = Gravity.CENTER
        jobTitle.setText(previousOrganization!!.title.toString())
        jobTitle.setTextColor(Color.parseColor("#545454"))
        jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        expContainer.addView(jobTitle)
    }

    fun datePrevious(previousOrganization: PreviousOrganization?) {

        val textViewDate = TextView(getActivity(), null, 0)

        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
        )

        paramsDate.setMargins(0, 10, 0, 10)
        textViewDate.layoutParams = paramsDate
        textViewDate.gravity = Gravity.CENTER
        textViewDate.setText(
            previousOrganization!!.startDate.toString().plus(" - ").plus(
                previousOrganization!!.endDate
            )
        )
        textViewDate.setTextColor(Color.parseColor("#545454"))
        textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        expContainer.addView(textViewDate)
    }

    fun horizontalSeparator() {

        val horizontalSeparator = View(getActivity(), null, 0)

        var paramsSeparator: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            2
        )

        paramsSeparator.setMargins(0, 10, 0, 10)
        horizontalSeparator.layoutParams = paramsSeparator
        horizontalSeparator.setBackgroundColor(Color.parseColor("#c0c0c0"))
        expContainer.addView(horizontalSeparator)
    }


    @Suppress("RedundantOverride")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_ID && resultCode == RESULT_OK) {

            var image: Uri = data!!.data
            CropImage.activity(image).setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1).start(context!!, this)

        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode === RESULT_OK) {


                var dialog = AlertDialog.Builder(context)
                var dialogView = layoutInflater.inflate(R.layout.image_load, null)
                dialog.setView(dialogView)
                dialog.setCancelable(false)
                var dialogObject = dialog.create()
                dialogObject.show()
                //dialogObject.window.setGravity(Gravity.CENTER)
                //dialogObject.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 500)


                val resultUri = result.uri

                var userId = userId
                var thumbFile = File(resultUri.path)

                var thumbBitmap =
                    Compressor(context).setMaxWidth(200).setMaxHeight(200).setQuality(65)
                        .compressToBitmap(thumbFile)

                //upload images to firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)

                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mStorageRef!!.child("profile_images").child(userId + ".jpeg")

                //another directory for thumb images
                var thumbFilePath =
                    mStorageRef!!.child("profile_images").child("thumbs").child(userId + ".jpeg")


                filePath.putFile(resultUri)
                    .addOnSuccessListener {

                            taskSnapshot ->

                        filePath.downloadUrl.addOnCompleteListener { taskSnapshot ->

                            var downloadUrl = taskSnapshot.result.toString()

                            Log.d("URL", downloadUrl)


                            //upload image

                            var uploadTask: UploadTask = thumbFilePath.putBytes(thumbByteArray)

                            uploadTask.addOnCompleteListener { task ->

                                thumbFilePath.downloadUrl.addOnCompleteListener { task ->

                                    var thumbUrl = task.result.toString()

                                    Log.d("thumbURL", thumbUrl)


                                    if (task.isSuccessful) {

                                        var updateObj = HashMap<String, Any>()
                                        updateObj.put("image", downloadUrl)
                                        updateObj.put("thumb_image", thumbUrl)


                                        //save profile image

                                        userDatabase!!.updateChildren(updateObj)
                                            .addOnCompleteListener {

                                                    task: Task<Void> ->
                                                if (task.isSuccessful) {
                                                    dialogObject.dismiss()
                                                }
                                            }
                                    }
                                }
                            }
                        }
                    }
            }

        }
    }
}
