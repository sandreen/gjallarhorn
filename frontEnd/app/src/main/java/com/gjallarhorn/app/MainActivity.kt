package com.gjallarhorn.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addAlarm = findViewById<FloatingActionButton>(R.id.addAlarm)
        addAlarm.setOnClickListener {
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
    }
}
