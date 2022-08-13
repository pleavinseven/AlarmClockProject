package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.databinding.FragmentDaysBinding


class DaysFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDaysBinding.inflate(inflater, container, false)
        val destination = arguments?.getInt("myArg")
        val alarm = arguments?.get("currentAlarm1")


        binding.fragmentBtnSetAlarmDays.setOnClickListener {
            if (destination == 0){
                Navigation.findNavController(requireView())
                    .navigate(DaysFragmentDirections.actionDaysFragmentToUpdateFragment(alarm as Alarm))
            } else {
                Navigation.findNavController(requireView())
                    .navigate(DaysFragmentDirections.actionDaysFragmentToNewAlarmFragment())
            }
        }

        return binding.root
    }
}