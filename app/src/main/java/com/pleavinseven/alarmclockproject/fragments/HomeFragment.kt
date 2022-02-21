package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // TODO: set on click listener for layout here
        //  .setOnClickListener{
    //  Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newAlarmFragment)
//        }
//        binding.btnSetAlarm2.setOnClickListener{
//            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newAlarmFragment)
//        }
//        binding.btnSetAlarm3.setOnClickListener{
//            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newAlarmFragment)
//        }
//        binding.btnAddAlarm.setOnClickListener{
//
//        }

        //placeholder:
        binding.button.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_newAlarmFragment)
        }
        return binding.root
    }
}