package com.pleavinseven.alarmclockproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pleavinseven.alarmclockproject.databinding.FragmentAlarmBinding



class AlarmFragment : Fragment() {

    lateinit var binding: FragmentAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        return binding.root
    }
}