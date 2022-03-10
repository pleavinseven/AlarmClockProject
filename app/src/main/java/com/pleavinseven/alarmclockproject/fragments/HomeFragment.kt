package com.pleavinseven.alarmclockproject.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> container!!.setBackgroundResource(R.drawable.alarm_app_dark_background)
            Configuration.UI_MODE_NIGHT_NO -> container!!.setBackgroundResource(R.drawable.alarm_app_light_background)
        }

        // RecyclerView
        val adapter = AlarmListAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //ViewModel
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        alarmViewModel.readAlarmData.observe(viewLifecycleOwner, Observer { alarm ->
            adapter.setData(alarm)
        })

        binding.btnAddAlarm.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_newAlarmFragment)
        }

        adapter.setOnItemClickListener(object : AlarmListAdapter.OnItemClickListener {

            override fun onClick(alarm: Alarm) {
                Navigation.findNavController(requireView())
                    .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateFragment(alarm))
            }

            override fun onLongClick(alarm: Alarm) {
                val deleteBuilder = AlertDialog.Builder(requireContext())
                deleteBuilder.setPositiveButton("Delete") { _, _ ->
                    alarmViewModel.delete(alarm)
                    Toast.makeText(context, "Alarm Deleted", Toast.LENGTH_SHORT)
                        .show()
                }
                deleteBuilder.setNegativeButton("Cancel") { _, _ ->
                    // do nothing
                }
                deleteBuilder.setTitle("Delete Alarm?")
                deleteBuilder.create().show()
            }

            override fun setSwitchOn(alarm: Alarm) {
                val toastTime = if (alarm.minute > 9){
                    "${alarm.hour}:${alarm.minute}"
                }else{
                    "${alarm.hour}:0${alarm.minute}"
                }
                val alarmManager = AlarmManager(
                    alarm.id,
                    alarm.hour,
                    alarm.minute,
                    true,
                    alarm.repeat
                )
                alarmManager.cancel(requireContext())
                Toast.makeText(context, "Alarm set for $toastTime", Toast.LENGTH_SHORT).show()
            }

            override fun setSwitchOff(alarm: Alarm) {
                val alarmManager = AlarmManager(
                    alarm.id,
                    alarm.hour,
                    alarm.minute,
                    true,
                    alarm.repeat
                )
                alarmManager.cancel(requireContext())
                Toast.makeText(context, "Alarm cancelled", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}


