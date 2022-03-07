package com.pleavinseven.alarmclockproject.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.databinding.LayoutAlarmBinding

class AlarmListAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(binding: LayoutAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    private var alarmList = ArrayList<Alarm>()


    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(alarm: Alarm)
        fun onLongClick(alarm: Alarm)
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
        val minute = currentItem.minute
        holder.itemView.findViewById<TextView>(R.id.tv_alarm_time).text =
            if (minute >= 10) {
                "${currentItem.hour}:${currentItem.minute}"
            } else {
                "${currentItem.hour}:0${currentItem.minute}"
            }

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
