package com.gjallarhorn.app

import kotlinx.android.synthetic.main.dialog_fragment_save_recording.*
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.File

class SaveRecordingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            // Use the inflated class to turn the custom fragment xml into a dialog
            val inflater = requireActivity().layoutInflater


            builder.setView(inflater.inflate(R.layout.dialog_fragment_save_recording, null))
                .setTitle(R.string.saveRecordingDialog)
                .setMessage(R.string.saveRecordingDescription)
                .setPositiveButton(R.string.saveRecordingPositive,
                    DialogInterface.OnClickListener { _, _ ->
                        try {
                            if (dialog?.recordingTitle != null) {
                                val audioFileName = dialog?.recordingTitle?.text.toString()

                                if(saveRecording(audioFileName)) {
                                    Toast.makeText(requireContext(), "Saved.", Toast.LENGTH_SHORT).show()

                                    returnToMainActivity()
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

    private fun saveRecording(audioFileName: String): Boolean{
        try {
            var audioFilePath = requireActivity().getExternalFilesDir(null)
                ?.absolutePath

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

    /* Takes the user back to the main alarm activity when the 'cancel' button is pushed */
    private fun returnToMainActivity() {
        val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)

        startActivity(mainActivityIntent)
    }
}