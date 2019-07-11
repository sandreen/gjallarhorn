package com.gjallarhorn.app

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            myIntent,
            FLAG_ONE_SHOT
        )

        val builder = NotificationCompat.Builder(context, "100")
            .setContentTitle("Alarm ringing")
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setContentText("Swipe to dismiss")
            .setSmallIcon(R.drawable.abc_ic_arrow_drop_right_black_24dp)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify((0..100).random(), builder.build())
        }
    }
}