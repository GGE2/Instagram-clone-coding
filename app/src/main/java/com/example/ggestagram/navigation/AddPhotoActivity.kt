package com.example.ggestagram.navigation

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ggestagram.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //Initialize storage
        storage = FirebaseStorage.getInstance()

        //Open the album
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type="image/*"
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode==Activity.RESULT_OK){
                photoUri = it.data?.data
                addphoto_image.setImageURI(photoUri)

            }
            else{
                finish()
            }
        }.launch(photoPickerIntent)
        //startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        add_photon_btn.setOnClickListener {
            Log.e(TAG,"PICKPCIK")
            contentUpload()
        }

    }

    private fun contentUpload() {

        //Make filename
            var timestamp = SimpleDateFormat("yyyyMMddmmss").format(Date())
            var imageFilename = "Image_" + timestamp + "_.png"
            var  storageRef = storage?.reference?.child("images")?.child(imageFilename)


        //FilreUpload
            storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
                Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_SHORT).show()
            }

    }



    }


