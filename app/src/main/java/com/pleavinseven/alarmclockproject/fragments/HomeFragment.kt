package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.viewModel.AlarmViewModel
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




        binding.btnAddAlarm.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newAlarmFragment)
        }
        return binding.root
    }
}