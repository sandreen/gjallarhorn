package com.gjallarhorn.app
import com.google.firebase.storage.FirebaseStorage

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.R.attr.start
import androidx.test.runner.intent.IntentStubberRegistry.reset
import java.io.FileInputStream
import java.io.IOException
import android.media.MediaPlayer
import java.io.FileOutputStream


class filePull : AppCompatActivity(){

    lateinit var storage: FirebaseStorage

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = FirebaseStorage.getInstance()


    }*/

    //retrieve an individual file from firebase
    fun individualPull(fileName: String){
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val mediaPlayer = MediaPlayer()


        val refPath = "Sounds/" + fileName


        val islandRef = storageRef.child(refPath)

        val localFile = File.createTempFile("yee", "mp3")

        islandRef.getFile(localFile).addOnSuccessListener {
            try {
                // create temp file that will hold byte array
                val tempMp3 = File.createTempFile("kurchina", "mp3", cacheDir)
                tempMp3.deleteOnExit()
                // resetting mediaplayer instance to evade problems
                mediaPlayer.reset()

                // In case you run into issues with threading consider new instance like:
                // MediaPlayer mediaPlayer = new MediaPlayer();

                // Tried passing path directly, but kept getting
                // "Prepare failed.: status=0x1"
                // so using file descriptor instead
                val fis = FileInputStream(tempMp3)
                mediaPlayer.setDataSource(fis.getFD())
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (ex: IOException) {
                val s = ex.toString()
                ex.printStackTrace()
            }

        }.addOnFailureListener {

        }
    }

}