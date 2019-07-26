package com.gjallarhorn.app

import android.Manifest
import kotlinx.android.synthetic.main.activity_audio_recording.*
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.view.MotionEvent
import android.os.Bundle
import android.view.View
import android.content.pm.PackageManager

import android.widget.Toast
import androidx.core.app.ActivityCompat

@SuppressLint("ByteOrderMark")
class AudioRecordingActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null

    private var audioFilePath: String? = null
    private var audioFileName: String? = null
    private var isRecording = false
    private var audioRecorded = false

    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_recording)

//        mediaPlayer = MediaPlayer.create(this, R.raw.tool)
//        mediaPlayer?.setOnPreparedListener {
//            println("Ready to go!")
//        }

        audioSetup()

        pushButton.setOnTouchListener { _, event ->
            handleTouch(event)
                true
        }
    }

    /* Sends a request to phone to learn about the status of requested permission */
    private fun requestPermission(permissionType: String, requestCode: Int) {
        val permission = this.checkSelfPermission(permissionType)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, arrayOf(permissionType), requestCode)
        }
    }

    /* If the phone does not have Record and Write to Storage permissions, ask for them */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //recordButton.isEnabled = false

                    Toast.makeText(this, "Record permission required", Toast.LENGTH_LONG).show()
                }
                else{
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST_CODE)
                }
                return
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    //recordButton.isEnabled = false

                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    /* Function to check if phone as a microphone */
    private fun hasMicrophone(): Boolean {
        val pManager = this.packageManager
        return pManager.hasSystemFeature(
            PackageManager.FEATURE_MICROPHONE)
    }

    /* Sets up the app ensuring the phone as a microphone and the app has the proper permission */
    private fun audioSetup() {
        if (!hasMicrophone()) {
//            stopButton.isEnabled = false
//            playButton.isEnabled = false
//            recordButton.isEnabled = false
        } else {
//            playButton.isEnabled = false
//            stopButton.isEnabled = false
        }

        // Saves in My Files > Internal Storage > Android > data > com.gjallarhorn.app > files
        audioFilePath = getExternalFilesDir(null)
            ?.absolutePath + "/tempaudio.m4a"

        requestPermission(Manifest.permission.RECORD_AUDIO,
            RECORD_REQUEST_CODE)
    }

    private fun handleTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                println("down")

                if(!audioRecorded) {
                    isRecording = true

                    try {
                        mediaRecorder = MediaRecorder()
                        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        mediaRecorder?.setOutputFile(audioFilePath)
                        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        mediaRecorder?.prepare()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    mediaRecorder?.start()
                }
                else
                {
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDataSource(audioFilePath)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                println("up or cancel")

                if (isRecording && !audioRecorded)
                {
                    //recordButton.isEnabled = false
                    mediaRecorder?.stop()
                    mediaRecorder?.release()
                    mediaRecorder = null

                    isRecording = false
                    audioRecorded = true

                    textView.text = getString(R.string.playback_description)
                }
                else if (audioRecorded)
                {
                    mediaPlayer?.pause()
                    mediaPlayer?.seekTo(0)
                }
                else
                {
                    mediaPlayer?.release()
                    mediaPlayer = null
                    //recordButton.isEnabled = true
                }
            }
            else -> {
                println("other")
            }
        }
    }

    /* Saves the media file to the Gjallarhorn folder for future use and upload */
    fun saveMedia(view: View){
        var saveDialog = SaveRecordingDialogFragment()
        saveDialog.show(supportFragmentManager, "SaveRecordingDialogFragment")
    }

    /* Resets the media recorder and player when the 'rerecord' button is pushed */
    fun resetMedia(view: View) {
        mediaRecorder?.release()
        mediaRecorder = null
        mediaRecorder?.reset()

        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer?.reset()

        audioRecorded = false

        if(textView.text !== getString(R.string.record_description))
            textView.text = getString(R.string.record_description)
    }

}
