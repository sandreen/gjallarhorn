package com.gjallarhorn.app

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_test.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL


class testActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    //retrieve an individual file from firebase
    fun individualPull(view: View){
        val database = FirebaseFirestore.getInstance()

        val alarmTitle = "disgust"
        val dataRef = database.collection("Alarms")
            //.whereEqualTo("AlarmName", alarmTitle)
            .get()
            .addOnSuccessListener{ documents ->
                val listURL: MutableList<String> = mutableListOf<String>()
                val listName: MutableList<String> = mutableListOf<String>()

                for (document in documents){
                    listURL.add(document.getString("URL").toString())
                    listName.add(document.getString("AlarmName").toString())
                    Log.d("Firebase: ", "${document.id} => ${document.data}")

                }

                val randNum = (0..listURL.size-1).random()
                val storageURL = listURL.get(randNum)
                val selectedName = listName.get(randNum)
                Log.d("Firebase: ", storageURL)
                pullFromURL(storageURL, selectedName)
        }
    }

    fun pullFromURL(storageURL:String, name:String){
        val storage = FirebaseStorage.getInstance()
        val httpsReference = storage.getReferenceFromUrl(storageURL)
        val mediaPlayer = MediaPlayer()

        val localFile = File.createTempFile(name, "m4a")

        httpsReference.getFile(localFile).addOnSuccessListener {
            try {
                // create temp file that will hold byte array
                //val tempMp3 = File.createTempFile("kurchina", "mp3", cacheDir)
                localFile.deleteOnExit()
                // resetting mediaplayer instance to evade problems
                mediaPlayer.reset()

                // In case you run into issues with threading consider new instance like:
                // MediaPlayer mediaPlayer = new MediaPlayer();

                // Tried passing path directly, but kept getting
                // "Prepare failed.: status=0x1"
                // so using file descriptor instead
                val fis = FileInputStream(localFile)
                mediaPlayer.setDataSource(fis.getFD())
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (ex: IOException) {
                val s = ex.toString()
                ex.printStackTrace()
            }

        }.addOnFailureListener {
            //dont worry about it
        }
    }

}
