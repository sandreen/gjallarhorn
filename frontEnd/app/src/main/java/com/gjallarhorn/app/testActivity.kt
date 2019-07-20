package com.gjallarhorn.app

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_test.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class testActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage

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
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val mediaPlayer = MediaPlayer()
        val fileName = "Yee.mp3"

        val refPath = "Sounds/" + fileName



        val islandRef = storageRef.child(refPath)

        val localFile = File.createTempFile("yee", "mp3")

        islandRef.getFile(localFile).addOnSuccessListener {
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
