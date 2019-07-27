package com.gjallarhorn.app

import android.app.Activity
import kotlinx.android.synthetic.main.dialog_fragment_save_recording.*
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SaveRecordingDialogFragment : DialogFragment() {
   // Firebase Stuff
    private var storage: FirebaseStorage? = null
    private var db: FirebaseFirestore? = null

    var flag = true




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Use the inflated class to turn the custom fragment xml into a dialog
            val inflater = requireActivity().layoutInflater



            //Initialize Firebase Storage and Firestore
            db = FirebaseFirestore.getInstance()
            storage = FirebaseStorage.getInstance()

            builder.setView(inflater.inflate(R.layout.dialog_fragment_save_recording, null))
                .setTitle(R.string.saveRecordingDialog)
                .setMessage(R.string.saveRecordingDescription)
                .setPositiveButton(R.string.saveRecordingPositive,
                    DialogInterface.OnClickListener { _, _ ->
                        try {
                            if (dialog?.recordingTitle != null) {
                                val audioFileName = dialog?.recordingTitle?.text.toString()
                                var audioFilePath = requireActivity().getExternalFilesDir(null)?.absolutePath

                                if(saveRecording(audioFileName, audioFilePath)) {
                                    Toast.makeText(requireContext(), "Saved.", Toast.LENGTH_SHORT).show()
                                    upload(audioFileName, audioFilePath)
                                    if(flag) {
                                        Toast.makeText(requireContext(), "Uploaded your alarm", Toast.LENGTH_SHORT).show()
                                        returnToMainActivity()
                                    } else {
                                        Log.d("FLAG", flag.toString())
                                        Toast.makeText(requireContext(), "Failed to upload", Toast.LENGTH_SHORT).show()
                                    }

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Error saving file. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(requireContext(), "Please input a title.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        catch(e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                        }
                    })
                .setNegativeButton(R.string.saveRecordingNegative,
                    DialogInterface.OnClickListener { _, _ ->

                        Toast.makeText(requireContext(), "Cancelled.",Toast.LENGTH_SHORT).show()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveRecording(audioFileName: String, audioFilePath: String?): Boolean{
        try {

            Toast.makeText(requireContext(), audioFileName, Toast.LENGTH_LONG).show()

            var sourcefile= File(audioFilePath, "/tempaudio.m4a")
            var destfile= File(audioFilePath, "/$audioFileName.m4a")



            return sourcefile.renameTo(destfile)
        }
        catch(e: Exception)
        {
            e.printStackTrace()
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun upload(audioFileName: String, audioFilePath: String?){
        Toast.makeText(requireContext(), "In upload", Toast.LENGTH_LONG).show()
        try {
           if (activity != null && isAdded) {
               var storageReference = FirebaseStorage.getInstance().reference.child("Sounds")
               var fileName = "/$audioFileName.m4a"
               var filePath = audioFilePath
               var soundFile: File = File((filePath), fileName)
               val fileUri: Uri? = Uri.fromFile(soundFile)
               val ref = storageReference.child(fileName)

               //Push the file to firebase storage
               ref.putFile(fileUri!!)
                   .addOnSuccessListener { taskSnapshot ->
                       val name = taskSnapshot.metadata!!.name
                       var url = taskSnapshot.getMetadata()?.getReference()?.getDownloadUrl().toString()
                       Log.d("firebase", "file uploaded")
                        uploadInformer(true)
                   }
                   .addOnFailureListener { e ->
                       Log.d("firebase", e.message)
                       uploadInformer(false)

                   }.addOnProgressListener { taskSnapshot ->
                       // taskSnapshot.bytesTransferred
                       // taskSnapshot.totalByteCount
                       Log.d("firebase", "progressing")
                   }
           } else {
               Toast.makeText(requireContext(), "Activity is null", Toast.LENGTH_LONG).show()
           }
        }
       catch(e: Exception){
           e.printStackTrace()
           Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
       }
    }

    private fun uploadInformer(successFlag: Boolean){
        Log.d("FLAG", flag.toString())
        flag = successFlag
        Log.d("FLAG", flag.toString())
    }


    /* Takes the user back to the main alarm activity when the 'cancel' button is pushed */
    private fun returnToMainActivity() {
        val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
        startActivity(mainActivityIntent)
    }
}