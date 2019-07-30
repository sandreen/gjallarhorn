package com.gjallarhorn.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class SetAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        val alarmTime = intent.getStringExtra("CardTime")
        val alarmTimeSplit = alarmTime?.split(":")

        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)

        if (alarmTimeSplit?.size == 2) {
            clock.hour = alarmTimeSplit[0].toInt()
            clock.minute = alarmTimeSplit[1].toInt()
        }
    }

    fun setAlarmTime(view: View) {
        var requestId = intent.getIntExtra("CardID", -1)

        if (requestId == -1)
            requestId = (0..1000).random()

        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)

        AlarmScheduler().scheduleAlarm(this, requestId, clock.hour, clock.minute)

        closeAlarmScreen()
    }

    private fun closeAlarmScreen() {
        val mainScreenIntent = Intent(this, MainActivity::class.java)

        startActivity(mainScreenIntent)
    }
}