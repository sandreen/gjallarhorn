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

    fun individualPull(view: View) {
        val mediaPlayer = MediaPlayer()
        pullRandomSound() {
            try {
                val localFile = it
                localFile.deleteOnExit()
                mediaPlayer.reset()
                val fis = FileInputStream(localFile)
                mediaPlayer.setDataSource(fis.getFD())
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (ex: IOException) {
                val s = ex.toString()
                ex.printStackTrace()
            }
        }
    }


    //retrieve an individual file from firebase
    fun pullRandomSound(fileCallBack: (File) -> Unit){
        val database = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        database.collection("Alarms")
            .get()
            .addOnSuccessListener{ documents ->
                val listURL: MutableList<String> = mutableListOf()
                val listName: MutableList<String> = mutableListOf()

                for (document in documents){
                    listURL.add(document.getString("URL").toString())
                    listName.add(document.getString("AlarmName").toString())
                    Log.d("Firebase: ", "${document.id} => ${document.data}")

                }

                val randNum = (0..listURL.size-1).random()
                val storageURL = listURL.get(randNum)
                val selectedName = listName.get(randNum)
                Log.d("Firebase: ", storageURL)

                val httpsReference = storage.getReferenceFromUrl(storageURL)
                val localFile = File.createTempFile(selectedName, "m4a")

                httpsReference.getFile(localFile).addOnSuccessListener {
                    fileCallBack(localFile)
                }.addOnFailureListener {
                    //dont worry about it
                }
        }
    }

}
