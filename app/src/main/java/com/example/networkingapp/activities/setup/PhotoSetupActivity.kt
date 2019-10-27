package com.example.networkingapp.activities.setup

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.networkingapp.R
import com.example.networkingapp.activities.TinderActivity
import com.example.networkingapp.activities.TinderCallback
import com.example.networkingapp.util.DATA_USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_photo_setup.*
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoSetupActivity : AppCompatActivity() {

    val GALLERY_ID: Int = 16

    private lateinit var database: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid
    private var mStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_setup)

        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile Setup"

        progressBar.progress = 90

        database = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        // Firebase storage
        mStorageRef = FirebaseStorage.getInstance().reference

        populatePhoto()

        photoIV.setOnClickListener {

            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }

        finishButton.setOnClickListener {

            startActivity(TinderActivity.newIntent(this))
            finishAffinity()
        }

        addPhotoButton.setOnClickListener {

            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }

    fun populatePhoto() {

        database.child(userId!!).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                var image = p0.child("image").value.toString()
                var thumbnail = p0.child("thumb_image").value.toString()

                if (!image!!.equals("default")) {
                    Picasso.with(applicationContext).load(image).placeholder(R.drawable.profile_pic)
                        .into(photoIV, object : Callback {
                            override fun onError() {
                                Log.d("TAG", "error")
                            }

                            override fun onSuccess() {

                                Log.d("TAG", "success")
                            }
                        })
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri = data!!.data
            CropImage.activity(image).setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1).start(this!!)

        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {


                var dialog = AlertDialog.Builder(this)
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
                    Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(65)
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

                                        database.child(userId!!).updateChildren(updateObj)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
