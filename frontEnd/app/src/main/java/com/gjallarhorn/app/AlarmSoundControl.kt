package com.gjallarhorn.app

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object AlarmSoundControl {
    private val mediaPlayer = MediaPlayer()

    fun init(context: Context, sound: Uri) {
        mediaPlayer.reset()
        mediaPlayer.apply {
            isLooping = true
            setDataSource(context, sound)
            prepare()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun stopPlayer() {
        mediaPlayer.stop()
    }
}
