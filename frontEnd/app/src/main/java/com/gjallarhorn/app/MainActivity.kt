package com.gjallarhorn.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val alarmList = AlarmList

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

        viewManager = LinearLayoutManager(this)
        viewAdapter = AlarmListAdapter(alarmList)

        recyclerView = findViewById<RecyclerView>(R.id.alarmListView).apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
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
        val setAlarmIntent = Intent(this, SetAlarmActivity::class.java)

        startActivity(setAlarmIntent)
    }
}
