package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.databinding.FragmentAlarmRingBinding
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AlarmRingFragment : Fragment() {

    lateinit var binding: FragmentAlarmRingBinding
    var vibeOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmRingBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            }

        if (prefs.getBoolean("sp_vibrate_on_off", true)) {
            vibeOn = true
            vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        }

        wakeScreen()
        setBubble(true)
        setCurrentTimeText()

        binding.btnCancelAlarm.setOnClickListener {
            turnOffAlarm(vibe)
        }

        binding.btnSnoozeAlarm.setOnClickListener {
            snoozeAlarm(prefs)
            turnOffAlarm(vibe)
        }

        return binding.root
    }

    private fun wakeScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
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
        } else {
            activity?.window?.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun turnOffAlarm(vibe: Vibrator) {
        vibe.cancel()
        val intent = Intent(requireContext(), AlarmRingService::class.java)
        requireContext().stopService(intent)
    }

    private fun snoozeAlarm(prefs: SharedPreferences) {
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val calendar: Calendar = Calendar.getInstance()
        val snoozeMins = prefs.getString("lp_snooze", "-1")!!.toInt()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, snoozeMins)
        val alarm = AlarmManager(
            alarmId,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            started = true,
            recurring = false,
        )
        Toast.makeText(
            requireContext(),
            "Alarm snoozed for $snoozeMins minutes",
            Toast.LENGTH_SHORT
        ).show()
        alarm.schedule(requireContext())
    }

    private fun setBubble(
        isLeftVisible: Boolean,
    ) {
        R.id.alarm_bubble.apply {
            binding.alarmBubble.visibility = if (isLeftVisible) View.VISIBLE else View.GONE
        }
    }

    fun setCurrentTimeText(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = current.format(formatter)
        binding.currentTime.text = formattedTime
    }
}
