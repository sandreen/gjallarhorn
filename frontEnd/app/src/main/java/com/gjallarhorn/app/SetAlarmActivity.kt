package com.gjallarhorn.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        val alarmText = intent.getStringExtra("CardText")
        val alarmTime = intent.getStringExtra("CardTime")
        val alarmTimeSplit = alarmTime?.split(":")

        val text = findViewById<EditText>(R.id.enterCustomText)
        val clock = findViewById<TimePicker>(R.id.alarmTimeSet)

        if (alarmText != "" && alarmText != null)
            text.setText(alarmText)

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

        val textField = findViewById<EditText>(R.id.enterCustomText)
        var customText = textField.text.toString()

        if (customText == getString(R.string.enter_text))
            customText = ""

        if (AlarmList.isInList(requestId)) {
            val alarmIndex = AlarmList.getIndexById(requestId)
            AlarmList.setTime(alarmIndex, "$hour:$minute")
            AlarmList.setActive(alarmIndex, true)
            AlarmList.setCustomText(alarmIndex, customText)
        } else {
            alarmList.addAlarm(requestId, Uri.parse(""), true, "$hour:$minute", customText)
        }

        AlarmScheduler().scheduleAlarm(this, requestId, clock.hour, clock.minute, customText)

        closeAlarmScreen()
    }

    private fun closeAlarmScreen() {
        val mainScreenIntent = Intent(this, MainActivity::class.java)

        startActivity(mainScreenIntent)
    }
}