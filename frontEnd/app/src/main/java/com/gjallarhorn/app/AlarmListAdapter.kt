package com.gjallarhorn.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class AlarmListAdapter(private val myDataset: AlarmList) : RecyclerView.Adapter<AlarmListAdapter.AlarmCardViewHolder>() {
    class AlarmCardViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmCardViewHolder {
        val alarmView = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_entry, parent, false) as LinearLayout
        return AlarmCardViewHolder(alarmView)
    }

    override fun onBindViewHolder(holder: AlarmCardViewHolder, position: Int) {
        val textBox = holder.layout.findViewById<TextView>(R.id.card_alarm_time)
        val time = myDataset.getIndex(position).getTime()
        textBox.text = padMinutes(time)

        val switch = holder.layout.findViewById<Switch>(R.id.card_alarm_toggle)
        switch.isChecked = myDataset.getIndex(position).getActive()
        switch.setOnClickListener { view ->
            val context = view.context
            val id = myDataset.getIndex(position).getId()
            val scheduler = AlarmScheduler()

            if (!switch.isChecked) {
                myDataset.setActive(position, false)
                scheduler.cancelAlarm(context, id)
            } else {
                val alarmTime = time.split(":")
                val hour = alarmTime[0].toInt()
                val minute = alarmTime[1].toInt()

                myDataset.setActive(position, true)

                scheduler.scheduleAlarm(context, id, hour, minute)
            }
        }

        val deleteButton = holder.layout.findViewById<Button>(R.id.card_delete_button)
        deleteButton.setOnClickListener { view ->
            val alarmList = AlarmList

            val context = view.context
            val id = myDataset.getIndex(position).getId()
            val scheduler = AlarmScheduler()

            alarmList.deleteElement(position)
            scheduler.cancelAlarm(context, id)
            this.notifyDataSetChanged()
        }

        val editButton = holder.layout.findViewById<Button>(R.id.card_edit_button)
        editButton.setOnClickListener { view ->
            val context = view.context
            val id = myDataset.getIndex(position).getId()

            val setAlarmIntent = Intent(context, SetAlarmActivity::class.java)
            setAlarmIntent.putExtra("CardID", id)
            setAlarmIntent.putExtra("CardTime", myDataset.getIndex(position).getTime())

            context.startActivity(setAlarmIntent)
        }
    }

    private fun padMinutes(time: String): String {
        val splitTime = time.split(":")
        val hour = splitTime[0]
        val minute = splitTime[1].toInt()

        return when (minute < 10) {
            true -> "$hour:0$minute"
            false -> time
        }
    }

    override fun getItemCount() = myDataset.getSize()
}