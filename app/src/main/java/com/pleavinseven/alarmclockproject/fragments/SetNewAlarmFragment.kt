package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.AlarmManager
import com.pleavinseven.alarmclockproject.databinding.FragmentSetNewAlarmBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil
import java.util.*


class SetNewAlarmFragment : Fragment() {

    private val timePickerUtil = TimePickerUtil()
    lateinit var binding: FragmentSetNewAlarmBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSetNewAlarmBinding.inflate(inflater, container, false)

        binding.fragmentCreateAlarmRecurring.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.fragmentCreateAlarmRecurring.visibility = View.VISIBLE
            } else {
                binding.fragmentCreateAlarmRecurring.visibility = View.GONE
            }
        })

        binding.fragmentBtnSetAlarm.setOnClickListener(View.OnClickListener { _ ->
            scheduleAlarm()
            Navigation.findNavController(requireView())
                .navigate(com.pleavinseven.alarmclockproject.R.id.action_newAlarmFragment_to_homeFragment)

        })
        return binding.root


    }

    private fun scheduleAlarm() {
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val timePicker = binding.fragmentCreateAlarmTimePicker

        val alarm = AlarmManager(
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