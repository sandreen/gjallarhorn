package com.gjallarhorn.app

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_storage_test.*
import java.io.File

class StorageTest : AppCompatActivity() {


    private var storage: FirebaseStorage? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_test)
        setSupportActionBar(toolbar)

        storage = FirebaseStorage.getInstance()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
    fun writeToDB(view: View){

    }



    fun upload(view: View) {

            var storageReference = FirebaseStorage.getInstance().reference.child("Sounds")
            var fileName  = "nogod.mp3"
            var filePath = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString()
            var myExternalFile: File = File((filePath), fileName)
            val fileUri: Uri? = Uri.fromFile(myExternalFile)
            val ref = storageReference.child(fileName)

            if(filePath != null) {
                ref.putFile(fileUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        val name = taskSnapshot.metadata!!.name
                        var url = taskSnapshot.getMetadata()?.getReference()?.getDownloadUrl().toString()

                        Toast.makeText(this, "file uploaded!", Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        // taskSnapshot.bytesTransferred
                        // taskSnapshot.totalByteCount
                        Toast.makeText(this, "progressing", Toast.LENGTH_LONG).show()
                    }
                    .addOnPausedListener { taskSnapshot ->
                        Toast.makeText(this, "paused", Toast.LENGTH_LONG).show()
                        // Upload is paused
                    }

            }else{
            Toast.makeText(this, "Record a Sound First!", Toast.LENGTH_LONG).show()
            }
        }

















}
