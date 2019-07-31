package com.gjallarhorn.app

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SnoozeAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("alarmId", -1)

        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)

        val splitTime = formatted.split(":")
        var hour = splitTime[0].toInt()
        var minute = splitTime[1].toInt()

        minute = (minute + 1) % 60
        if (minute < 1)
            hour++

        AlarmScheduler().scheduleAlarm(context, 1, hour, minute)

        Toast.makeText(context, "Snoozing until $hour:$minute", Toast.LENGTH_LONG).show()

        val alarmControl = AlarmSoundControl
        alarmControl.stopPlayer()
    }
}