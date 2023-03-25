package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.viewmodel.AlarmViewModel
import com.pleavinseven.alarmclockproject.databinding.FragmentSetNewAlarmBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil
import java.util.*


class SetNewAlarmFragment : Fragment() {

    private val timePickerUtil = TimePickerUtil()
    private lateinit var binding: FragmentSetNewAlarmBinding
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSetNewAlarmBinding.inflate(inflater, container, false)

        val snoozeEntries = resources.getStringArray(R.array.snooze_entries)
        val snoozeValues = resources.getStringArray(R.array.snooze_values)

        val snoozeMap: Map<String, String> = snoozeEntries.zip(snoozeValues).toMap()

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        binding.addAlarmBtnSetAlarm.setOnClickListener {
            scheduleAlarm(snoozeMap)
            Navigation.findNavController(requireView())
                .navigate(SetNewAlarmFragmentDirections.actionNewAlarmFragmentToHomeFragment())
        }

        return binding.root
    }

    private fun insertToDatabase(
        hour: Int,
        minute: Int,
        started: Boolean,
        once: Boolean,
        vibrate: Boolean,
        shake: Boolean,
        snooze: Int
    ) {
        val alarm = Alarm(0, hour, minute, started, once, vibrate, shake, snooze)
        alarmViewModel.addAlarm(alarm)
    }

    private fun scheduleAlarm(snoozeMap: Map<String,String>) {
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val timePicker = binding.addAlarmTimePicker
        val hour = timePickerUtil.getTimePickerHour(timePicker)
        val minute = timePickerUtil.getTimePickerMinute(timePicker)
        val started = true
        val once = binding.addAlarmSwitchOnce.isChecked
        val vibrate = binding.addAlarmSwitchVibrate.isChecked
        val shake = binding.addAlarmSwitchShake.isChecked
        val snooze = snoozeMap[binding.addAlarmSwitchSnooze.selectedItem]!!.toInt()

        val alarmManager = AlarmManager(
            alarmId,
            hour,
            minute,
            true,
            once,
            vibrate,
            shake,
            snooze
        )

        insertToDatabase(hour, minute, started, once, vibrate, shake, snooze)
        alarmManager.schedule(requireContext())
    }
}