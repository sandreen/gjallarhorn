package com.gjallarhorn.app

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_storage_test.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StorageTest : AppCompatActivity() {


    private var storage: FirebaseStorage? = null
    val db = FirebaseFirestore.getInstance()


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

    fun getDate(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm:ss")
        var answer: String =  current.format(formatter)
        return answer
    }

    fun writeToDB(){
        val tone = hashMapOf(
            "Author" to "Randy Bobandy",
            "Explicit" to "DOH",
            "Language" to "English",
            "Path" to 123,
            "Title" to "Good shit",
            "Date" to getDate()
        )

        db.collection("Alarms")
            .add(tone)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_LONG).show()
            }

            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

    }

    fun inputTest(view: View){


        var test = findViewById<TextInputEditText>(R.id.testInput)
        var testString = test.text.toString()
        Toast.makeText(this, testString, Toast.LENGTH_LONG).show()
    }

    fun findTune(view: View){
        db.collection("Alarms")
//            .whereEqualTo("Title", "Dank Tune")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Toast.makeText(this,  "${document.id} => ${document.data}", Toast.LENGTH_LONG).show()
                    Log.d("test", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
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


                        Toast.makeText(this, "file uploaded" + url, Toast.LENGTH_LONG).show()
                        writeToDB()

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
