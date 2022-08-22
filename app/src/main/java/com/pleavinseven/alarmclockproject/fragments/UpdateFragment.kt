package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.viewmodel.AlarmViewModel
import com.pleavinseven.alarmclockproject.databinding.FragmentUpdateBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil


class UpdateFragment : Fragment() {

    private val timePickerUtil = TimePickerUtil()
    lateinit var binding: FragmentUpdateBinding
    private lateinit var alarmViewModel: AlarmViewModel
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding.fragmentBtnUpdateAlarm.setOnClickListener {
            updateAlarm()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_updateFragment_to_homeFragment)
        }

        binding.fragmentCreateAlarmDays.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(UpdateFragmentDirections.actionUpdateFragmentToDaysFragment(0, args.currentAlarm))
        }

        return binding.root
    }

    private fun updateDatabase(id: Int, hour: Int, minute: Int, started: Boolean, repeat: Boolean, vibrate: Boolean, shake: Boolean, snooze: Int) {
        val alarm = Alarm(id, hour, minute, started, repeat, vibrate, shake, snooze)
        alarmViewModel.update(alarm)
    }

    private fun updateAlarm() {
        val timePicker = binding.fragmentUpdateAlarmTimePicker
        val id = args.currentAlarm.id
        val hour = timePickerUtil.getTimePickerHour(timePicker)
        val minute = timePickerUtil.getTimePickerMinute(timePicker)
        val started = true
        val repeat = binding.fragmentUpdateAlarmRecurring.isChecked
        val vibrate = binding.fragmentUpdateAlarmVibrate.isChecked
        val shake = binding.fragmentUpdateAlarmShakeToWake.isChecked
        val snooze = binding.fragmentUpdateAlarmSnooze.selectedItem as Int

        val alarmManager = AlarmManager(
            id,
            hour,
            minute,
            true,
            repeat,
            vibrate,
            shake,
            snooze
        )

        updateDatabase(id, hour, minute, started, repeat, vibrate, shake, snooze)
        alarmManager.cancel(requireContext())
        alarmManager.schedule(requireContext())
    }
}