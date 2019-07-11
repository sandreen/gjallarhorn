package com.gjallarhorn.app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the NotificationChannel
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("100", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    fun showOptionButtons(view: View) {
        val addAlarm = view as FloatingActionButton
        val recordButton = findViewById<FloatingActionButton>(R.id.recordButton)
        val setAlarmButton = findViewById<FloatingActionButton>(R.id.setAlarmButton)
        if (recordButton.visibility == View.VISIBLE) {
            addAlarm.setImageDrawable(ContextCompat.getDrawable(applicationContext, android.R.drawable.ic_input_add))
            recordButton.hide()
            setAlarmButton.hide()
        } else {
            addAlarm.setImageDrawable(ContextCompat.getDrawable(applicationContext, android.R.drawable.ic_delete))
            recordButton.show()
            setAlarmButton.show()
        }
    }

    fun showTimeSelection(view: View) {
        val time = findViewById<TimePicker>(R.id.alarmTimeSet)
        val button = findViewById<Button>(R.id.submitAlarmButton)
        if (time.visibility == View.VISIBLE) {
            time.visibility = View.INVISIBLE
            button.visibility = View.INVISIBLE
        } else {
            showOptionButtons(findViewById<FloatingActionButton>(R.id.mainAction))
            time.visibility = View.VISIBLE
            button.visibility = View.VISIBLE
        }
    }

    fun setAlarmTime(view: View) {
        val alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, alarmIntent, 0)

        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)

        val calendar: Calendar = Calendar.getInstance().apply{
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, clock.hour)
            set(Calendar.MINUTE, clock.minute)
            set(Calendar.SECOND, 0)
        }

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(applicationContext,"Alarm set for ${clock.hour}:${clock.minute}",Toast.LENGTH_LONG)
            .show()
        showTimeSelection(findViewById<TimePicker>(R.id.alarmTimeSet))
    }
}
