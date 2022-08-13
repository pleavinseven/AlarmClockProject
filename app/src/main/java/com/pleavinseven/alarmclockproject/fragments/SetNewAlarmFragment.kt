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
    private var new = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSetNewAlarmBinding.inflate(inflater, container, false)

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding.fragmentBtnSetAlarm.setOnClickListener {
            scheduleAlarm()
            Navigation.findNavController(requireView())
                .navigate(SetNewAlarmFragmentDirections.actionNewAlarmFragmentToHomeFragment())
        }

        binding.fragmentCreateAlarmDays.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(SetNewAlarmFragmentDirections.actionNewAlarmFragmentToDaysFragment())
        }

        return binding.root
    }

    private fun insertToDatabase(hour: Int, minute: Int, started: Boolean, repeat: Boolean) {
        val alarm = Alarm(0, hour, minute, started, repeat)
        alarmViewModel.addAlarm(alarm)
    }

    private fun scheduleAlarm() {
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val timePicker = binding.fragmentCreateAlarmTimePicker
        val hour = timePickerUtil.getTimePickerHour(timePicker)
        val minute = timePickerUtil.getTimePickerMinute(timePicker)
        val started = true
        val repeat = binding.fragmentCreateAlarmRecurring.isChecked

        val alarmManager = AlarmManager(
            alarmId,
            hour,
            minute,
            true,
            repeat
        )

        insertToDatabase(hour, minute, started, repeat)
        alarmManager.schedule(requireContext())
    }
}