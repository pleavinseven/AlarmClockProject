package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.viewmodel.AlarmViewModel
import com.pleavinseven.alarmclockproject.databinding.FragmentSetNewAlarmBinding
import com.pleavinseven.alarmclockproject.databinding.FragmentUpdateBinding
import com.pleavinseven.alarmclockproject.util.TimePickerUtil
import java.util.*


class UpdateFragment : Fragment() {

    private val timePickerUtil = TimePickerUtil()
    lateinit var binding: FragmentUpdateBinding
    private lateinit var alarmViewModel: AlarmViewModel
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        binding.fragmentBtnUpdateAlarm.setOnClickListener {
            updateAlarm()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_updateFragment_to_homeFragment)
        }
        return binding.root
    }

    private fun updateDatabase(id: Int, hour: Int, minute: Int, repeat: Boolean) {
        val alarm = Alarm(id, hour, minute, repeat)
        alarmViewModel.update(alarm)
    }

    private fun updateAlarm() {
        val timePicker = binding.fragmentUpdateAlarmTimePicker
        val id = args.currentAlarm.id
        val hour = timePickerUtil.getTimePickerHour(timePicker)
        val minute = timePickerUtil.getTimePickerMinute(timePicker)
        val repeat = binding.fragmentUpdateAlarmRecurring.isChecked

        val alarmManager = AlarmManager(
            id,
            hour,
            minute,
            true,
            binding.fragmentUpdateAlarmRecurring.isChecked
        )

        updateDatabase(id, hour, minute, repeat)
        alarmManager.schedule(requireContext())
    }


}