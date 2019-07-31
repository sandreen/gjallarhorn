package com.gjallarhorn.app

import android.util.Log
import java.io.File
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore

class PullAlarmSound {
    lateinit var storage: FirebaseStorage
    lateinit var database: FirebaseFirestore

    //retrieve an individual file from firebase
    fun pullRandomSound(fileCallBack: (File) -> Unit) {
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

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

                val randNum = (0 until listURL.size - 1).random()
                val storageURL = listURL[randNum]
                val selectedName = listName[randNum]
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
