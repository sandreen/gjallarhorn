package com.gjallarhorn.app

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        textBox.text = myDataset.getIndex(position).getTime()
    }

    override fun getItemCount() = myDataset.getSize()
}