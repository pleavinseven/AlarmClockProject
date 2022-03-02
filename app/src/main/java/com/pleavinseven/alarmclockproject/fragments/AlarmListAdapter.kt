package com.pleavinseven.alarmclockproject.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.databinding.LayoutAlarmBinding

class AlarmListAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Alarm) -> Unit)? = null
    private var alarmList = ArrayList<Alarm>()


    inner class MyViewHolder(binding: LayoutAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutAlarmBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = alarmList[position]
        val minute = currentItem.minute
        holder.itemView.findViewById<TextView>(R.id.tv_alarm_time).text =
            if (minute >= 10) {
                "${currentItem.hour}:${currentItem.minute}"
            } else {
                "${currentItem.hour}:0${currentItem.minute}"
            }

        holder.itemView.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        //holder.itemView.findViewById<TextView>(R.id.tv_repeat_days)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    fun setData(alarm: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(alarm)
        notifyDataSetChanged()
    }

}