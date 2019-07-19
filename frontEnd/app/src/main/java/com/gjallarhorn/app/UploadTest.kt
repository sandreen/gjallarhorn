package com.gjallarhorn.app

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
//import javax.xml.parsers.parse
import kotlinx.android.synthetic.main.activity_upload_test.*
import java.lang.NullPointerException
import java.net.URI
import java.util.*

class UploadTest : AppCompatActivity() {


    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null

    var blah = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_test)


          storage = FirebaseStorage.getInstance()

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

        }
    }


    fun recordSound() {

    }


    fun uploadSound(view: View) {
        var storageReference = FirebaseStorage.getInstance().reference

        if(filePath != null) {
            //set to the name of
            val fileUri: Uri? = null

            storageReference.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    val name = taskSnapshot.metadata!!.name
                    var url = taskSnapshot.getMetadata()?.getReference()?.getDownloadUrl().toString()


                }
                .addOnFailureListener { exception ->
                    // Handle unsuccessful uploads
                }
                .addOnProgressListener { taskSnapshot ->
                    // taskSnapshot.bytesTransferred
                    // taskSnapshot.totalByteCount
                }
                .addOnPausedListener { taskSnapshot ->
                    // Upload is paused
                }

        }else{
        Toast.makeText(this, "Record a Sound First!", Toast.LENGTH_LONG).show()
        }


    }








}
