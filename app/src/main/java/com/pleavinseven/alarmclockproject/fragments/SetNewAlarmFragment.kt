package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.viewmodel.AlarmViewModel
import com.pleavinseven.alarmclockproject.databinding.FragmentSetNewAlarmBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil
import java.util.*


class SetNewAlarmFragment : Fragment() {

    private val timePickerUtil = TimePickerUtil()
    lateinit var binding: FragmentSetNewAlarmBinding
    private lateinit var alarmViewModel: AlarmViewModel


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

        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)


        binding.fragmentBtnSetAlarm.setOnClickListener(View.OnClickListener { _ ->
            scheduleAlarm()
            Navigation.findNavController(requireView())
                .navigate(com.pleavinseven.alarmclockproject.R.id.action_newAlarmFragment_to_homeFragment)
        })
        return binding.root


    }

    private fun insertToDatabase(hour: Int, minute: Int, repeat: Boolean) {
        val alarm = Alarm(0, hour, minute, repeat)
        alarmViewModel.addAlarm(alarm)
    }

    private fun scheduleAlarm() {
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val timePicker = binding.fragmentCreateAlarmTimePicker
        val hour = timePickerUtil.getTimePickerHour(timePicker)
        val minute = timePickerUtil.getTimePickerMinute(timePicker)
        val repeat = binding.fragmentCreateAlarmRecurring.isChecked

        val alarmManager = AlarmManager(
            alarmId,
            hour,
            minute,
            binding.fragmentCreateAlarmTitle.text.toString(),
            true,
            binding.fragmentCreateAlarmRecurring.isChecked
        )

        insertToDatabase(hour, minute, repeat)
        alarmManager.schedule(requireContext())
    }
}