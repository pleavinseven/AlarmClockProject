package com.pleavinseven.alarmclockproject.fragments

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

        //test click listener
        adapter.setOnItemClickListener(object : AlarmListAdapter.OnItemClickListener {
            override fun onLongClick(alarm: Alarm) {
                val deleteBuilder = AlertDialog.Builder(requireContext())
                deleteBuilder.setPositiveButton("Delete") { _, _ ->
                    alarmViewModel.delete(alarm)
                    Toast.makeText(context, "Alarm Deleted", Toast.LENGTH_SHORT)
                        .show()
                }
                deleteBuilder.setNegativeButton("Cancel") { _, _ ->

                }
                deleteBuilder.setTitle("Delete Alarm?")
                deleteBuilder.create().show()
            }
        })


        return binding.root
    }
}


