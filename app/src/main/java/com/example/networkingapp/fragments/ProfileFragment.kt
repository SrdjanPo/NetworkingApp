package com.example.networkingapp.fragments

import android.app.ActionBar
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import com.example.networkingapp.util.DATA_USERS
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
    private lateinit var database: DatabaseReference
    private lateinit var currentOrgDB: DatabaseReference
    private var mStorageRef: StorageReference? = null
    private var callback: TinderCallback? = null

    val REQUEST_CODE_BASICINFO = 11
    val REQUEST_CODE_INTERESTS = 12
    val REQUEST_CODE_ABOUT = 13
    val REQUEST_CODE_GOALS = 14
    val REQUEST_CODE_EXPERIENCE = 15
    val GALLERY_ID: Int = 16

    var counterSnapshot = 1

    var hashMap = HashMap<String, Int?>()

    companion object {
        val company = arrayListOf<String?>()
        val companyTitle = arrayListOf<String?>()
        val date = arrayListOf<String?>()
        val prevCompany = arrayListOf<String?>()
        val prevTitle = arrayListOf<String?>()
        val prevStartDate = arrayListOf<String?>()
        val prevEndDate = arrayListOf<String?>()

    }

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

        mStorageRef = FirebaseStorage.getInstance().reference

        progressLayout.setOnTouchListener { view, event -> true }

        populateInfo()

        // Signout button
        signoutButton.setOnClickListener { callback?.onSignout() }

        //Open Gallery

        photoIV.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
        // Open BasicInfoActivity

        basicInfoEdit.setOnClickListener {
            startBasicInfoActivity()
        }
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
            //val intentExperience = Intent(getActivity(), ExperienceActivity::class.java)
            //startActivity(intentExperience)

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

                    flowInterests.removeAllViews()
                    flowGoals.removeAllViews()
                    expContainer.removeAllViews()


                    val user = p0.getValue(User::class.java)

                    // Basic info
                    profileName.setText(user?.name, TextView.BufferType.NORMAL)
                    profileProfession.setText(user?.profession, TextView.BufferType.NORMAL)
                    profileLocation.setText(user?.location, TextView.BufferType.NORMAL)

                    var image = p0.child("image").value.toString()
                    var thumbnail = p0.child("thumb_image").value.toString()

                    if (!image!!.equals("default")) {
                        Picasso.with(context).load(image).noPlaceholder().into(photoIV,  object :Callback{
                            override fun onError() {
                                Log.d("TAG", "error")
                            }

                            override fun onSuccess() {

                                Log.d("TAG", "success")

                            }

                        })
                    }


                    for (snapshot in p0.child("interestedIn").children) {


                        var interestsFromDB = snapshot.getValue(String::class.java)

                        val textView = TextView(getActivity(), null, 0, R.style.interestsProfile)

                        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params.setMargins(0, 20, 20, 0)

                        textView.layoutParams = params

                        textView.gravity = Gravity.CENTER

                        textView.layoutParams = params

                        textView.setText(interestsFromDB)

                        flowInterests.addView(textView)
                    }

                    for (snapshotGoal in p0.child("goals").children) {

                        var goalFromDB = snapshotGoal.getValue(String::class.java)

                        val textView = TextView(getActivity(), null, 0, R.style.goalsProfile)

                        var params: FlowLayout.LayoutParams = FlowLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params.setMargins(0, 30, 20, 0)

                        textView.layoutParams = params
                        textView.gravity = Gravity.CENTER
                        textView.setText(goalFromDB)

                        if (goalFromDB == "Hire employees") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_hireemployeesicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Looking for a job") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_lookingforajobicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Find Co-Founders") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_findcofoundersicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Invest in projects") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_investinprojectsicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Find investors") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_suit1,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Grow my business") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_plant1,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Hire freelancers") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_hirefreelancersicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Find freelance jobs") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_findfreelancejobsicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Find mentors") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_owl2,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Mentor others") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_mentorothersicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Make new friends") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_makenewfriendsicon,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        } else if (goalFromDB == "Explore ideas") {

                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_explorenewideas,
                                0,
                                0,
                                0
                            )
                            textView.compoundDrawablePadding = 15
                        }

                        flowGoals.addView(textView)

                    }

                    for (snapshot in p0.child("currentOrg").children) {

                        var currentFromDB = snapshot.getValue(CurrentOrganization::class.java)

                        val textView = TextView(getActivity(), null, 0)

                        var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        params.setMargins(0, 10, 0, 10)
                        textView.layoutParams = params
                        textView.gravity = Gravity.CENTER
                        textView.setText(currentFromDB!!.company.toString())
                        textView.setTextColor(Color.parseColor("#545454"))
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        textView.setTypeface(null, Typeface.BOLD)

                        expContainer.addView(textView)

                        val textViewTitle = TextView(getActivity(), null, 0)

                        var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        paramsTitle.setMargins(0, 10, 0, 10)
                        textViewTitle.layoutParams = paramsTitle
                        textViewTitle.gravity = Gravity.CENTER
                        textViewTitle.setText(currentFromDB!!.title.toString())
                        textViewTitle.setTextColor(Color.parseColor("#545454"))
                        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                        expContainer.addView(textViewTitle)

                        val textViewDate = TextView(getActivity(), null, 0)

                        var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                        )

                        paramsDate.setMargins(0, 10, 0, 10)
                        textViewDate.layoutParams = paramsDate
                        textViewDate.gravity = Gravity.CENTER
                        textViewDate.setText(currentFromDB!!.startDate.toString().plus(" - Present"))
                        textViewDate.setTextColor(Color.parseColor("#545454"))
                        textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                        expContainer.addView(textViewDate)

                        val horizontalSeparator = View(getActivity(), null, 0)

                        var paramsSeparator: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                            2 // This will define text view height
                        )

                        paramsSeparator.setMargins(0, 10, 0, 10)
                        horizontalSeparator.layoutParams = paramsSeparator
                        horizontalSeparator.setBackgroundColor(Color.parseColor("#545454"))
                        expContainer.addView(horizontalSeparator)
                    }

                    for (snapshot in p0.child("previousOrg").children) {

                        val countChildren = p0.child("previousOrg").childrenCount

                        if (counterSnapshot != countChildren.toInt()) {

                            var previousFromDB = snapshot.getValue(PreviousOrganization::class.java)

                            val textView = TextView(getActivity(), null, 0)

                            var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            params.setMargins(0, 10, 0, 10)
                            textView.layoutParams = params
                            textView.gravity = Gravity.CENTER
                            textView.setText(previousFromDB!!.company.toString())
                            textView.setTextColor(Color.parseColor("#545454"))
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            textView.setTypeface(null, Typeface.BOLD)

                            expContainer.addView(textView)

                            val textViewTitle = TextView(getActivity(), null, 0)

                            var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            paramsTitle.setMargins(0, 10, 0, 10)
                            textViewTitle.layoutParams = paramsTitle
                            textViewTitle.gravity = Gravity.CENTER
                            textViewTitle.setText(previousFromDB.title.toString())
                            textViewTitle.setTextColor(Color.parseColor("#545454"))
                            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                            expContainer.addView(textViewTitle)

                            val textViewDate = TextView(getActivity(), null, 0)

                            var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            paramsDate.setMargins(0, 10, 0, 10)
                            textViewDate.layoutParams = paramsDate
                            textViewDate.gravity = Gravity.CENTER
                            textViewDate.setText(
                                previousFromDB.startDate.toString().plus(" - ").plus(
                                    previousFromDB.endDate
                                )
                            )
                            textViewDate.setTextColor(Color.parseColor("#545454"))
                            textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                            expContainer.addView(textViewDate)

                            val horizontalSeparator = View(getActivity(), null, 0)

                            var paramsSeparator: LinearLayout.LayoutParams =
                                LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                    2 // This will define text view height
                                )

                            paramsSeparator.setMargins(0, 10, 0, 10)
                            horizontalSeparator.layoutParams = paramsSeparator
                            horizontalSeparator.setBackgroundColor(Color.parseColor("#545454"))
                            expContainer.addView(horizontalSeparator)

                            counterSnapshot++

                        } else {

                            var previousFromDB = snapshot.getValue(PreviousOrganization::class.java)

                            val textView = TextView(getActivity(), null, 0)

                            var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            params.setMargins(0, 10, 0, 10)
                            textView.layoutParams = params
                            textView.gravity = Gravity.CENTER
                            textView.setText(previousFromDB!!.company.toString())
                            textView.setTextColor(Color.parseColor("#545454"))
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            textView.setTypeface(null, Typeface.BOLD)

                            expContainer.addView(textView)

                            val textViewTitle = TextView(getActivity(), null, 0)

                            var paramsTitle: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            paramsTitle.setMargins(0, 10, 0, 10)
                            textViewTitle.layoutParams = paramsTitle
                            textViewTitle.gravity = Gravity.CENTER
                            textViewTitle.setText(previousFromDB.title.toString())
                            textViewTitle.setTextColor(Color.parseColor("#545454"))
                            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                            expContainer.addView(textViewTitle)

                            val textViewDate = TextView(getActivity(), null, 0)

                            var paramsDate: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, // This will define text view width
                                ViewGroup.LayoutParams.WRAP_CONTENT // This will define text view height
                            )

                            paramsDate.setMargins(0, 10, 0, 10)
                            textViewDate.layoutParams = paramsDate
                            textViewDate.gravity = Gravity.CENTER
                            textViewDate.setText(
                                previousFromDB.startDate.toString().plus(" - ").plus(
                                    previousFromDB.endDate
                                )
                            )
                            textViewDate.setTextColor(Color.parseColor("#545454"))
                            textViewDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                            expContainer.addView(textViewDate)

                            counterSnapshot = 1

                        }
                    }

                    /*for(snapshotCurrentOrg in p0.child("currentOrg").children) {

                        var currentOrgDB = snapshotCurrentOrg.getValue(CurrentOrganization::class.java)

                        company.add(currentOrgDB?.company)
                        companyTitle.add(currentOrgDB?.title)
                        date.add(currentOrgDB?.startDate)
                    }*/


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


    @Suppress("RedundantOverride")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_ID && resultCode == RESULT_OK) {

            var image: Uri = data!!.data
            CropImage.activity(image).setCropShape(CropImageView.CropShape.OVAL).setAspectRatio(1, 1).start(context!!, this)

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


        if (requestCode == REQUEST_CODE_BASICINFO && resultCode == RESULT_OK) {
            val name = data?.getStringExtra(BasicInfoActivity.INPUT_NAME)
            val profession = data?.getStringExtra(BasicInfoActivity.INPUT_PROFESSION)
            val location = data?.getStringExtra(BasicInfoActivity.INPUT_LOCATION)
            profileName.text = name
            profileProfession.text = profession
            profileLocation.text = location
        }
        /*if (requestCode == REQUEST_CODE_INTERESTS && resultCode == RESULT_OK) {


            val t = activity!!.supportFragmentManager.beginTransaction()
            t.setReorderingAllowed(false)
            t.detach(this).attach(this).commitAllowingStateLoss()

        }*/

        /*if (requestCode == REQUEST_CODE_GOALS && resultCode == RESULT_OK) {

            val t = activity!!.supportFragmentManager.beginTransaction()
            t.setReorderingAllowed(false)
            t.detach(this).attach(this).commitAllowingStateLoss()

        }

        if (requestCode == REQUEST_CODE_EXPERIENCE && resultCode == RESULT_OK) {

            val t = activity!!.supportFragmentManager.beginTransaction()
            t.setReorderingAllowed(false)
            t.detach(this).attach(this).commitAllowingStateLoss()

            //scrollView.scrollTo(0, relativeexp.y.toInt())

            *//*scrollView.postDelayed( Runnable() {
                  run() {
                    scrollView.scrollTo(0, relativeexp.y.toInt());
                }
            }, 1000)*//*

        }*/

        if (requestCode == REQUEST_CODE_ABOUT && resultCode == RESULT_OK) {

            val about = data?.getStringExtra(AboutActivity.INPUT_ABOUT)

            aboutProfile.setText(about)
        }
    }
}
