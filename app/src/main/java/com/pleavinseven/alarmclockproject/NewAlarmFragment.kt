package com.pleavinseven.alarmclockproject

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.pleavinseven.alarmclockproject.databinding.FragmentNewAlarmBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil
import java.util.*


class NewAlarmFragment : Fragment() {

    val timePickerUtil = TimePickerUtil()
    lateinit var binding: FragmentNewAlarmBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewAlarmBinding.inflate(inflater, container, false)

        binding.fragmentCreateAlarmRecurring.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.fragmentCreateAlarmRecurring.visibility = View.VISIBLE
            } else {
                binding.fragmentCreateAlarmRecurring.visibility = View.GONE
            }
        })

        binding.fragmentBtnSetAlarm.setOnClickListener(View.OnClickListener { _ ->
            scheduleAlarm()

        })
        return binding.root



    }

    fun scheduleAlarm() {
        val alarmId = Random().nextInt(Integer.MAX_VALUE);
        val timePicker = binding.fragmentCreateAlarmTimePicker

        val alarm = Alarm(
                alarmId,
        timePickerUtil.getTimePickerHour(timePicker),
        timePickerUtil.getTimePickerMinute(timePicker),
        binding.fragmentCreateAlarmTitle.text.toString(),
        true,
        binding.fragmentCreateAlarmRecurring.isChecked
        )


        alarm.schedule(requireContext())
    }
}