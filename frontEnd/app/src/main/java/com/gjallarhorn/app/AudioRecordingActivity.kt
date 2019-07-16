package com.gjallarhorn.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_audio_recording.pushButton
import android.media.MediaPlayer
import android.view.MotionEvent
import android.os.Bundle
import android.view.View

class AudioRecordingActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_recording)

        mediaPlayer = MediaPlayer.create(this, R.raw.tool)
        mediaPlayer?.setOnPreparedListener {
            println("Ready to go!")
        }

        pushButton.setOnTouchListener { _, event ->
            handleTouch(event)
                true
        }
    }

    fun returnToMainActivity(view: View) {
        val mainActivityIntent = Intent(this, MainActivity::class.java)

        startActivity(mainActivityIntent)
    }

    private fun handleTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                println("down")
                mediaPlayer?.start()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                println("up or cancel")
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
            }
            else -> {
                println("other")
            }
        }
    }


}
