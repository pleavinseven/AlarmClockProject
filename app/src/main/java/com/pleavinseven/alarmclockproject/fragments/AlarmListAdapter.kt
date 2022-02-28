package com.pleavinseven.alarmclockproject.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.database.Alarm
import com.pleavinseven.alarmclockproject.databinding.FragmentHomeBinding

class AlarmListAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var alarmList = emptyList<Alarm>()

    class ViewHolder(binding: FragmentHomeBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = FragmentHomeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = alarmList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_alarm_time).text =
            "${currentItem.hour}:${currentItem.minute}"
        //holder.itemView.findViewById<TextView>(R.id.tv_repeat_days)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

}