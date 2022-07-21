package com.pleavinseven.alarmclockproject.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.databinding.LayoutAlarmBinding

class AlarmListAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(binding: LayoutAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    var alarmList = ArrayList<Alarm>()


    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(alarm: Alarm)
        fun onLongClick(alarm: Alarm)
        fun setSwitchOn(alarm: Alarm)
        fun setSwitchOff(alarm: Alarm)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutAlarmBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = alarmList[position]
        val recurring = currentItem.repeat
        val switch = holder.itemView.findViewById<Switch>(R.id.switch_alarm)

        //set time text
        holder.itemView.findViewById<TextView>(R.id.tv_alarm_time).text = formatTime(currentItem)

        //set repeat text
        holder.itemView.findViewById<TextView>(R.id.tv_repeat_days).text =
            if (recurring) {
                holder.itemView.context.getString(R.string.daily_alarm)
            }
            else
                holder.itemView.context.getString(R.string.once_alarm)


        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener?.onClick(currentItem)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (onItemClickListener != null) {
                onItemClickListener?.onLongClick(currentItem)
            }
            true
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (onItemClickListener != null) {
                if (isChecked) {
                    onItemClickListener?.setSwitchOn(currentItem)
                } else {
                    onItemClickListener?.setSwitchOff(currentItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    fun setData(alarm: List<Alarm>) {
        alarmList.clear()
        alarmList.addAll(alarm)
        notifyDataSetChanged()
    }

    private fun formatTime(currentItem: Alarm): String{
        return if (currentItem.minute <= 9 && currentItem.hour <= 9) {
            "0${currentItem.hour}:0${currentItem.minute}"
        } else if (currentItem.minute <= 9) {
            "${currentItem.hour}:0${currentItem.minute}"
        } else if (currentItem.hour <= 9) {
            "0${currentItem.hour}:${currentItem.minute}"
        } else {
            "${currentItem.hour}:${currentItem.minute}"
        }
    }
}
