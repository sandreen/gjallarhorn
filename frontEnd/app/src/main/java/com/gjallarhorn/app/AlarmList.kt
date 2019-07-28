package com.gjallarhorn.app

import android.net.Uri

object AlarmList {
    private val alarmList = mutableListOf<Alarm>()

    fun addAlarm(id: Int, sound: Uri, active: Boolean, time: String) {
        val newAlarm = Alarm(id, sound, active, time)
        alarmList.add(newAlarm)
    }

    fun getIndex(index: Int): Alarm {
        return alarmList[index]
    }

    fun getSize(): Int {
        return alarmList.size
    }

    fun getId(index: Int): Int {
        return alarmList[index].getId()
    }

    fun getSound(index: Int): Uri {
        return alarmList[index].getSound()
    }

    fun getActive(index: Int): Boolean {
        return alarmList[index].getActive()
    }

    fun getTime(index: Int): String {
        return alarmList[index].getTime()
    }

    fun setSound(index: Int, sound: Uri) {
        alarmList[index].setSound(sound)
    }

    fun setActive(index: Int, value: Boolean) {
        alarmList[index].setActive(value)
    }
}