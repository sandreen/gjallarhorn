package com.gjallarhorn.app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = applicationContext

        // Create the NotificationChannel
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("100", name, importance)
        mChannel.description = descriptionText
        mChannel.setSound(null, null)

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)

        val alarmList = AlarmList

        viewManager = LinearLayoutManager(this)
        viewAdapter = AlarmListAdapter(alarmList)

        recyclerView = findViewById<RecyclerView>(R.id.alarmListView).apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        Toast.makeText(applicationContext,"Number of alarms is ${alarmList.getSize()}", Toast.LENGTH_LONG)
            .show()
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

        val currTime = LocalDateTime.now()
        time.hour = currTime.format(DateTimeFormatter.ofPattern("HH")).toInt()
        time.minute = currTime.format(DateTimeFormatter.ofPattern("mm")).toInt()

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
        val requestId = (0..1000).random()
        val alarmSet = AlarmList
        val sound = Uri.parse("android.resource://" + this.packageName + "/raw/funny_fanfare_sound")
        val alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, requestId, alarmIntent, 0)

        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)

        val calendar: Calendar = Calendar.getInstance().apply{
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, clock.hour)
            set(Calendar.MINUTE, clock.minute)
            set(Calendar.SECOND, 0)
        }

        alarmSet.addAlarm(requestId, sound, true, "${clock.hour}:${clock.minute}")
        viewAdapter.notifyDataSetChanged()

        Toast.makeText(applicationContext,"Alarm set for ${clock.hour}:${clock.minute}", Toast.LENGTH_LONG)
            .show()

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        showTimeSelection(findViewById<TimePicker>(R.id.alarmTimeSet))
    }
}
