package com.gjallarhorn.app

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sound = intent.getStringExtra("alarmName")
        val id = intent.getIntExtra("alarmId", 0)
        val text = intent.getStringExtra("alarmText")
        val alarmPath = sound.substring(sound.lastIndexOf("/")+1)
        val alarmTitle: String
        if (alarmPath.indexOf(".") > 0) {
            alarmTitle = alarmPath.substring(0, alarmPath.lastIndexOf("-"))
        } else {
            alarmTitle = alarmPath
        }

        val mediaPlayer = AlarmSoundControl
        mediaPlayer.init(context, Uri.parse(sound))
        mediaPlayer.startPlayer()

        val stopAlarmIntent = Intent(context, StopAlarmReceiver::class.java)
        stopAlarmIntent.putExtra("alarmId", id)
        val pendingStopIntent = PendingIntent.getBroadcast(context, id, stopAlarmIntent, FLAG_ONE_SHOT)

        val snoozeAlarmIntent = Intent(context, SnoozeAlarmReceiver::class.java)
        snoozeAlarmIntent.putExtra("alarmId", id)
        snoozeAlarmIntent.putExtra("alarmText", text)
        val pendingSnoozeIntent = PendingIntent.getBroadcast(context, id, snoozeAlarmIntent, FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, "100")
            .setContentTitle("Alarm ringing: $text")
            .setContentText("Now playing: $alarmTitle")
            .setSmallIcon(R.drawable.abc_ic_arrow_drop_right_black_24dp)
            .setSound(null)
            .setAutoCancel(true)
            .addAction(R.drawable.abc_ic_star_black_36dp, "Stop", pendingStopIntent)
            .addAction(R.drawable.abc_ic_star_black_36dp, "Snooze", pendingSnoozeIntent)
            .setDeleteIntent(pendingStopIntent)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }
}