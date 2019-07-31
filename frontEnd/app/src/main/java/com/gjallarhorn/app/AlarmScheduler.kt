package com.gjallarhorn.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri

class AlarmScheduler {
    private val alarmSet = AlarmList

    fun scheduleAlarm(context: Context, id: Int, hour: Int, minute: Int) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val soundName = "android.resource://" + context.packageName + "/raw/funny_fanfare_sound"

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra("alarmName", soundName)
        alarmIntent.putExtra("alarmId", id)

        val pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0)

        val sound = Uri.parse(soundName)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (id != 1) {
            if (AlarmList.isInList(id)) {
                val alarmIndex = AlarmList.getIndexById(id)
                AlarmList.setTime(alarmIndex, "$hour:$minute")
                AlarmList.setActive(alarmIndex, true)
            } else {
                alarmSet.addAlarm(id, sound, true, "$hour:$minute")
            }
        }

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT).cancel()
    }
}