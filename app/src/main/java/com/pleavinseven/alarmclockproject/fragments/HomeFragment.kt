package com.pleavinseven.alarmclockproject.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.adapter.AlarmListAdapter
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.viewmodel.AlarmViewModel
import com.pleavinseven.alarmclockproject.databinding.FragmentHomeBinding
import java.time.Duration
import java.time.Instant


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var alarmViewModel: AlarmViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // RecyclerView
        val adapter = AlarmListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> container!!.setBackgroundResource(R.drawable.dark_gradient_background)
            Configuration.UI_MODE_NIGHT_NO -> container!!.background = ColorDrawable(Color.parseColor("#CDCCF0"))

        }

        //ViewModel
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        alarmViewModel.readAlarmData.observe(viewLifecycleOwner, Observer { alarm ->
            adapter.setData(alarm)
            setTvNextAlarm(adapter, alarm)
        })

        binding.btnAddAlarm.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_newAlarmFragment)
        }

        adapter.setOnItemClickListener(object : AlarmListAdapter.OnItemClickListener {

            // short click updates alarm
            override fun onClick(alarm: Alarm) {
                Navigation.findNavController(requireView())
                    .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateFragment(alarm))
            }

            // hold/ long click to delete alarm
            override fun onLongClick(alarm: Alarm) {
                val deleteBuilder = AlertDialog.Builder(requireContext())
                deleteBuilder.setPositiveButton(R.string.delete_builder_delete) { _, _ ->
                    alarmViewModel.delete(alarm)
                    Toast.makeText(
                        context,
                        "${context?.getString(R.string.delete_builder_alarm_deleted)}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                deleteBuilder.setNegativeButton(R.string.cancel_alarm) { _, _ ->
                    // do nothing
                }
                deleteBuilder.setTitle(R.string.title_delete)
                deleteBuilder.create().show()
            }

            override fun setSwitchOn(alarm: Alarm) {
                val toastTime = formatTime(alarm)
                val alarmManager = AlarmManager(
                    alarm.id,
                    alarm.hour,
                    alarm.minute,
                    true,
                    alarm.repeat,
                )
                alarmManager.cancel(requireContext())
                Toast.makeText(
                    context,
                    "${context?.getString(R.string.toast_alarm_set)} $toastTime",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun setSwitchOff(alarm: Alarm) {
                val alarmManager = AlarmManager(
                    alarm.id,
                    alarm.hour,
                    alarm.minute,
                    true,
                    alarm.repeat,
                )
                alarmManager.cancel(requireContext())
                Toast.makeText(
                    context,
                    "${context?.getString(R.string.toast_alarm_cancelled)}",
                Toast.LENGTH_SHORT
                ).show()
            }
        })

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTvNextAlarm(adapter: AlarmListAdapter, alarm: List<Alarm>) {
        // format alarm time to today's date
        var tvNextAlarm = 1441 // minutes in a day
        adapter.setData(alarm)
        val regex = Regex("\\d{2}:\\d{2}")
        val currentTime = Instant.now()
        val tomorrow = currentTime.plusMillis(86_400_000) // + 24 hours
        for (alarms in adapter.alarmList) {
            var stringTime = currentTime.toString().replace(regex, formatTime(alarms))
            var alarmTime = Instant.parse(stringTime)
            var duration = Duration.between(currentTime, alarmTime)
            // if alarm has passed or is current time, set alarm time to tomorrow's date
            if (duration.isNegative or duration.isZero) {
                stringTime = tomorrow.toString().replace(regex, formatTime(alarms))
                alarmTime = Instant.parse(stringTime)
                duration = Duration.between(currentTime, alarmTime)
            }
            if (duration.toMinutes() < tvNextAlarm) {
                tvNextAlarm = duration.toMinutes().toInt()
                binding.tvNextAlarmTime.text = formatTime(alarms)
            } else {
                continue
            }
        }
    }

    fun formatTime(alarm: Alarm): String {
        // make sure time is formatted to 24 hour clock correctly.
        return if (alarm.minute <= 9 && alarm.hour <= 9) {
            "0${alarm.hour}:0${alarm.minute}"
        } else if (alarm.minute <= 9) {
            "${alarm.hour}:0${alarm.minute}"
        } else if (alarm.hour <= 9) {
            "0${alarm.hour}:${alarm.minute}"
        } else {
            "${alarm.hour}:${alarm.minute}"
        }
    }
}


