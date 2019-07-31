package com.gjallarhorn.app

import android.net.Uri

class Alarm (id: Int, sound: Uri, active: Boolean, time: String) {
    private var alarmId = id
    private var alarmSound = sound
    private var alarmActive = active
    private var alarmTime = time

    fun getId(): Int {
        return alarmId
    }

    fun getSound(): Uri {
        return alarmSound
    }

    fun getActive(): Boolean {
        return alarmActive
    }

    fun getTime(): String {
        return alarmTime
    }

    fun setSound(sound: Uri) {
        alarmSound = sound
    }

    fun setActive(value: Boolean) {
        alarmActive = value
    }

    fun setTime(newTime: String) {
        alarmTime = newTime
    }
}