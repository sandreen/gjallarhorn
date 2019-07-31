package com.gjallarhorn.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AlarmScheduler {
    private val alarmSet = AlarmList

    fun scheduleAlarm(context: Context, id: Int, hour: Int, minute: Int) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val recyclerView = View(context).findViewById<RecyclerView>(R.id.alarmListView)

        val alarmPuller = PullAlarmSound()
        alarmPuller.pullRandomSound {
            val soundName = it.toURI().toString()
            val sound = Uri.parse(soundName)

            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("alarmId", id)
            alarmIntent.putExtra("alarmName", soundName)

            val pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0)

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
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
}