package com.gjallarhorn.app

import android.content.Intent
import android.net.Uri
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
        val alarmList = AlarmList
        var requestId = intent.getIntExtra("CardID", -1)

        if (requestId == -1)
            requestId = (2..1000).random()

        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)
        val hour = clock.hour
        val minute = clock.minute

        if (AlarmList.isInList(requestId)) {
            val alarmIndex = AlarmList.getIndexById(requestId)
            AlarmList.setTime(alarmIndex, "$hour:$minute")
            AlarmList.setActive(alarmIndex, true)
        } else {
            alarmList.addAlarm(requestId, Uri.parse(""), true, "$hour:$minute")
        }

        AlarmScheduler().scheduleAlarm(this, requestId, clock.hour, clock.minute)

        closeAlarmScreen()
    }

    private fun closeAlarmScreen() {
        val mainScreenIntent = Intent(this, MainActivity::class.java)

        startActivity(mainScreenIntent)
    }
}