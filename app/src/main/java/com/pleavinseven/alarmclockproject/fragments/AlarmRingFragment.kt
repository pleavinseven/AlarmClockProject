package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.databinding.FragmentAlarmRingBinding
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AlarmRingFragment : Fragment() {

    lateinit var binding: FragmentAlarmRingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmRingBinding.inflate(inflater, container, false)

        val bundle = arguments
        val vibrate = bundle?.getBoolean("vibrate")
        val snoozeMins = bundle?.getInt("snooze")

        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            }

        wakeScreen()
        setCurrentTimeText()

        binding.btnCancelAlarm.setOnClickListener {
            turnOffAlarm(vibe)
        }

        binding.btnSnoozeAlarm.setOnClickListener {
            snoozeAlarm(snoozeMins!!)
            turnOffAlarm(vibe)
        }

        if (vibrate!!) {
            vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        }

        return binding.root
    }

    private fun wakeScreen() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        activity?.setTurnScreenOn(true)
        activity?.setShowWhenLocked(true)
        (activity?.getSystemService(KeyguardManager::class.java) as KeyguardManager).requestDismissKeyguard(
            requireActivity(),
            object : KeyguardManager.KeyguardDismissCallback() {
                override fun onDismissCancelled() {}
                override fun onDismissError() {}
                override fun onDismissSucceeded() {}
            }
        )
    }

    private fun turnOffAlarm(vibe: Vibrator) {
        vibe.cancel()
        val intent = Intent(requireContext(), AlarmRingService::class.java)
        requireContext().stopService(intent)
    }

    private fun snoozeAlarm(snoozeMins: Int) {
        if (snoozeMins <= 0) return
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, snoozeMins)
        val alarm = AlarmManager(
            alarmId,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            started = true,
            once = false,
            vibrate = true,
            snooze = 0,
            shake = false
        )
        Toast.makeText(
            requireContext(),
            "Alarm snoozed for $snoozeMins minutes",
            Toast.LENGTH_SHORT
        ).show()
        alarm.schedule(requireContext())
    }


    private fun setCurrentTimeText() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = current.format(formatter)
        binding.currentTime.text = formattedTime
    }
}
