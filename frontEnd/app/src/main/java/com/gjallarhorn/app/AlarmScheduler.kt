package com.gjallarhorn.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.widget.Toast

class AlarmScheduler {
    fun scheduleAlarm(context: Context, id: Int, hour: Int, minute: Int, text: String) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmPuller = PullAlarmSound()
        alarmPuller.pullRandomSound {
            val soundName = it.toURI().toString()
            val sound = Uri.parse(soundName)

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("alarmId", id)
            alarmIntent.putExtra("alarmName", soundName)
            alarmIntent.putExtra("alarmText", text)

            val pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0)

            val calendar: Calendar = Calendar.getInstance()
            val day = checkCurrentDay(calendar, hour, minute)
            calendar.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.DAY_OF_YEAR, day)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            if (id != 1) {
                val alarmIndex = AlarmList.getIndexById(id)
                AlarmList.setTime(alarmIndex, "$hour:$minute")
                AlarmList.setActive(alarmIndex, true)
                AlarmList.setSound(alarmIndex, sound)
            }

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT).cancel()
    }

    private fun checkCurrentDay(calendar: Calendar, hour: Int, minute: Int): Int {
        var currentDay = calendar.get(Calendar.DAY_OF_YEAR)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMin = calendar.get(Calendar.MINUTE)

        if ((hour == currentHour && minute < currentMin) || hour < currentHour)
            currentDay++
        return currentDay
    }
}